package com.wasim.csdl.patient_chatapp.models

class Users {
    var user_ID: String = ""
    var user_Name: String = ""
    private var profile: String = ""
    private var cover: String = ""
    private var status: String = ""
    private var search: String = ""
    private var facebook: String = ""
    private var instagram: String = ""
    private var website: String = ""

    constructor()

    constructor(
        user_ID: String,
        username: String,
        profile: String,
        cover: String,
        status: String,
        search: String,
        facebook: String,
        instagram: String,
        website: String
    ) {
        this.user_ID = user_ID
        this.user_Name = username
        this.profile = profile
        this.cover = cover
        this.status = status
        this.search = search
        this.facebook = facebook
        this.instagram = instagram
        this.website = website
    }

     fun getUSerId():  String  {
        return user_ID
    }

    fun setUserId(uid: String) {
        this.user_ID = uid
    }

    fun getUsername(): String {
        return user_Name
    }

    fun setUsername(username: String) {
        this.user_Name = username
    }

    fun getProfile(): String {
        return profile
    }

    fun setProfile(profile: String) {
        this.profile = profile
    }

    fun getCover(): String {
        return cover
    }

    fun setCover(cover: String) {
        this.cover = cover
    }

    fun getStatus(): String {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
    }

    fun getSearch(): String {
        return search
    }

    fun setSearch(search: String) {
        this.search = search
    }

    fun getFacebook(): String {
        return facebook
    }

    fun setFacebook(facebook: String) {
        this.facebook = facebook
    }

    fun getInstagram(): String {
        return instagram
    }

    fun setInstagram(instagram: String) {
        this.instagram = instagram
    }

    fun getWebsite(): String {
        return website
    }

    fun setWebsite(website: String) {
        this.website = website
    }



}