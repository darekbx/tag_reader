package com.tagreader.api

import com.tagreader.model.Entry
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WykopEndpoints {

    @GET("/tag/{tag}/page/{page}/appkey/{appKey}")
    fun getTag(
            @Path("tag") tag: String,
            @Path("page") page: Int,
            @Path("appKey") appKey: String): Call<Entry>
}