package com.example.notepads.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notepads.R
import com.example.notepads.data.local.db.DBHelper
import com.example.notepads.data.local.db.dao.NotesDao
import com.example.notepads.data.model.RecyclerNotesModel
import com.example.notepads.databinding.ListItemRecycleBinBinding

class RecycleBinAdapter(
    private val context: Context,
    private val dao: NotesDao,


    ) : RecyclerView.Adapter<RecycleBinAdapter.RecycleViewHolder>(), Filterable {

    private var allItemNotes : ArrayList<RecyclerNotesModel>
    init {
        allItemNotes = dao.getNotesForRecycler(DBHelper.TRUE_STATE, DBHelper.FALSE_STATE)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        /*   ListItemNotesBinding.inflate(context.layoutInflater, parent, false)در اینجا بجای استفاده از */
        //از این روش استفاده کردیم چون باید برای استفاده از روش قبلی اکتیویتی رو میگرفتیم در کانتکست و ایمپورتش هم میکردیم و این سنگین بجای این کار از این روش استفاده می کنیم
        return RecycleViewHolder(
            ListItemRecycleBinBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun getItemCount(): Int = allItemNotes.size


    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.saveData(allItemNotes[position])
    }


    inner class RecycleViewHolder(
        private val binding: ListItemRecycleBinBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun saveData(data: RecyclerNotesModel) {
            binding.subjectNote.text = data.title

            binding.ImageViewDeleteNote.setOnClickListener {
                val dialog = AlertDialog.Builder(context)
                dialog.setTitle(context.resources.getString(R.string.deleteforever))
                dialog.setIcon(R.drawable.ic_delete)
                dialog.setMessage(R.string.deleteـtheـnoteـforever)
                dialog.setPositiveButton(R.string.Yes) { dia, _ ->
                    val dao = NotesDao(DBHelper(context))
                    val result = dao.deleteNotes(data.id.toString())
                    if (result) {
                        toast(context.resources.getString(R.string.deleteـnoteـforever))
                        allItemNotes.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)

                    } else {
                        toast(context.resources.getString(R.string.ـdont_deleteـnoteـforever))
                    }

                    dia.dismiss()
                }
                dialog.setNegativeButton(R.string.no) { dia, _ -> dia.dismiss() }
                dialog.create().show()
                }
                binding.ImageViewRestore.setOnClickListener {

                    dao.editState(
                        data.id.toString(),
                        DBHelper.FALSE_STATE,
                        DBHelper.NOTES_DELETE_STATE
                    )
                    allItemNotes.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                    toast(context.resources.getString(R.string.restore))

                }



        }



        private fun toast(text: String) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }


    }

    override fun getFilter(): Filter =
        object : Filter() {
            ///constraint همون تکستس که کاربر مینویسه
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterList = ArrayList<RecyclerNotesModel>()
                allItemNotes = dao.getNotesForRecycler(DBHelper.TRUE_STATE, DBHelper.FALSE_STATE)
                if (constraint.isNullOrEmpty()) {
                    filterList.addAll(allItemNotes)
                } else {
                    val filterPattern = constraint.toString().trim()
                    for (item in allItemNotes)
                        if (item.title.contains(filterPattern))
                            filterList.add(item)
                }
                val result = FilterResults()
                result.values = filterList
                return result
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHEKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                allItemNotes.clear()
                allItemNotes.addAll(results?.values as ArrayList<RecyclerNotesModel>)
                notifyDataSetChanged()
            }


        }
}
