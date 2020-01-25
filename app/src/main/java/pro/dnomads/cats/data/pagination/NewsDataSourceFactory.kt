package pro.dnomads.cats.data.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import pro.dnomads.cats.data.model.ArticlesItem
import pro.dnomads.cats.data.room.NewsDao
import pro.dnomads.cats.retrofitAPI.NewsService
import javax.inject.Inject

class NewsDataSourceFactory @Inject constructor(
    private val compositeDisposable: CompositeDisposable,
    private val networkService: NewsService,
    private val db: NewsDao
) : DataSource.Factory<Int, ArticlesItem>() {

    val newsDataSourceLiveData = MutableLiveData<NewsDataSource>()

    override fun create(): DataSource<Int, ArticlesItem> {
        val newsDataSource = NewsDataSource(networkService, compositeDisposable, db)
        newsDataSourceLiveData.postValue(newsDataSource)
        return newsDataSource
    }
}