package com.tagreader.model

import com.google.gson.annotations.SerializedName

data class Entry(
        val id: Long,
        val date: String,
        val author: String,
        val body: String,

        @SerializedName("vote_count")
        val voteCount: Int,
        val comments: Array<Entry>,
        val embed: Embed,

        val url: String)