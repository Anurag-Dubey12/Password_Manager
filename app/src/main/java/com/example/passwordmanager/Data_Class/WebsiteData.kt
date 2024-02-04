package com.example.passwordmanager.Data_Class

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class WebsiteData(
    @DocumentId
    var id : String? = null,
    @PropertyName("Web_Address")
    val url: String,
    @PropertyName("Login_ID")
    val Login_ID: String,
    @PropertyName("Password")
    val Password: String,
    @PropertyName("Comment")
    val Comment: String=" ",
    @PropertyName("UID")
    val uid:String
):Serializable
