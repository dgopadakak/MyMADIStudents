package com.example.madistudents.ui.faculty

import android.content.Context

class GroupOperator(context: Context)
{
    private var groups: ArrayList<Group> = ArrayList()
    var dbh: DbHelper = DbHelper(context, "MyFirstDB", null, 1)
}