package com.hao.easy.base.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import com.hao.easy.base.common.DataListResult
import com.hao.easy.base.datasource.DataSourceFactory
import com.hao.easy.base.datasource.PagedDataLoader

abstract class BaseListViewModel<T> : BaseViewModel(), PagedDataLoader<T> {

    open fun pageSize(): Int {
        return 10
    }

    private val dataSourceFactory: DataSourceFactory<T> by lazy {
        DataSourceFactory(this)
    }

    val loadLiveData = LivePagedListBuilder(dataSourceFactory, pageSize()).build()

    val refreshLiveData: MutableLiveData<DataListResult> = MutableLiveData()

    val loadMoreLiveData: MutableLiveData<DataListResult> by lazy { MutableLiveData<DataListResult>() }

    val notifyItemLiveData: MutableLiveData<Pair<Int, Any?>> by lazy { MutableLiveData<Pair<Int, Any?>>() }

    val removeItemLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    fun refresh() {
        dataSourceFactory.sourceLiveData.value?.invalidate()
    }

    @CallSuper
    override fun refresh(callback: PageKeyedDataSource.LoadInitialCallback<Int, T>) {
        loadData(1) {
            when {
                it == null -> refreshLiveData.value = DataListResult.FAILED
                it.isEmpty() -> refreshLiveData.value = DataListResult.NO_DATA
                it.size < pageSize() -> {
                    callback.onResult(it, null, null)
                    refreshLiveData.value = DataListResult.NO_MORE
                }
                else -> {
                    callback.onResult(it, null, 2)
                    refreshLiveData.value = DataListResult.SUCCEED
                }
            }
        }
    }

    @CallSuper
    override fun loadMore(key: Int, callback: PageKeyedDataSource.LoadCallback<Int, T>) {
        loadData(key) {
            when {
                it == null -> loadMoreLiveData.value = DataListResult.FAILED
                it.size < pageSize() -> {
                    callback.onResult(it, null)
                    loadMoreLiveData.value = DataListResult.NO_MORE
                }
                else -> {
                    callback.onResult(it, key + 1)
                    loadMoreLiveData.value = DataListResult.SUCCEED
                }
            }
        }
    }

    fun notifyItem(position: Int, payload: Any? = null) {
        notifyItemLiveData.value = Pair(position, payload)
    }

    fun removeItem(position: Int) {
        removeItemLiveData.value = position
    }

    abstract fun loadData(page: Int, onResponse: (ArrayList<T>?) -> Unit)
}
