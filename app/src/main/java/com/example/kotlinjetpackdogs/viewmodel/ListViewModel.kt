package com.example.kotlinjetpackdogs.viewmodel

import android.app.Application
import android.app.NotificationManager
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinjetpackdogs.model.DogBreed
import com.example.kotlinjetpackdogs.model.DogDatabase
import com.example.kotlinjetpackdogs.model.DogsApiService
import com.example.kotlinjetpackdogs.util.NotificationsHelper
import com.example.kotlinjetpackdogs.util.SharedPreferencesHelper
import kotlinx.coroutines.launch

class ListViewModel(application: Application): BaseViewModel(application) {

    private var prefHelper = SharedPreferencesHelper(getApplication())
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    private val dogsService = DogsApiService()

    private val _dogs = MutableLiveData<List<DogBreed>>()
    val dogs: LiveData<List<DogBreed>> get() = _dogs

    private val _dogsLoadError = MutableLiveData<Boolean>()
    val dogsLoadError: LiveData<Boolean> get() = _dogsLoadError

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun refresh() {
        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else fetchFromRemote()
    }

    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()

        try {
            val cachePreferenceInt = cachePreference?.toInt() ?: (5 * 60)
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        } catch (e: java.lang.NumberFormatException) {
            e.printStackTrace()
        }
    }

    private fun fetchFromDatabase() {
        _loading.value = true
        launch {
            val dogs = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsRetrieved(dogs)
            Toast.makeText(getApplication(), "Dogs retrieved from DB", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchFromRemote() {
        _loading.value = true
        viewModelScope.launch {
           try {
               dogsRetrieved(dogsService.getDogs())
               storeDogsLocally(_dogs.value!!)
               Toast.makeText(getApplication(), "Dogs retrieved from REMOTE", Toast.LENGTH_SHORT).show()
               NotificationsHelper(getApplication()).createNotification()
           } catch (e: Throwable) {
               _loading.value = false
               _dogsLoadError.value = true
               e.printStackTrace()
           }
        }
    }

    private fun dogsRetrieved(dogs: List<DogBreed>) {
        _dogs.value = dogs
        _loading.value = false
        _dogsLoadError.value = false
    }

    fun refreshBypassCache() {
        fetchFromRemote()

    }


    private suspend fun storeDogsLocally(dogsList: List<DogBreed>) {
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            dao.deleteAll()
            val result = dao.insertAll(*dogsList.toTypedArray())
            var i = 0
            while (i < dogsList.size) {
                dogsList[i].uuid = result[i].toInt()
                ++i
            }

        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }
}
