package com.wasim.csdl.patient_chatapp.utils.source

import io.paperdb.Paper

const val ACCESS_TOEKN = "ACCESS_TOEKN"
const val USER = "USER"
const val REGISTER_USER = "REGISTER_USER"
const val FIRST_TIME = "FIRST_TIME"
const val FIREBASE_TOKEN = "firebase_token"
const val FCM = "fcm"
const val DEVICE_NAME = "DEVICE_NAME"
const val LANGUAGE = "LANGUAGE"
const val STEPS = "STEPS"
const val SECONDS = "SECONDS"
const val HOURS = "HOURS"
const val MINUTES = "MINUTES"
const val REWARD = "REWARD"


fun setFirstTime(first_time: Boolean) {
    Paper.book(FIRST_TIME).write("first_time", first_time)
}


fun isFirstTime(): Boolean {
    val keys =
        Paper.book(FIRST_TIME).allKeys
    if (keys.size == 0) return true
    return Paper.book(FIRST_TIME).read("first_time")
}

fun addAccessToken(token: String?) {
    Paper.book(ACCESS_TOEKN).write("token", token)
}


fun getAccessToken(): String? {
    val keys =
        Paper.book(ACCESS_TOEKN).allKeys
    if (keys.size == 0) return null
    return Paper.book(ACCESS_TOEKN)?.read("token")
}


fun deleteAccessToken() {
    Paper.book(ACCESS_TOEKN).delete("token")
}

//fun addUser(registerUserModel: RegisterModel) {
//    Paper.book(USER).write("User", registerUserModel)
//}


fun deleteUser() {
    Paper.book(USER).delete("loginUser")
}

fun deleteRegisterUser() {
    Paper.book(REGISTER_USER).delete("registerUser")
}

//fun getUser(): RegisterModel? {
//    val keys =
//        Paper.book(USER).allKeys
//    if (keys.size == 0) return null
//    return Paper.book(USER)?.read("User")
//}


fun setFCMToken(token: String) {
    Paper.book(FIREBASE_TOKEN).write(FCM, token)
}

fun getFCMToken(): String? {
    val keys =
        Paper.book(FIREBASE_TOKEN).allKeys
    if (keys.size == 0) return ""
    return Paper.book(FIREBASE_TOKEN).read(FCM)
}




