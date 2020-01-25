package pro.dnomads.cats.data.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import pro.dnomads.cats.State
import pro.dnomads.cats.data.model.ArticlesItem
import pro.dnomads.cats.data.room.NewsDao
import pro.dnomads.cats.retrofitAPI.NewsService
import javax.inject.Inject

class NewsDataSource @Inject constructor(
    private val networkService: NewsService,
    private val compositeDisposable: CompositeDisposable,
    private val db: NewsDao
) : PageKeyedDataSource<Int, ArticlesItem>() {

    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ArticlesItem>
    ) {
        updateState(State.RUNNING)
        compositeDisposable.add(

            networkService.getNews(
                platform = "android",
                date = "2019-04-00",
                sort = "publishedAt",
                key = "dfb4f5e7a3e345e384cde4e09bc72367",
                page = 1,
                size = params.requestedLoadSize / 2
            )
                .subscribe(
                    { response ->
                        updateState(State.SUCCESS)
                        db.insertAll(response.articles)
                        callback.onResult(
                            response.articles,
                            null,
                            2
                        )

                    },
                    {
                        //Load first page from db
                        callback.onResult(
                            db.getOffset(params.requestedLoadSize / 2, 0),
                            null,
                            2
                        )
                        updateState(State.FAILED)
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ArticlesItem>) {
        updateState(State.RUNNING)
        compositeDisposable.add(
            networkService.getNews(

                platform = "android",
                date = "2019-04-00",
                sort = "publishedAt",
                key = "dfb4f5e7a3e345e384cde4e09bc72367",
                page = params.key,
                size = params.requestedLoadSize
            )
                .subscribe(
                    { response ->
                        updateState(State.SUCCESS)
                        db.insertAll(response.articles)
                        callback.onResult(
                            response.articles,
                            params.key + 1
                        )
                    },
                    {
                        updateState(State.FAILED)
                        setRetry(Action { loadAfter(params, callback) })
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ArticlesItem>) {
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    fun retry() {
        retryCompletable?.let {
            compositeDisposable.add(
                it.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

}