package com.example.madistudents

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*

class EditExamActivity : AppCompatActivity()
{
    private lateinit var editExamName: EditText
    private lateinit var editTeacherName: EditText
    private lateinit var editAuditory: EditText
    private lateinit var editDate: EditText
    private lateinit var editTime: EditText
    private lateinit var editPeople: EditText
    private lateinit var editAbstract: EditText
    private lateinit var editComment: EditText

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_exam)

        editExamName = findViewById(R.id.editTextExamName)
        editTeacherName = findViewById(R.id.editTextTeacherName)
        editAuditory = findViewById(R.id.editTextAuditory)
        editDate = findViewById(R.id.editTextDate)
        editTime = findViewById(R.id.editTextTime)
        editPeople = findViewById(R.id.editTextPeople)
        editAbstract = findViewById(R.id.editTextAbstract)
        editComment = findViewById(R.id.editTextComment)

        val action = intent.getSerializableExtra("action") as Int

        findViewById<Button>(R.id.button_confirm).setOnClickListener { confirmChanges(action) }

        if (action == 2)
        {
            editExamName.setText(intent.getSerializableExtra("exam") as String)
            editTeacherName.setText(intent.getSerializableExtra("teacher") as String)
            editAuditory.setText(intent.getSerializableExtra("auditory") as String)
            editDate.setText(intent.getSerializableExtra("date") as String)
            editTime.setText(intent.getSerializableExtra("time") as String)
            editPeople.setText(intent.getSerializableExtra("people") as String)
            if (intent.getSerializableExtra("abstract") as String == "1")
            {
                editAbstract.setText("можно")
            }
            else
            {
                editAbstract.setText("нельзя")
            }
            editComment.setText(intent.getSerializableExtra("comment") as String)
        }
    }

    private fun confirmChanges(action: Int)
    {
        if (editExamName.text.toString() != "" && editTeacherName.text.toString() != ""
            && editAuditory.text.toString() != "" && editDate.text.toString() != ""
            && editTime.text.toString() != "" && editPeople.text.toString() != ""
            && editAbstract.text.toString() != "")
        {
            if (editAbstract.text.toString().trim().lowercase(Locale.ROOT) == "можно"
                || editAbstract.text.toString().trim().lowercase(Locale.ROOT) == "нельзя")
            {
                if (isDateValid(editDate.text.toString().trim())
                    && isTimeValid(editTime.text.toString().trim()))
                {
                    val intent = Intent(this@EditExamActivity,
                        MainActivity::class.java)
                    intent.putExtra("action", action)
                    intent.putExtra("exam", editExamName.text.toString().trim())
                    intent.putExtra("teacher", editTeacherName.text.toString().trim())
                    intent.putExtra("auditory", editAuditory.text.toString().trim().toInt())
                    intent.putExtra("date", editDate.text.toString().trim())
                    intent.putExtra("time", editTime.text.toString().trim())
                    intent.putExtra("people", editPeople.text.toString().trim().toInt())
                    if (editAbstract.text.trim() == "можно")
                    {
                        intent.putExtra("abstract", 1)
                    }
                    else
                    {
                        intent.putExtra("abstract", 0)
                    }
                    intent.putExtra("comment", editComment.text.toString().trim())
                    setResult(RESULT_OK, intent)
                    finish()
                }
                else
                {
                    Snackbar.make(findViewById(R.id.button_confirm),
                        "Проверьте дату и время!", Snackbar.LENGTH_LONG)
                        .setBackgroundTint(Color.RED)
                        .show()
                }
            }
            else
            {
                Snackbar.make(findViewById(R.id.button_confirm),
                    "Поле \"Пользоваться лекциями\" поддерживает только " +
                            "значения \"можно\" или \"нельзя\"!", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(Color.RED)
                    .show()
            }
        }
        else
        {
            Snackbar.make(findViewById(R.id.button_confirm),
                "Заполните обязательные поля!", Snackbar.LENGTH_LONG)
                .setBackgroundTint(Color.RED)
                .show()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun isDateValid(date: String?): Boolean
    {
        val myFormat = SimpleDateFormat("dd.MM.yyyy")
        myFormat.isLenient = false
        return try
        {
            if (date != null)
            {
                myFormat.parse(date)
            }
            true
        }
        catch (e: Exception)
        {
            false
        }
    }

    private fun isTimeValid(date: String?): Boolean
    {
        return try
        {
            LocalTime.parse(date)
            true
        }
        catch (e: java.lang.Exception)
        {
            false
        }
    }
}