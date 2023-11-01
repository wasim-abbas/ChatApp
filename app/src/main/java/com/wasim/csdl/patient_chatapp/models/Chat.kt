package com.wasim.csdl.patient_chatapp.models

class Chat {

    private var sender: String = ""
    private var message: String = ""
    private var receiver: String = ""
    private var isSeen = false
    private var url: String = ""
    private var messageID: String = ""

    constructor()

    constructor(
        sender: String,
        message: String,
        receiver: String,
        isSeen: Boolean,
        url: String,
        messageID: String
    ) {
        this.sender = sender
        this.message = message
        this.receiver = receiver
        this.isSeen = isSeen
        this.url = url
        this.messageID = messageID
    }

    fun getSender(): String? {
        return sender
    }

    fun setSender(sender: String) {
        this.sender = sender
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun getReceiver(): String? {
        return receiver
    }

    fun setReceiver(receiver: String) {
        this.receiver = receiver
    }

    fun getIsSeen(): Boolean? {
        return isSeen
    }

    fun setIsSeen(isSeen: Boolean) {
        this.isSeen = isSeen
    }

    fun getUrl(): String? {
        return url
    }

    fun setUrl(url: String) {
        this.url = url
    }

    fun getMessageID(): String? {
        return messageID
    }

    fun setMessageID(messageID: String) {
        this.messageID = messageID
    }

}