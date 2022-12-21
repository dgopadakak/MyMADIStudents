package com.example.madistudents.ui.faculty

import androidx.room.TypeConverter

class GroupOperatorConverter
{
    @TypeConverter
    fun fromGO(groups: ArrayList<Group>): String
    {
        val resultString: StringBuilder = java.lang.StringBuilder()
        for (i in groups)
        {
            resultString.append("###${i.name}")
            for (j in i.listOfExams)
            {
                resultString.append("##${j.nameOfExam}#${j.nameOfTeacher}#${j.peopleInAuditory}" +
                        "#${j.date}#${j.time}#${j.peopleInAuditory}" +
                        "#${j.isAbstractAvailable}#${j.comment}")
            }
        }
        return resultString.toString()
    }

    @TypeConverter
    fun toGO(data: String): ArrayList<Group>
    {
        val groupsForReturn: ArrayList<Group> = ArrayList()
        val groupsString: List<String> = data.substring(3).split("###")
        for (i in groupsString)
        {
            val partsOfGroup: List<String> = i.split("##")
            val tempExams: ArrayList<Exam> = ArrayList()
            for (j in 1 until partsOfGroup.size)
            {
                val partsOfExam: List<String> = partsOfGroup[j].split("#")
                val tempExam = Exam(partsOfExam[0], partsOfExam[1], partsOfExam[2].toInt(),
                    partsOfExam[3], partsOfExam[4], partsOfExam[5].toInt(),
                    partsOfExam[6].toInt(), partsOfExam[7])
                tempExams.add(tempExam)
            }
            groupsForReturn.add(Group(partsOfGroup[0], tempExams))
        }
        return groupsForReturn
    }
}