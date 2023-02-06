package com.example.kotlinjetpackdogs.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kotlinjetpackdogs.R
import com.example.kotlinjetpackdogs.databinding.FragmentDetailBinding
import com.example.kotlinjetpackdogs.model.DogBreed
import com.example.kotlinjetpackdogs.viewmodel.DetailViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private lateinit var viewModel: DetailViewModel
    private lateinit var dog: DogBreed

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
        viewModel.fetch()

        arguments?.let {
            dog = DetailFragmentArgs.fromBundle(it).dogBreed
        }

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
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}