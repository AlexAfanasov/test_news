package pro.dnomads.cats.retrofitAPI

import io.reactivex.Single
import pro.dnomads.cats.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("everything")
    fun getNews(
        @Query("q") platform: String?,
        @Query("from") date: String?,
        @Query("sortBy") sort: String?,
        @Query("apiKey") key: String?,
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): Single<NewsResponse>

}