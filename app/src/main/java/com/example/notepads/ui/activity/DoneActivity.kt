package com.example.notepads.ui.activity

//import androidx.appcompat.widget.SearchView
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notepads.adapter.DoneAdapter
import com.example.notepads.data.local.db.DBHelper
import com.example.notepads.data.local.db.dao.NotesDao
import com.example.notepads.databinding.ActivityDoneBinding

class DoneActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoneBinding
    private lateinit var adapter: DoneAdapter
    private lateinit var dao: NotesDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycler()
        binding.back.setOnClickListener { finish() }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //از داخل آداپتر میخواهیم از فانکشن گت فیلتر استفاده کنی ولی لون در حققیقت کد جاوا که اوراید شده برای همین ما از متغیر فیلتر استفاده میکنیم
                adapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun initRecycler() {
        dao = NotesDao(DBHelper(this))
        binding.recyclerNotes.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        adapter = DoneAdapter(this, dao)

        binding.recyclerNotes.adapter = adapter
    }
}
