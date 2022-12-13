package com.example.madistudents.ui.faculty

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DbHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version)
{
    private val table1Name = "GROUPS"
    private val col11 = "id"
    private val col12 = "name"
    private val createTable1 = "CREATE TABLE IF NOT EXISTS $table1Name " +
            "($col11 INTEGER PRIMARY KEY AUTOINCREMENT, $col12 TEXT);"
    private val dropTable1 = "DROP TABLE IF EXISTS $table1Name"

    private val table2Name = "EXAMS"
    private val col21 = "id"
    private val col22 = "group_id"
    private val col23 = "exam_name"
    private val col24 = "teacher_name"
    private val col25 = "auditory"
    private val col26 = "date"
    private val col27 = "time"
    private val col28 = "people"
    private val col29 = "abstract"
    private val col210 = "comment"
    private val createTable2 = "CREATE TABLE IF NOT EXISTS $table2Name " +
            "($col21 INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$col22 INTEGER, $col23 TEXT, $col24 TEXT, $col25 INTEGER, $col26 TEXT, $col27 TEXT," +
            "$col28 INTEGER, $col29 INTEGER, $col210 TEXT);"
    private val dropTable2 = "DROP TABLE IF EXISTS $table2Name"

    override fun onCreate(db: SQLiteDatabase?)
    {
        db?.execSQL(createTable1)
        db?.execSQL(createTable2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)
    {
        db?.execSQL(dropTable1)
        db?.execSQL(dropTable2)
        onCreate(db)
    }

    fun insertGroup(group: Group): Int
    {
        var forReturn = 0
        val db = this.writableDatabase
        val cv1 = ContentValues()
        cv1.put(col12, group.name)
        var result = db.insert(table1Name, null, cv1)
        if (result != -1L)
        {
            forReturn = 1000
        }
        val cursor: Cursor = db.query(table1Name, arrayOf(col11), "$col12 = ${group.name}",
            null, null, null, null)
        cursor.moveToFirst()
        val index = cursor.getColumnIndex(col11)
        var groupId: Int = -1
        if (index >= 0)
        {
            groupId = cursor.getInt(index)
        }
        cursor.close()
        for (i in group.listOfExams)
        {
            val cv2 = ContentValues()
            cv2.put(col22, groupId)
            cv2.put(col23, i.nameOfExam)
            cv2.put(col24, i.nameOfTeacher)
            cv2.put(col25, i.auditory)
            cv2.put(col26, i.date)
            cv2.put(col27, i.time)
            cv2.put(col28, i.peopleInAuditory)
            cv2.put(col29, i.isAbstractAvailable)
            cv2.put(col210, i.comment)
            result = db.insert(table2Name, null, cv2)
            if (result != -1L)
            {
                forReturn++
            }
        }
        return forReturn
    }
}