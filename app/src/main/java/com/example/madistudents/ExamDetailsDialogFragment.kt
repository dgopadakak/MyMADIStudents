package com.example.madistudents

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class ExamDetailsDialogFragment: android.app.DialogFragment()
{
    private val exceptionTag = "ExamDetailsDialogFragment"

    interface OnInputListenerSortId
    {
        fun sendInputSortId(sortId: Int)
    }

    lateinit var onInputListenerSortId: OnInputListenerSortId

    private lateinit var textViewExamName: TextView
    private lateinit var textViewTeacherName: TextView
    private lateinit var textViewAuditory: TextView
    private lateinit var textViewDate: TextView
    private lateinit var textViewTime: TextView
    private lateinit var textViewPeople: TextView
    private lateinit var textViewAbstract: TextView
    private lateinit var textViewComment: TextView
    private lateinit var buttonOk: Button

    private var currentIdForSort: Int = -1

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        val view: View = inflater!!.inflate(R.layout.exam_details, container, false)
        textViewExamName = view.findViewById(R.id.textViewExamName)
        textViewTeacherName = view.findViewById(R.id.textViewTeacherName)
        textViewAuditory = view.findViewById(R.id.textViewAuditory)
        textViewDate = view.findViewById(R.id.textViewDate)
        textViewTime = view.findViewById(R.id.textViewTime)
        textViewPeople = view.findViewById(R.id.textViewPeople)
        textViewAbstract = view.findViewById(R.id.textViewAbstract)
        textViewComment = view.findViewById(R.id.textViewComment)
        buttonOk = view.findViewById(R.id.button_details_ok)

        textViewExamName.setOnLongClickListener { setSortId(0) }
        textViewTeacherName.setOnLongClickListener { setSortId(1) }
        textViewAuditory.setOnLongClickListener { setSortId(2) }
        textViewDate.setOnLongClickListener { setSortId(3) }
        textViewTime.setOnLongClickListener { setSortId(4) }
        textViewPeople.setOnLongClickListener { setSortId(5) }
        textViewAbstract.setOnLongClickListener { setSortId(6) }
        textViewComment.setOnLongClickListener { setSortId(7) }

        buttonOk.setOnClickListener { returnIdForSort() }

        val arguments: Bundle = getArguments()
        textViewExamName.text = arguments.getString("examName")
        textViewTeacherName.text = arguments.getString("teacherName")
        textViewAuditory.text = arguments.getString("auditory")
        textViewDate.text = arguments.getString("date")
        textViewTime.text = arguments.getString("time")
        textViewPeople.text = arguments.getString("people")
        if (arguments.getString("abstract") == "1")
        {
            textViewAbstract.text = "можно"
        }
        else
        {
            textViewAbstract.text = "нельзя"
        }
        textViewComment.text = arguments.getString("comment")

        return view
    }

    override fun onAttach(activity: Activity?)
    {
        super.onAttach(activity)
        try {
            onInputListenerSortId = getActivity() as OnInputListenerSortId
        }
        catch (e: ClassCastException)
        {
            Log.e(exceptionTag, "onAttach: ClassCastException: " + e.message)
        }
    }

    private fun setSortId(id: Int): Boolean
    {
        currentIdForSort = id
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        return true
    }

    private fun returnIdForSort()
    {
        onInputListenerSortId.sendInputSortId(currentIdForSort)
        dialog.dismiss()
    }
}