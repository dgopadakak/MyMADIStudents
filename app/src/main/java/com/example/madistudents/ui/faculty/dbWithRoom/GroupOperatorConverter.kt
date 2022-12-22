package com.example.madistudents.ui.faculty.dbWithRoom

import androidx.room.TypeConverter
import com.example.madistudents.ui.faculty.Group
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class GroupOperatorConverter
{
    @TypeConverter
    fun fromGO(groups: ArrayList<Group>): String
    {
        val gsonBuilder = GsonBuilder()
        val gson: Gson = gsonBuilder.create()
        return gson.toJson(groups)
    }

    @TypeConverter
    fun toGO(data: String): ArrayList<Group>
    {
        val gsonBuilder = GsonBuilder()
        val gson: Gson = gsonBuilder.create()
        return try {
            val type: Type = object : TypeToken<ArrayList<Group>>() {}.type
            gson.fromJson(data, type)
        } catch (e: Exception) {
            ArrayList()
        }
    }
}