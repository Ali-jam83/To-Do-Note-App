package com.example.notepads.data.local.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(
    private val context: Context
) : SQLiteOpenHelper(context, NAME_DATABASE, null, VERSION_DATABASE) {

    companion object {

        private const val NAME_DATABASE = "notepads.db"
        private const val VERSION_DATABASE = 1

        const val NOTES_TABLE = "notes"
        const val NOTES_ID = "id"
        const val NOTES_DELETE_STATE = "deleteState"
        const val NOTES_CHECK_STATE = "CheckState"
        const val NOTES_TITLE = "title"
        const val NOTES_DATE = "date"
        const val NOTES_DETAILS = "details"


        const val TRUE_STATE = 1
        const val FALSE_STATE = 0


    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS $NOTES_TABLE (" +
                    "$NOTES_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$NOTES_DELETE_STATE INTEGER," +
                    "$NOTES_CHECK_STATE INTEGER," +
                    "$NOTES_TITLE VARCHAR(255)," +
                    "$NOTES_DATE VARCHAR(255), " +
                    "$NOTES_DETAILS TEXT )")


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
}