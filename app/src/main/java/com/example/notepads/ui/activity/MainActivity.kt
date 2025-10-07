package com.example.notepads.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.SearchView
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notepads.utils.MyContextWrapper
import com.example.notepads.R
import com.example.notepads.adapter.NotesAdapter
import com.example.notepads.data.local.db.DBHelper
import com.example.notepads.data.local.db.dao.NotesDao
import com.example.notepads.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: NotesDao
    private lateinit var adapter: NotesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycler()

        setSupportActionBar(binding.toolbar)
        title=""


        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {

                //از داخل آداپتر میخواهیم از فانکشن گت فیلتر استفاده کنی ولی لون در حققیقت کد جاوا که اوراید شده برای همین ما از متغیر فیلتر استفاده میکنیم
                adapter.filter.filter(newText)
                return false
            }
        })

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open,
            R.string.close
        )
        toggle.isDrawerIndicatorEnabled = true
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.new_note -> {
                    val intent = Intent(this, AddNotesActivity::class.java)
                    intent.putExtra("key",true)

                    startActivity(intent)
                    binding.drawerLayout.closeDrawers()

                    true
                }

                R.id.deleted -> {
                    val intent = Intent(this, RecycleBinActivity::class.java)
                    startActivity(intent)
                    binding.drawerLayout.closeDrawers()

                    true
                }

                R.id.change_language -> {
                    binding.drawerLayout.closeDrawers()
                    showChangeLang()


                    true
                }

                R.id.checked -> {
                    val  intent=Intent(this,DoneActivity::class.java)
                    startActivity(intent)
                    binding.drawerLayout.closeDrawers()

                    true
                }


                else -> false
            }
        }

    }

    // همینطور که میدونی  on start وقتی که بک بزنیم یا برگردیم به این  اکتیویتی اجرا میشه برای همین با کدی که درونش نوشتیم باعث میشه ریسایکلر ویو آیتم ها رفرش بشه و تغییر بکنه اگه تغییری صورت گرفته باشه
    override fun onStart() {
        super.onStart()
         val data =dao.getNotesForRecycler(DBHelper.FALSE_STATE, DBHelper.FALSE_STATE)
        adapter.changeData(data)


    }

    /*   ۳ فانکشن پایین برای  تغییر زبان ساخته شده    */

    private fun showChangeLang() {

        val listItem = arrayOf("farsi", "english")

        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle(resources.getString(R.string.change_language))
        mBuilder.setIcon(R.drawable.ic_language)


        mBuilder.setSingleChoiceItems(listItem, -1) { dialog, which ->


            if (which == 0) {

                setLocate("fa")

                recreate()
            } else if (which == 1) {
                setLocate("en")
                recreate()
            }
            dialog.dismiss()
        }

        val mDialog = mBuilder.create()
        mDialog.show()

    }

    private fun setLocate(lang: String) {

        MyContextWrapper.wrap(this, lang)
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)

        val editor = getSharedPreferences("language", MODE_PRIVATE).edit()
        editor.putString("lang", lang)
        editor.apply()

    }

    private fun loadLocate() {

        val pref = getSharedPreferences("language", MODE_PRIVATE)
        val lang = pref.getString("lang", "") ?: "en"
        setLocate(lang)

    }



    private fun initRecycler() {
         dao = NotesDao(DBHelper(this))
        binding.recyclerNotes.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        adapter=NotesAdapter(this, dao)

        binding.recyclerNotes.adapter =adapter
    }


}