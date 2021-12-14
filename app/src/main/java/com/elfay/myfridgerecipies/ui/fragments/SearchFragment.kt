package com.elfay.myfridgerecipies.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.elfay.myfridgerecipies.adapters.RecipesAdapter
import com.elfay.myfridgerecipies.databinding.FragmentSearchBinding
import com.elfay.myfridgerecipies.ui.viewmodels.RecipesViewModel
import com.elfay.myfridgerecipies.util.Const.Companion.SEACH_RECIPES_TIME_DELAY
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.elfay.myfridgerecipies.models.RecipiesByIngredientsReponse
import com.elfay.myfridgerecipies.util.Const
import com.elfay.myfridgerecipies.util.Resource
import java.util.stream.Collectors.toList


class SearchFragment : Fragment() {

    private val viewModel: RecipesViewModel by viewModels()

    private lateinit var recipesAdapter: RecipesAdapter
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        var job: Job? = null
        binding.search.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEACH_RECIPES_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.getRecipes(editable.toString())
                    }
                }
            }
        }
        viewModel.searchedRecipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { recipesResponse ->
                        recipesAdapter.differ.submitList(recipesResponse)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e(Const.TAG, "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
        recipesAdapter.setOnItemClickListener {
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragment2ToRecipeDetailsFragment(it.id)
            )
        }
    }


    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        recipesAdapter = RecipesAdapter()
        binding.recipiesRecyclerView.apply {
            adapter = recipesAdapter
            layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        }
    }
}