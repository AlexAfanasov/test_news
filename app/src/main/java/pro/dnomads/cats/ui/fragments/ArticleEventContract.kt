package pro.dnomads.cats.ui.fragments

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import pro.dnomads.cats.State
import pro.dnomads.cats.data.model.ArticlesItem
import pro.dnomads.cats.ui.base.BasePresenter
import pro.dnomads.cats.ui.base.BaseView

interface ArticleEventContract {
    interface Presenter : BasePresenter<View> {
        fun requestRetry()
        fun getArticles(): LiveData<PagedList<ArticlesItem>>
        fun getState(): LiveData<State>
        fun listIsEmpty(): Boolean
    }

    interface View : BaseView<Presenter>
}