package com.elfay.myfridgerecipies.models.recipe_details

data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>
)