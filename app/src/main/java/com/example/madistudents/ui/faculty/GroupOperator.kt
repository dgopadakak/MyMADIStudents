package com.example.madistudents.ui.faculty

class GroupOperator()
{
    private var groups: ArrayList<Group> = ArrayList()

    fun getGroups(): ArrayList<Group>
    {
        return groups
    }

    fun setGroups(newGroups: ArrayList<Group>)
    {
        groups = newGroups
    }

    fun getExamsNames(indexGroup: Int): ArrayList<String>
    {
        val arrayListForReturn: ArrayList<String> = ArrayList()
        for (i in groups[indexGroup].listOfExams)
        {
            arrayListForReturn.add(i.nameOfExam)
        }
        return arrayListForReturn
    }

    fun getTeachersNames(indexGroup: Int): ArrayList<String>
    {
        val arrayListForReturn: ArrayList<String> = ArrayList()
        for (i in groups[indexGroup].listOfExams)
        {
            arrayListForReturn.add(i.nameOfTeacher)
        }
        return arrayListForReturn
    }
}