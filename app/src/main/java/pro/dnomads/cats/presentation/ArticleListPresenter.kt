package pro.dnomads.cats.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import moxy.MvpPresenter
import pro.dnomads.cats.PREFETCH_DISTANCE
import pro.dnomads.cats.State
import pro.dnomads.cats.data.model.ArticlesItem
import pro.dnomads.cats.data.pagination.NewsDataSource
import pro.dnomads.cats.data.pagination.NewsDataSourceFactory
import pro.dnomads.cats.ui.fragments.ArticleEventContract
import javax.inject.Inject

class ArticleListPresenter @Inject constructor(private val newsDataSourceFactory:NewsDataSourceFactory) :
    MvpPresenter<ArticleEventContract.View>() {
    private lateinit var articles: LiveData<PagedList<ArticlesItem>>


    fun requestRetry() {
        newsDataSourceFactory.newsDataSourceLiveData.value?.retry()
    }

    fun getState(): LiveData<State> = Transformations.switchMap<NewsDataSource,
            State>(newsDataSourceFactory.newsDataSourceLiveData, NewsDataSource::state)


    fun listIsEmpty(): Boolean {
        return articles.value?.isEmpty() ?: true
    }

    private val pageSize = 20
    fun getArticles(): LiveData<PagedList<ArticlesItem>> {
        if (!::articles.isInitialized) {

            val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(false)
                .build()
            articles = LivePagedListBuilder(newsDataSourceFactory, config).build()
        }
        return articles
    }
}