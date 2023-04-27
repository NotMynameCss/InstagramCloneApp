package edu.poly.instagramcloneapp.model


data class MessageModel(
    val message:String? = null,
    var senderId:String? = null,
    var receiverId:String? = null,
    var Image: String? = null,
    var timestamp: String? = null
)
