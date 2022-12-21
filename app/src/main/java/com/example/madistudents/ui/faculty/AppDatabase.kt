package com.example.madistudents.ui.faculty

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ GroupOperator::class ], version=1, exportSchema = false)
abstract class AppDatabase: RoomDatabase()
{
    public abstract fun groupOperatorDao(): GroupOperatorDao
}