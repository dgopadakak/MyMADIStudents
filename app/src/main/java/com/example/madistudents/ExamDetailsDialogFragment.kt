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

    private lateinit var textViewExamNameTitle: TextView
    private lateinit var textViewExamName: TextView
    private lateinit var textViewTeacherNameTitle: TextView
    private lateinit var textViewTeacherName: TextView
    private lateinit var textViewAuditoryTitle: TextView
    private lateinit var textViewAuditory: TextView
    private lateinit var textViewDateTitle: TextView
    private lateinit var textViewDate: TextView
    private lateinit var textViewTimeTitle: TextView
    private lateinit var textViewTime: TextView
    private lateinit var textViewPeopleTitle: TextView
    private lateinit var textViewPeople: TextView
    private lateinit var textViewAbstractTitle: TextView
    private lateinit var textViewAbstract: TextView
    private lateinit var textViewCommentTitle: TextView
    private lateinit var textViewComment: TextView
    private lateinit var buttonDel: Button
    private lateinit var buttonEdit: Button
    private lateinit var buttonCancel: Button
    private lateinit var buttonOk: Button
    private lateinit var textViewCurrSort: TextView

    private var currentIdForSort: Int = -1

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        val view: View = inflater!!.inflate(R.layout.exam_details, container, false)
        textViewExamNameTitle = view.findViewById(R.id.textViewExamNameTitle)
        textViewExamName = view.findViewById(R.id.textViewExamName)
        textViewTeacherNameTitle = view.findViewById(R.id.textViewTeacherNameTitle)
        textViewTeacherName = view.findViewById(R.id.textViewTeacherName)
        textViewAuditoryTitle = view.findViewById(R.id.textViewAuditoryTitle)
        textViewAuditory = view.findViewById(R.id.textViewAuditory)
        textViewDateTitle = view.findViewById(R.id.textViewDateTitle)
        textViewDate = view.findViewById(R.id.textViewDate)
        textViewTimeTitle = view.findViewById(R.id.textViewTimeTitle)
        textViewTime = view.findViewById(R.id.textViewTime)
        textViewPeopleTitle = view.findViewById(R.id.textViewPeopleTitle)
        textViewPeople = view.findViewById(R.id.textViewPeople)
        textViewAbstractTitle = view.findViewById(R.id.textViewAbstractTitle)
        textViewAbstract = view.findViewById(R.id.textViewAbstract)
        textViewCommentTitle = view.findViewById(R.id.textViewCommentTitle)
        textViewComment = view.findViewById(R.id.textViewComment)
        buttonDel = view.findViewById(R.id.button_details_delete)
        buttonEdit = view.findViewById(R.id.button_details_edit)
        buttonCancel = view.findViewById(R.id.button_cancel)
        buttonOk = view.findViewById(R.id.button_details_ok)
        textViewCurrSort = view.findViewById(R.id.textViewCurrentSort)

        textViewExamNameTitle.setOnLongClickListener { setSortId(0) }
        textViewExamName.setOnLongClickListener { setSortId(0) }
        textViewTeacherNameTitle.setOnLongClickListener { setSortId(1) }
        textViewTeacherName.setOnLongClickListener { setSortId(1) }
        textViewAuditoryTitle.setOnLongClickListener { setSortId(2) }
        textViewAuditory.setOnLongClickListener { setSortId(2) }
        textViewDateTitle.setOnLongClickListener { setSortId(3) }
        textViewDate.setOnLongClickListener { setSortId(3) }
        textViewTimeTitle.setOnLongClickListener { setSortId(4) }
        textViewTime.setOnLongClickListener { setSortId(4) }
        textViewPeopleTitle.setOnLongClickListener { setSortId(5) }
        textViewPeople.setOnLongClickListener { setSortId(5) }
        textViewAbstractTitle.setOnLongClickListener { setSortId(6) }
        textViewAbstract.setOnLongClickListener { setSortId(6) }
        textViewCommentTitle.setOnLongClickListener { setSortId(7) }
        textViewComment.setOnLongClickListener { setSortId(7) }

        buttonDel.setOnClickListener { returnDel() }
        buttonEdit.setOnClickListener { returnEdit() }
        buttonCancel.setOnClickListener { dialog.dismiss() }
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
        if (arguments.getString("connection") != "1")
        {
            buttonDel.isEnabled = false
            buttonEdit.isEnabled = false
        }

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
        if (currentIdForSort == 0)
        {
            textViewCurrSort.text = "Сортировка по предмету"
        }
        else if (currentIdForSort == 1)
        {
            textViewCurrSort.text = "Сортировка по ФИО"
        }
        else if (currentIdForSort == 2)
        {
            textViewCurrSort.text = "Сортировка по аудитории"
        }
        else if (currentIdForSort == 3 || currentIdForSort == 4)
        {
            textViewCurrSort.text = "Сортировка по дате и времени"
        }
        else if (currentIdForSort == 5)
        {
            textViewCurrSort.text = "Сортировка по количеству"
        }
        else if (currentIdForSort == 6)
        {
            textViewCurrSort.text = "Сортировка по лекциям"
        }
        else if (currentIdForSort == 7)
        {
            textViewCurrSort.text = "Сортировка по комментарию"
        }
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(200
            , VibrationEffect.DEFAULT_AMPLITUDE))
        return true
    }

    private fun returnIdForSort()
    {
        onInputListenerSortId.sendInputSortId(currentIdForSort)
        dialog.dismiss()
    }

    private fun returnDel()
    {
        currentIdForSort = 8
        returnIdForSort()
    }

    private fun returnEdit()
    {
        currentIdForSort = 9
        returnIdForSort()
    }
}