package com.example.madistudents.ui.faculty

data class Exam(
    val nameOfExam: String,
    val nameOfTeacher: String,
    val auditory: Int,
    val date: String,
    val time: String,
    val peopleInAuditory: Int,
    val isAbstractAvailable: Int,
    val comment: String
)
