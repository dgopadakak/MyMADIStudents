package com.example.madistudents.ui.faculty.dbWithRoom

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.madistudents.ui.faculty.GroupOperator

@Database(entities = [ GroupOperator::class ], version=1, exportSchema = false)
abstract class AppDatabase: RoomDatabase()
{
    public abstract fun groupOperatorDao(): GroupOperatorDao
}