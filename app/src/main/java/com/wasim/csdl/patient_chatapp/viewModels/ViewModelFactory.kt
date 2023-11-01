package com.wasim.csdl.patient_chatapp.viewModels

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wasim.csdl.patient_chatapp.utils.source.DataRepository

class ViewModelFactory(var dataRepository: DataRepository, application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    private var application: Application? = null

    init {
        this.application = application
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(AuthViewModel::class.java) ->
                    AuthViewModel(dataRepository, application!!)
//
//                isAssignableFrom(MainActivityViewModel::class.java) ->
//                    MainActivityViewModel(dataRepository,application!!)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T


    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            synchronized(ViewModelFactory::class.java) {
                ViewModelFactory(DataRepository(application.applicationContext), application)
                    .also { INSTANCE = it }
            }


        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
