package com.example.kotlinjetpackdogs.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinjetpackdogs.R
import com.example.kotlinjetpackdogs.model.DogBreed
import com.example.kotlinjetpackdogs.util.getProgressDrawable
import com.example.kotlinjetpackdogs.util.loadImage

class DogsListAdapter(private val dogsList: ArrayList<DogBreed>): RecyclerView.Adapter<DogsListAdapter.DogViewHolder>() {

    fun updateDogsList(newDogsList: List<DogBreed>) {
        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_dog, parent, false)
        return DogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {

        val currentItem = dogsList[position]
        Log.d("dogBreed", currentItem.toString())
        holder.dogName.text = currentItem.dogBreed
        holder.dogLifeSpan.text = currentItem.lifeSpan
        holder.view.setOnClickListener {
            Navigation.findNavController(it).navigate(ListFragmentDirections.actionListFragmentToDetailFragment(currentItem.uuid))
        }
        holder.image.loadImage(currentItem.imageUrl!!, getProgressDrawable(context = holder.image.context))
    }

    override fun getItemCount() = dogsList.size

    class DogViewHolder(var view: View): RecyclerView.ViewHolder(view){
        val image = view.findViewById<ImageView>(R.id.dog_image)
        val dogName = view.findViewById<TextView>(R.id.dog_name)
        val dogLifeSpan = view.findViewById<TextView>(R.id.lifespan)
    }
}