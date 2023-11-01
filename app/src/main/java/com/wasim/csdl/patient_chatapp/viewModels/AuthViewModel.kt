package com.wasim.csdl.patient_chatapp.viewModels

import android.app.Application
import com.wasim.csdl.patient_chatapp.utils.source.DataRepository


class AuthViewModel(var dataRepository: DataRepository, application: Application) :
    BaseViewModel(application) {

   // var registerLiveData = MutableLiveData<NewResgisterREsponse>()


//    fun registerUSer(registerRequest: RegisterRequest) {
//        viewModelScope.launch {
//            when (val apiResponse = dataRepository.registerUser(registerRequest)) {
//                is ResultWrapper.NetworkError -> errorLiveData.postValue("Network Error")
//                is ResultWrapper.GenericError -> errorLiveData.postValue(apiResponse.error)
//                is ResultWrapper.Success -> {
//
////                    if (apiResponse.value.code() == 401) {
////                        errorLiveData.postValue("Unauthenticated User")
////                    } else {
//                    if (apiResponse.value.code() == 200) {
//                        Log.d("ok", "${apiResponse.value.body()}")
//                        registerLiveData.postValue(apiResponse.value.body())
//                    } else {
//                        Log.d("ok", "${apiResponse.value.body()}")
//                        errorLiveData.postValue(apiResponse.value.body().toString())
//                    }
//                }
//            }
//        }
//    }


    }


