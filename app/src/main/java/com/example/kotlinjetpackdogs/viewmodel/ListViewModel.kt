package com.example.kotlinjetpackdogs.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinjetpackdogs.model.DogBreed
import com.example.kotlinjetpackdogs.model.DogDatabase
import com.example.kotlinjetpackdogs.model.DogsApiService
import kotlinx.coroutines.launch

class ListViewModel(application: Application): BaseViewModel(application) {

    private val dogsService = DogsApiService()

    private val _dogs = MutableLiveData<List<DogBreed>>()
    val dogs: LiveData<List<DogBreed>> get() = _dogs

    private val _dogsLoadError = MutableLiveData<Boolean>()
    val dogsLoadError: LiveData<Boolean> get() = _dogsLoadError

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun refresh() {
        fetchFromRemote()
    }

    private fun fetchFromRemote() {
        _loading.value = true
        viewModelScope.launch {
           try {
               dogsRetrieved()
               storeDogsLocally(_dogs.value!!)
           } catch (e: Throwable) {
               _loading.value = false
               _dogsLoadError.value = true
               e.printStackTrace()
           }
        }
    }

    private suspend fun dogsRetrieved() {
            _dogs.value = dogsService.getDogs()
            _loading.value = false
            _dogsLoadError.value = false
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
            dogsRetrieved()
        }
    }
}
