package com.example.madistudents.ui.faculty

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.madistudents.ui.faculty.dbWithRoom.GroupOperatorConverter
import java.util.*

@Entity
class GroupOperator()
{
    @PrimaryKey
    private var id: Int = 1

    @TypeConverters(GroupOperatorConverter::class)
    private var groups: ArrayList<Group> = ArrayList()

    fun getGroups(): ArrayList<Group>
    {
        return groups
    }

    fun setGroups(newGroups: ArrayList<Group>)
    {
        groups = newGroups
    }

    fun setId(id: Int)
    {
        this.id = id
    }

    fun getId(): Int
    {
        return id
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
                tempArrayListOfExamsNames.add(i.nameOfExam.lowercase(Locale.ROOT))
            }
            tempArrayListOfExamsNames.sort()
            for (i in tempArrayListOfExamsNames)
            {
                for (j in groups[indexGroup].listOfExams)
                {
                    if (i == j.nameOfExam.lowercase(Locale.ROOT)
                        && !tempArrayListOfExams.contains(j))
                    {
                        tempArrayListOfExams.add(j)
                        break
                    }
                }
            }
            groups[indexGroup].listOfExams = tempArrayListOfExams
        }

        if (sortIndex == 1)
        {
            val tempArrayListOfTeacherNames: ArrayList<String> = ArrayList()
            val tempArrayListOfExams: ArrayList<Exam> = ArrayList()
            for (i in groups[indexGroup].listOfExams)
            {
                tempArrayListOfTeacherNames.add(i.nameOfTeacher.lowercase(Locale.ROOT))
            }
            tempArrayListOfTeacherNames.sort()
            for (i in tempArrayListOfTeacherNames)
            {
                for (j in groups[indexGroup].listOfExams)
                {
                    if (i == j.nameOfTeacher.lowercase(Locale.ROOT)
                        && !tempArrayListOfExams.contains(j))
                    {
                        tempArrayListOfExams.add(j)
                        break
                    }
                }
            }
            groups[indexGroup].listOfExams = tempArrayListOfExams
        }

        if (sortIndex == 2)
        {
            val tempArrayListOfAuditory: ArrayList<Int> = ArrayList()
            val tempArrayListOfExams: ArrayList<Exam> = ArrayList()
            for (i in groups[indexGroup].listOfExams)
            {
                tempArrayListOfAuditory.add(i.auditory)
            }
            tempArrayListOfAuditory.sort()
            for (i in tempArrayListOfAuditory)
            {
                for (j in groups[indexGroup].listOfExams)
                {
                    if (i == j.auditory && !tempArrayListOfExams.contains(j))
                    {
                        tempArrayListOfExams.add(j)
                        break
                    }
                }
            }
            groups[indexGroup].listOfExams = tempArrayListOfExams
        }

        if (sortIndex == 3 || sortIndex == 4)
        {
            val tempArrayListOfTimeAndDate: ArrayList<GregorianCalendar> = ArrayList()
            val tempArrayListOfExams: ArrayList<Exam> = ArrayList()
            for (i in groups[indexGroup].listOfExams)
            {
                val d: List<String> = i.date.split(".")
                val t: List<String> = i.time.split(":")
                tempArrayListOfTimeAndDate.add(GregorianCalendar(d[2].toInt(), d[1].toInt(),
                    d[0].toInt(), t[0].toInt(), t[1].toInt()))
            }
            tempArrayListOfTimeAndDate.sort()
            for (i in tempArrayListOfTimeAndDate)
            {
                for (j in groups[indexGroup].listOfExams)
                {
                    val d: List<String> = j.date.split(".")
                    val t: List<String> = j.time.split(":")
                    val tempGregorianCalendar = GregorianCalendar(d[2].toInt(), d[1].toInt(),
                        d[0].toInt(), t[0].toInt(), t[1].toInt())
                    if (tempGregorianCalendar == i && !tempArrayListOfExams.contains(j))
                    {
                        tempArrayListOfExams.add(j)
                        break
                    }
                }
            }
            groups[indexGroup].listOfExams = tempArrayListOfExams
        }

        if (sortIndex == 5)
        {
            val tempArrayListOfPeoples: ArrayList<Int> = ArrayList()
            val tempArrayListOfExams: ArrayList<Exam> = ArrayList()
            for (i in groups[indexGroup].listOfExams)
            {
                tempArrayListOfPeoples.add(i.peopleInAuditory)
            }
            tempArrayListOfPeoples.sort()
            for (i in tempArrayListOfPeoples)
            {
                for (j in groups[indexGroup].listOfExams)
                {
                    if (i == j.peopleInAuditory && !tempArrayListOfExams.contains(j))
                    {
                        tempArrayListOfExams.add(j)
                        break
                    }
                }
            }
            groups[indexGroup].listOfExams = tempArrayListOfExams
        }

        if (sortIndex == 6)
        {
            val tempArrayListOfAbstract: ArrayList<Int> = ArrayList()
            val tempArrayListOfExams: ArrayList<Exam> = ArrayList()
            for (i in groups[indexGroup].listOfExams)
            {
                tempArrayListOfAbstract.add(i.isAbstractAvailable)
            }
            tempArrayListOfAbstract.sort()
            for (i in tempArrayListOfAbstract)
            {
                for (j in groups[indexGroup].listOfExams)
                {
                    if (i == j.isAbstractAvailable && !tempArrayListOfExams.contains(j))
                    {
                        tempArrayListOfExams.add(j)
                        break
                    }
                }
            }
            groups[indexGroup].listOfExams = tempArrayListOfExams
        }

        if (sortIndex == 7)
        {
            val tempArrayListOfComment: ArrayList<String> = ArrayList()
            val tempArrayListOfExams: ArrayList<Exam> = ArrayList()
            for (i in groups[indexGroup].listOfExams)
            {
                tempArrayListOfComment.add(i.comment.lowercase(Locale.ROOT))
            }
            tempArrayListOfComment.sort()
            for (i in tempArrayListOfComment)
            {
                for (j in groups[indexGroup].listOfExams)
                {
                    if (i == j.comment.lowercase(Locale.ROOT)
                        && !tempArrayListOfExams.contains(j))
                    {
                        tempArrayListOfExams.add(j)
                        break
                    }
                }
            }
            groups[indexGroup].listOfExams = tempArrayListOfExams
        }
    }
}