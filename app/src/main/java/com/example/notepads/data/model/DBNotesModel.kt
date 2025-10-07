package com.example.notepads.data.model

data class DBNotesModel(
    var id:Int,
    var deleteState:Int,
    var checkState:Int,
    var title:String,
    var date:String,
    var details : String
)
