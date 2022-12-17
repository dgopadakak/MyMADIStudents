package com.example.madistudents

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MyDialogFragmentDelExam: DialogFragment()
{
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("Вы уверены?")
            .setTitle("Внимание!")
            .setPositiveButton("OK"
            ) { _, _ -> (activity as MainActivity?)?.delExam() }
            .setNegativeButton("Отмена") { _, _ -> }
        return builder.create()
    }
}