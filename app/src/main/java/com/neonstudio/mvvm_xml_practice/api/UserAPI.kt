package com.neonstudio.mvvm_xml_practice.api

import com.neonstudio.mvvm_xml_practice.models.UserRequest
import com.neonstudio.mvvm_xml_practice.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {

    @POST("/users/signup")
    suspend fun signup(@Body userResponse: UserRequest)  :  Response<UserResponse>


    @POST("/users/signin")
    suspend fun signin(@Body userResponse: UserRequest)  :  Response<UserResponse>

}