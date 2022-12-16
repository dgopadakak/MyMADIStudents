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

    fun getExam(indexGroup: Int, indexExam: Int): Exam
    {
        return groups[indexGroup].listOfExams[indexExam]
    }

    fun sortExams(indexGroup: Int, sortIndex: Int)
    {
        if (sortIndex == 0)
        {
            val tempArrayListOfExamsNames: ArrayList<String> = ArrayList()
            val tempArrayListOfExams: ArrayList<Exam> = ArrayList()
            for (i in groups[indexGroup].listOfExams)
            {
                tempArrayListOfExamsNames.add(i.nameOfExam)
            }
            tempArrayListOfExamsNames.sort()
            for (i in tempArrayListOfExamsNames)
            {
                for (j in groups[indexGroup].listOfExams)
                {
                    if (i == j.nameOfExam)
                    {
                        tempArrayListOfExams.add(j)
                    }
                }
            }
            groups[indexGroup].listOfExams = tempArrayListOfExams
        }
        if (sortIndex == 1)
        {
            val tempArrayListOfExamsNames: ArrayList<String> = ArrayList()
            val tempArrayListOfExams: ArrayList<Exam> = ArrayList()
        }
    }
}