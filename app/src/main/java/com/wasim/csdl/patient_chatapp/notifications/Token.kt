package com.wasim.csdl.patient_chatapp.notifications


class Token {
    private var token: String = ""

    constructor()
    constructor(token: String) {
        this.token = token
    }

    fun getToken(): String? {
        return token
    }

    fun setToken(token: String?) {
        this.token = token!!
    }


}