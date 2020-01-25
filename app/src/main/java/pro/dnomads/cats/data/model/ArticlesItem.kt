package pro.dnomads.cats.data.model

import androidx.room.Entity
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "news",
    primaryKeys = ["publishedAt", "description"]
)
@TypeConverters(SourceConverter::class)
data class ArticlesItem(
    @field:SerializedName("publishedAt")
    val publishedAt: String,

    @field:SerializedName("author")
	val author: String? = null,

    @field:SerializedName("urlToImage")
	val urlToImage: String? = null,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("source")
	val source: Source? = null,

    @field:SerializedName("title")
	val title: String? = null,

    @field:SerializedName("url")
	val url: String = "",

    @field:SerializedName("content")
	val content: String? = null
)