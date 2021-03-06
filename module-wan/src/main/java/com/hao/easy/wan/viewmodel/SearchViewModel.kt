package com.hao.easy.wan.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.hao.easy.wan.db.Db
import com.hao.easy.wan.model.HotWord
import com.hao.easy.wan.repository.Api
import com.hao.library.http.subscribeBy
import com.hao.library.utils.CoroutineUtils
import com.hao.library.viewmodel.BaseViewModel

class SearchViewModel : BaseViewModel() {

    val hotWordLiveData = MutableLiveData<ArrayList<HotWord>>()

    fun getHotWords() {
        Api.getHotWords().subscribeBy({
            hotWordLiveData.value = it
        }).add()
    }

    fun insert(content: String?) {
        CoroutineUtils.io {
            val dao = Db.instance().historyDao()
            if (!TextUtils.isEmpty(content)) {
                dao.deleteByName(content!!)
                dao.insert(HotWord(null, content))
            }
        }
    }

    fun deleteAll() {
        CoroutineUtils.io {
            Db.instance().historyDao().deleteAll()
        }
    }
}