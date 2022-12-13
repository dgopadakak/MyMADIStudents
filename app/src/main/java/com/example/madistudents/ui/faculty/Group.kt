package com.example.madistudents.ui.faculty

data class Group(
    val name: String,
    val listOfExams: ArrayList<Exam> = ArrayList()
)
