package com.example.kotlinjetpackdogs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinjetpackdogs.model.DogBreed

class DetailViewModel: ViewModel() {

    private val _dog = MutableLiveData<DogBreed>()
    val dog: LiveData<DogBreed> get() = _dog

    fun fetch() {
        val newDog = DogBreed("1", "Corgi", "15 Years", "BreedGroup", "bred for", "temperament", "no")
        _dog.value = newDog
    }

}