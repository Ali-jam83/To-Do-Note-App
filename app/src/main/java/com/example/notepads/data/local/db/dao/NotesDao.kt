package com.example.notepads.data.local.db.dao

import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import com.example.notepads.data.model.RecyclerNotesModel
import com.example.notepads.data.local.db.DBHelper
import com.example.notepads.data.model.DBNotesModel

class NotesDao(
    private val db: DBHelper
) {
    private val contentValues = ContentValues()
    private lateinit var cursor: Cursor

    fun saveNotes(notes: DBNotesModel): Boolean {
        val database = db.writableDatabase
        contentValues(notes)
        val result = database.insert(DBHelper.NOTES_TABLE, null, contentValues)
        database.close()
        return result > 0

    }

    fun getNotesForRecycler(
        valuesDelete: Int,
        valuesCheck: Int
    ): ArrayList<RecyclerNotesModel> {
        val database = db.readableDatabase
        val query = "SELECT ${DBHelper.NOTES_ID},${DBHelper.NOTES_TITLE} " +
                "FROM ${DBHelper.NOTES_TABLE}"+
             " WHERE ${DBHelper.NOTES_DELETE_STATE} = ? AND ${DBHelper.NOTES_CHECK_STATE} = ?"
        cursor = database.rawQuery(query, arrayOf(valuesDelete.toString(), valuesCheck.toString()))
        val data = getDataForRecycler()
        cursor.close()
        database.close()

        return data

    }
    fun getNotesById(
        id: Int,
    ): DBNotesModel {
        val database = db.readableDatabase
        val query = "SELECT * FROM ${DBHelper.NOTES_TABLE} WHERE ${DBHelper.NOTES_ID} = ? "
        cursor = database.rawQuery(query, arrayOf( id.toString()))
        val data = getData()
        cursor.close()
        database.close()

        return data

    }



    fun editState(id: String, state: Int,key : String): Boolean {

        val database = db.writableDatabase
        contentValues.clear()
        contentValues.put(key, state)
        val result = database.update(
            DBHelper.NOTES_TABLE,
            contentValues,
            "${DBHelper.NOTES_ID} = ? ",
            arrayOf(id)
        )
        database.close()


        return result > 0

    }

    fun deleteNotes(id: String): Boolean {

        val database = db.writableDatabase

        val result = database.delete(
            DBHelper.NOTES_TABLE,
            "${DBHelper.NOTES_ID} = ? ",
            arrayOf(id)
        )
        database.close()


        return result > 0

    }

    fun editNotesById(id: Int, notes: DBNotesModel):Boolean{
        val database = db.writableDatabase
        contentValues(notes)
        val result = database.update(
            DBHelper.NOTES_TABLE,
            contentValues,
            "${DBHelper.NOTES_ID} = ? ",
            arrayOf(id.toString()))
        database.close()
        return result>0
    }


    private fun getDataForRecycler(): ArrayList<RecyclerNotesModel> {
        val data = ArrayList<RecyclerNotesModel>()
        try {
            if (cursor.moveToFirst()) {
                do {
                    var id = cursor.getInt(getIndex(DBHelper.NOTES_ID))
                    var title = cursor.getString(getIndex(DBHelper.NOTES_TITLE))
                    data.add(RecyclerNotesModel(id, title))

                } while (cursor.moveToNext())
            }

        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
        }

        return data
    }

    private fun getData(): DBNotesModel {
        val data = DBNotesModel(0,0,0,"","","")
        try {
            if (cursor.moveToFirst()) {

                   data.id = cursor.getInt(getIndex(DBHelper.NOTES_ID))
                    data.title = cursor.getString(getIndex(DBHelper.NOTES_TITLE))
                    data.date = cursor.getString(getIndex(DBHelper.NOTES_DATE))
                    data.details= cursor.getString(getIndex(DBHelper.NOTES_DETAILS))
                    data.deleteState = cursor.getInt(getIndex(DBHelper.NOTES_DELETE_STATE))
                    data.checkState = cursor.getInt(getIndex(DBHelper.NOTES_CHECK_STATE))

            }

        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
        }

        return data
    }


    private fun getIndex(name: String) = cursor.getColumnIndex(name)

    private fun contentValues(notes: DBNotesModel) {

        contentValues.clear()
        contentValues.put(DBHelper.NOTES_DELETE_STATE, notes.deleteState)
        contentValues.put(DBHelper.NOTES_CHECK_STATE, notes.checkState)
        contentValues.put(DBHelper.NOTES_DETAILS, notes.details)
        contentValues.put(DBHelper.NOTES_TITLE, notes.title)
        contentValues.put(DBHelper.NOTES_DATE, notes.date)
    }
}