package com.elfay.myfridgerecipies.models.recipe_details

data class WinePairing(
    val pairedWines: List<String>,
    val pairingText: String,
    val productMatches: List<ProductMatche>
)