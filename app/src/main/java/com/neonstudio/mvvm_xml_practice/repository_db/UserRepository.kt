package com.neonstudio.mvvm_xml_practice.repository_db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.neonstudio.mvvm_xml_practice.api.UserAPI
import com.neonstudio.mvvm_xml_practice.models.UserRequest
import com.neonstudio.mvvm_xml_practice.models.UserResponse
import com.neonstudio.mvvm_xml_practice.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {


    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData


    suspend fun registerUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.signup(userRequest)
        //Log.d(TAG, response.body().toString())
        handleResponse(response)

    }

    suspend fun loginUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.signin(userRequest)
        //Log.d(TAG, response.body().toString())
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {

            try {

//                val errorBody = response.errorBody()!!.string()
//            val errorObj = JSONObject(errorBody)

                val errorObj = JSONObject(response.errorBody()!!.string())
                //val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error parsing response: ${e.message}")
                _userResponseLiveData.postValue(NetworkResult.Error("Unexpected response format"))
            }

//


        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }


}