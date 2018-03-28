package com.tagreader.model

import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import com.google.gson.annotations.SerializedName

data class Entry(
        val id: Long,
        val date: String,
        val title: String,
        val description: String,
        val preview: String,
        val tags: String,

        @SerializedName("comment_count")
        val commentCount: Int,
        val author: String,
        val body: String,

        @SerializedName("vote_count")
        val voteCount: Int,
        val comments: Array<Entry>,
        val embed: Embed,

        val url: String) {

    val image: String
        get () = if (embed != null) embed.preview else preview

    val header: String
        get() = if (TextUtils.isEmpty(title)) author else title

    val content: Spanned
        get() = Html.fromHtml(if (TextUtils.isEmpty(description)) body else description, Html.FROM_HTML_MODE_COMPACT)

    val hasTags: Boolean
        get() = !TextUtils.isEmpty(tags)

    val hasImage: Boolean
        get() = !TextUtils.isEmpty(image)
}