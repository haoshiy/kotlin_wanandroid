package com.hao.easy.wan.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hao.easy.wan.model.Ad
import com.hao.easy.wan.repository.Api
import com.hao.library.http.subscribeBy

class WechatViewModel : AuthorViewModel() {

    val adLiveData = MutableLiveData<ArrayList<Ad>>()

    fun initData() {
        Api.getAd().subscribeBy({
            if (it != null) {
                adLiveData.value = it
            }
        }).add()

        getAuthors()
    }

}