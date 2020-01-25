package pro.dnomads.cats.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SourceConverter {
    @TypeConverter
    fun fromSource(hobbies: Source): String {
        val type = object: TypeToken<Source>() {}.type
        return Gson().toJson(hobbies, type)
    }

    @TypeConverter
    fun toSource(data: String): Source {
        val type = object : TypeToken<Source>() {}.type
        return Gson().fromJson(data, type)
    }
}