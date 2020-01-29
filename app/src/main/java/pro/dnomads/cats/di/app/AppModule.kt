package pro.dnomads.cats.di.app

import android.app.Application
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import pro.dnomads.cats.ENDPOINT
import pro.dnomads.cats.data.pagination.NewsDataSourceFactory
import pro.dnomads.cats.data.room.NewsDao
import pro.dnomads.cats.data.room.NewsDb
import pro.dnomads.cats.retrofitAPI.NewsService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {


    @Singleton
    @Provides
    fun provideFactory(db: NewsDao, newsService: NewsService, compositeDisposable: CompositeDisposable) =
        NewsDataSourceFactory(compositeDisposable, newsService, db)

    @Singleton
    @Provides
    fun provideDb(app: Application) = NewsDb.invoke(app)

    @Singleton
    @Provides
    fun provideComposite() = CompositeDisposable()

    @Singleton
    @Provides
    fun provideTracksDao(db: NewsDb): NewsDao {
        return db.newsDao()
    }

    @Singleton
    @Provides
    fun provideNewsService(): NewsService = getService()

    private fun getService(): NewsService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(NewsService::class.java)
    }


    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)


    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
}
