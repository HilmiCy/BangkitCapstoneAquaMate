package com.example.aquamatesocialfish.models

class PostUserModel {
    var contentUrl: String = ""
    var contentCaption: String = ""
    var uid: String = ""
    var time: String = ""
    var likedBy: ArrayList<String> = ArrayList()

    constructor()

    constructor(contentUrl: String, contentCaption: String) {
        this.contentUrl = contentUrl
        this.contentCaption = contentCaption
    }

    constructor(contentUrl: String, contentCaption: String, uid: String, time: String) {
        this.contentUrl = contentUrl
        this.contentCaption = contentCaption
        this.uid = uid
        this.time = time
    }
}