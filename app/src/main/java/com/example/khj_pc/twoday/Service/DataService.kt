package com.example.khj_pc.twoday.Service

import com.example.khj_pc.twoday.Data.Data
import com.example.khj_pc.twoday.Data.DataResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DataService {
    @POST("/dev/data")
    fun sendWord(@Body data : Data) : Call<DataResponse>
}