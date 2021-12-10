package com.codarch.teddybearkindergarten

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ParentControl : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "CommitPrefEdits")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_control)

        val studentName: TextView = findViewById<TextView>(R.id.studentName)
        val dateText: TextView = findViewById<TextView>(R.id.date)
        val situationText: TextView = findViewById<TextView>(R.id.situation)
        val preferences = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val checkButton = findViewById<ImageView>(R.id.checkButton)
        val xButton = findViewById<ImageView>(R.id.xButton)

        dateText.text = current.format(formatter)

        if (preferences.getInt(KEY_CHECK, 1) == 0) {
            situationText.text = "(Öğrenci okula GELMEYECEK olarak kayıtlı)"

        } else {
            situationText.text = "(Öğrenci okula GELECEK olarak kayıtlı)"

        }

        studentName.text = preferences.getString(KEY_NAME, "")

        try {
            val databaseCheck = this.openOrCreateDatabase("StudentsCheck", MODE_PRIVATE, null)
            databaseCheck.execSQL("CREATE TABLE IF NOT EXISTS studentsCheck (id INTEGER PRIMARY KEY, studentName VARCHAR, parentCheck INT, schoolCheck INT)")

            checkButton.setOnClickListener {

                editor.putInt(KEY_CHECK, 1)
                editor.apply()
                databaseCheck.execSQL(
                    "UPDATE studentsCheck SET parentCheck = 1 WHERE studentName = '${
                        preferences.getString(
                            KEY_NAME,
                            ""
                        )
                    }'"
                )
                Toast.makeText(
                    applicationContext,
                    "Gelecektir olarak ayarlandı",
                    Toast.LENGTH_SHORT
                )
                    .show()
                situationText.text = "(Öğrenci okula GELECEK olarak kayıtlı)"


            }
            xButton.setOnClickListener {

                editor.putInt(KEY_CHECK, 0)
                editor.apply()
                databaseCheck.execSQL(
                    "UPDATE studentsCheck SET parentCheck = 0 WHERE studentName = '${
                        preferences.getString(
                            KEY_NAME,
                            ""
                        )
                    }'"
                )
                Toast.makeText(
                    applicationContext,
                    "Gelmeyecektir olarak ayarlandı",
                    Toast.LENGTH_SHORT
                )
                    .show()
                situationText.text = "(Öğrenci okula GELMEYECEK olarak kayıtlı)"

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}