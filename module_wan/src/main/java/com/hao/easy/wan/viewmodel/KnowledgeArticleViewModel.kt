package com.hao.easy.wan.viewmodel

import com.hao.easy.base.Config
import com.hao.easy.wan.model.Article
import com.hao.easy.wan.model.Author
import com.hao.easy.wan.repository.Api
import com.hao.library.http.subscribeBy
import kotlin.properties.Delegates

class KnowledgeArticleViewModel : BaseArticleViewModel() {

    override fun pageSize() = 6

    var typeId: Int = Author.ID_HONGYANG

    private var refresh by Delegates.observable(Config.refresh) { _, old, new ->
        if (old != new) {
            refresh()
        }
    }

    override fun onResume() {
        super.onResume()
        refresh = Config.refresh
    }

    override fun loadData(page: Int, onResponse: (ArrayList<Article>?) -> Unit) {
        Api.getKnowledgeArticle(page - 1, typeId).subscribeBy({
            onResponse(it?.datas)
        }, {
            onResponse(null)
        }).add()
    }
}

