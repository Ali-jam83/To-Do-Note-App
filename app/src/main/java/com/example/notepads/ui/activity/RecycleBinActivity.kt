package com.example.notepads.ui.activity

import android.os.Bundle
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notepads.R
import com.example.notepads.adapter.DoneAdapter
import com.example.notepads.adapter.NotesAdapter
import com.example.notepads.adapter.RecycleBinAdapter
import com.example.notepads.data.local.db.DBHelper
import com.example.notepads.data.local.db.dao.NotesDao
import com.example.notepads.databinding.ActivityRecycleBinBinding

class RecycleBinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecycleBinBinding
     private lateinit var dao: NotesDao
     private lateinit var adapter: RecycleBinAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecycleBinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        title=""
        initRecycler()
        binding.back.setOnClickListener{

            finish()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }
    private fun initRecycler() {
         dao = NotesDao(DBHelper(this))
        binding.recyclerNotes.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

          adapter= RecycleBinAdapter(this, dao)

        binding.recyclerNotes.adapter =adapter
    }

}