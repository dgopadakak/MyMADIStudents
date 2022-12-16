package com.example.madistudents.ui.faculty

data class Group(
    val name: String,
    var listOfExams: ArrayList<Exam> = ArrayList()
)
