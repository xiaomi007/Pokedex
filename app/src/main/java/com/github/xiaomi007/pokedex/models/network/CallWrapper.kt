package com.github.xiaomi007.pokedex.models.network

import com.github.xiaomi007.pokedex.models.item.PokemonRaw

/**
 * CallWrapper wraps the call to /pokemon/ api, which has the count of available pokemon,
 * the previous and next page, and the list of Pokemon.
 *
 * @author Damien
 */
data class CallWrapper(
        val count: Int,
        val previous: String?,
        val next: String?,
        val results: List<PokemonRaw>
)