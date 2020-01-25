package pro.dnomads.cats.data.room

import androidx.paging.DataSource
import androidx.room.*
import pro.dnomads.cats.data.model.ArticlesItem


@Dao
abstract class NewsDao {
    @Query("SELECT * FROM news")
    abstract fun getAll(): DataSource.Factory<Int, ArticlesItem>

    @Query("SELECT * FROM news LIMIT :limit OFFSET :offset")
    abstract fun getOffset(limit: Int, offset: Int): List<ArticlesItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(news: List<ArticlesItem>)

    @Delete
    protected abstract fun delete(tracks: ArticlesItem)

    @Update
    protected abstract fun updateTodo(tracks: List<ArticlesItem>)

    @Query("DELETE FROM news")
    protected abstract fun deleteAll()

    @Transaction
    open fun test(tracks: List<ArticlesItem>) {
        deleteAll()
        insertAll(tracks)
    }
}