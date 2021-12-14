package com.elfay.myfridgerecipies.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfay.myfridgerecipies.api.RetrofitInstance
import com.elfay.myfridgerecipies.models.RecipiesByIngredientsReponse
import com.elfay.myfridgerecipies.models.recipe_details.RecipeDetailsResponse
import com.elfay.myfridgerecipies.util.Const.Companion.API_KEY
import com.elfay.myfridgerecipies.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class RecipesViewModel: ViewModel() {

    val recipeInformationResponse: MutableLiveData<Resource<RecipeDetailsResponse>> = MutableLiveData()
    val searchedRecipesResponse: MutableLiveData<Resource<RecipiesByIngredientsReponse>> = MutableLiveData()
    //var searchRecpesPage = 1
   // var storeSearchedRecipesResponse : RecipiesByIngredientsReponse? = null

    fun getRecipes(ingredients: String) = viewModelScope.launch {
        searchedRecipesResponse.postValue(Resource.Loading())
        val response = RetrofitInstance.api.getRecipesByIngredients(ingredients,API_KEY)
        searchedRecipesResponse.postValue(handleSearchedRecipesResponse(response))

    }

    fun getRecipeInformation(id : Int) = viewModelScope.launch {
        recipeInformationResponse.postValue(Resource.Loading())
        val response = RetrofitInstance.api.getRecipeInformation(id,API_KEY)
        recipeInformationResponse.postValue(handleRecipeInformationResponse(response))
    }

    private fun handleSearchedRecipesResponse(response: Response<RecipiesByIngredientsReponse>): Resource<RecipiesByIngredientsReponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success( resultResponse)
            }
        }
        return Resource.Error(response.message())

    }

    private fun handleRecipeInformationResponse(response: Response<RecipeDetailsResponse>): Resource<RecipeDetailsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success( resultResponse)
            }
        }
        return Resource.Error(response.message())

    }
}