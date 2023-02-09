package com.example.kotlinjetpackdogs.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kotlinjetpackdogs.databinding.FragmentListBinding
import com.example.kotlinjetpackdogs.viewmodel.ListViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinjetpackdogs.R
import com.example.kotlinjetpackdogs.viewmodel.BaseViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private val dogsListAdapter = DogsListAdapter(arrayListOf())
    private var _binding: FragmentListBinding ? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.inflateMenu(R.menu.menu_main)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_settings -> {
                    findNavController().navigate(R.id.action_listFragment_to_settingsFragment)
                    true
                }
                else -> false
            }
        }

        binding.refreshLayout.setOnRefreshListener {
            binding.dogsList.visibility = View.GONE
            binding.listError.visibility = View.GONE
            binding.loadingView.visibility = View.VISIBLE
            viewModel.refreshBypassCache()
            binding.refreshLayout.isRefreshing = false
        }

        viewModel = ViewModelProvider(this)[ListViewModel::class.java]
        viewModel.refresh()

        binding.dogsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogsListAdapter
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.dogs.observe(viewLifecycleOwner) { dogs ->
            dogs?.let {
                binding.dogsList.visibility = View.VISIBLE
                dogsListAdapter.updateDogsList(dogs)
            }
        }
        viewModel.dogsLoadError.observe(viewLifecycleOwner) { isError ->
            isError?.let {
                binding.listError.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let {
                binding.loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    binding.listError.visibility = View.GONE
                    binding.dogsList.visibility = View.GONE
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}