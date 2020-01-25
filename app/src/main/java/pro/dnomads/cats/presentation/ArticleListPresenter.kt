package pro.dnomads.cats.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import pro.dnomads.cats.PREFETCH_DISTANCE
import pro.dnomads.cats.State
import pro.dnomads.cats.data.model.ArticlesItem
import pro.dnomads.cats.data.pagination.NewsDataSource
import pro.dnomads.cats.data.pagination.NewsDataSourceFactory
import pro.dnomads.cats.data.room.NewsDao
import pro.dnomads.cats.retrofitAPI.NewsService
import pro.dnomads.cats.ui.fragments.ArticleEventContract
import javax.inject.Inject

class ArticleListPresenter @Inject constructor(db: NewsDao, networkService: NewsService) :
    ArticleEventContract.Presenter {
    private val compositeDisposable = CompositeDisposable()
    private var newsDataSourceFactory: NewsDataSourceFactory
    private lateinit var articles: LiveData<PagedList<ArticlesItem>>

    private lateinit var view: ArticleEventContract.View

    init {
        newsDataSourceFactory = NewsDataSourceFactory(compositeDisposable, networkService, db)
    }

    override fun subscribe(view: ArticleEventContract.View) {
        this.view = view
    }

    override fun unSubscribe() {
        compositeDisposable.clear()
    }

    override fun requestRetry() {
        newsDataSourceFactory.newsDataSourceLiveData.value?.retry()
    }

    override fun getState(): LiveData<State> = Transformations.switchMap<NewsDataSource,
            State>(newsDataSourceFactory.newsDataSourceLiveData, NewsDataSource::state)


    override fun listIsEmpty(): Boolean {
        return articles.value?.isEmpty() ?: true
    }

    private val pageSize = 20
    override fun getArticles(): LiveData<PagedList<ArticlesItem>> {
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