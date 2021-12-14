package com.elfay.myfridgerecipies.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.elfay.myfridgerecipies.R
import com.elfay.myfridgerecipies.adapters.ViewPagerAdapter
import com.elfay.myfridgerecipies.databinding.FragmentRecipeDetailsBinding
import com.elfay.myfridgerecipies.models.recipe_details.RecipeDetailsResponse
import com.elfay.myfridgerecipies.ui.viewmodels.RecipesViewModel
import com.elfay.myfridgerecipies.util.Const
import com.elfay.myfridgerecipies.util.Resource


class RecipeDetailsFragment : Fragment() {
    private val viewModel: RecipesViewModel by viewModels()
    private var _binding: FragmentRecipeDetailsBinding? = null
    private val binding get() = _binding!!
    val args: RecipeDetailsFragmentArgs by navArgs()
    val messages = listOf("one","two","three","four","five")
    val adapter = ViewPagerAdapter(messages)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recipeId = args.recipeId
        viewModel.getRecipeInformation(recipeId)

        viewModel.recipeInformationResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { recipesResponse ->
                        bind(recipesResponse)
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


    }

    private fun bind(recipeDetails : RecipeDetailsResponse){
        Glide.with(this).load(recipeDetails.image).into(binding.recipeImage)
        binding.recipeName.text = recipeDetails.title
        var steps : String ="empty comeback later"
        if(!recipeDetails.analyzedInstructions.isEmpty()) {
            steps = ""
            for (i in recipeDetails.analyzedInstructions[0].steps.indices) {
                steps += "${i + 1}-" + recipeDetails.analyzedInstructions[0].steps[i].step + "\n"
            }
        }
       // binding.instructions.text = steps
        binding.viewPager.adapter = adapter
    }
    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }
}