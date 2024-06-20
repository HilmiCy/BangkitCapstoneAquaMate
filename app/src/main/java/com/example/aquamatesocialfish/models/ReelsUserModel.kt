package com.example.aquamatesocialfish.models

class ReelsUserModel {
    var videoReelsUrl: String = ""
    var contentCaption: String = ""
    var profileImageReel: String? = null

    constructor()
    constructor(videoReelsUrl: String, contentCaption: String) {
        this.videoReelsUrl = videoReelsUrl
        this.contentCaption = contentCaption
    }

    constructor(videoReelsUrl: String, contentCaption: String, profileImageReel: String) {
        this.videoReelsUrl = videoReelsUrl
        this.contentCaption = contentCaption
        this.profileImageReel = profileImageReel
    }


}