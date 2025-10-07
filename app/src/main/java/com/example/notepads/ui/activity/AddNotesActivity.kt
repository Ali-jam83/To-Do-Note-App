package com.example.notepads.ui.activity

import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notepads.R
import com.example.notepads.databinding.ActivityAddNotesBinding
import com.example.notepads.data.local.db.DBHelper
import com.example.notepads.data.local.db.dao.NotesDao
import com.example.notepads.data.model.DBNotesModel
import ir.amozeshgam.persiandate.PersianDate

class AddNotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dao = NotesDao(DBHelper(this))
/*  در اینجا key از دکمه نیو نوت گرفته میشه و id  از  روت ویو هولدر  */
        val key = intent .getBooleanExtra("key",false)
        val id = intent .getIntExtra("id",0)
         if(key){
             binding.date.text=getDate()
         }else{
             val note = dao.getNotesById(id)
             binding.date.text=note.date
             val edit = Editable.Factory()
             binding.edtTitle.text=edit.newEditable(note.title)
             binding.edtDescription.text=edit.newEditable(note.details)
         }

        binding.btnSave.setOnClickListener {
            val title = binding.edtTitle.text.toString()
            val description = binding.edtDescription.text.toString()
            if (title.isNotEmpty()) {
                val notes = DBNotesModel(0, DBHelper.FALSE_STATE, DBHelper.FALSE_STATE, title, getDate(), description)
                val result = if (key){dao.saveNotes(notes)}else{dao.editNotesById(id, notes )}
                if (result) {
                    finish()

                    toast(resources.getString(R.string.saved))

                } else {
                    toast(resources.getString(R.string.save_note))
                }
            } else {
                toast(resources.getString(R.string.title_note))
            }
        }
        binding.btnCancel.setOnClickListener {
            finish()
        }


    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    /* این فانکشن به ما تاریخ دقیق و ساعت گوشی رو میده البته با استفاده از کلاس پرشین دیت */
    private fun getDate(): String {

        // با استفاده از کلاس PersianDate تاریخ شمسی و ساعت کنونی را بدست می آوریم
        val date = PersianDate()

        // این کد تاریخ شمسی فعلی را به رشته تبدیل میکند
        val currentDate = "${date.year}/${date.month}/${date.day}"
        // این کد ساعت و دقیقه و ثانیه فعلی را به رشته تبدیل میکند
        val currentTime = "${date.hour}:${date.min}:${date.second}"

        // در اینجا ساعت و تاریخ را به هم چسبانده و return میکنیم
        return "$currentDate | $currentTime"

    }
}
