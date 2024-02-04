package com.example.passwordmanager.Data_Class

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class BankCardData(
    @DocumentId
    var id : String? = null,
    @PropertyName("Name")
    val name: String,
    @PropertyName("Card_Number")
    val cardNumber: String,
    @PropertyName("Card_Type")
    val cardType: String,
    @PropertyName("Card_Holder")
    val cardHolderName:String,
    @PropertyName("Valid_Through")
    val validThrough:String,
    @PropertyName("CVC")
    val cvc: String,
    @PropertyName("PIN")
    val pin: String,
    @PropertyName("Card_Issuer")
    val cardIssuer: String,
    @PropertyName("Customer_Service_Phone")
    val customerServicePhone: String,
    @PropertyName("Card_Color")
    val cardColor: String,
    @PropertyName("Comment")
    val comment: String
):Serializable
