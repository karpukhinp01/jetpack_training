package com.example.kotlinjetpackdogs.view

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.kotlinjetpackdogs.R
import com.example.kotlinjetpackdogs.databinding.FragmentDetailBinding
import com.example.kotlinjetpackdogs.model.DogBreed
import com.example.kotlinjetpackdogs.model.DogPalette
import com.example.kotlinjetpackdogs.util.getProgressDrawable
import com.example.kotlinjetpackdogs.util.loadImage
import com.example.kotlinjetpackdogs.viewmodel.DetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private lateinit var viewModel: DetailViewModel
    private var dogUuid: Int = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        arguments?.let {
            dogUuid = DetailFragmentArgs.fromBundle(it).uuid
        }
        viewModel.fetch(dogUuid)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.dog.observe(viewLifecycleOwner) { dog ->
            dog?.let {
                binding.apply {
                    dogName.text = dog.dogBreed
                    dogLifespan.text = dog.lifeSpan
                    dogPurpose.text = dog.bredFor
                    dogTemperament.text = dog.temperament
                    dogImage.loadImage(dog.imageUrl!!, getProgressDrawable(requireContext()))
                }
//                it.imageUrl?.let {
//                    setUpBackgroundColor(it)
//                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpBackgroundColor(url: String) {
        Glide.with(requireContext())
            .asBitmap()
            .load(url)
            .into(object: CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate { palette ->
                            val intColor = palette?.lightMutedSwatch?.rgb ?: 0
                            val myPalette = DogPalette(intColor)
                            binding.root.setBackgroundColor(myPalette.color)
                        }
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }
}