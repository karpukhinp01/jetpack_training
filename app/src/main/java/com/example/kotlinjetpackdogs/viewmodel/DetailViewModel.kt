package com.example.kotlinjetpackdogs.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinjetpackdogs.model.DogBreed
import com.example.kotlinjetpackdogs.model.DogDatabase
import kotlinx.coroutines.launch

class DetailViewModel(application: Application): BaseViewModel(application) {

    private val _dog = MutableLiveData<DogBreed>()
    val dog: LiveData<DogBreed> get() = _dog

    fun fetch(uuid: Int) {
        viewModelScope.launch {
            _dog.value = DogDatabase(getApplication()).dogDao().getDog(uuid)
        }
    }

}