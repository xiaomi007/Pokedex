package com.github.xiaomi007.pokedex.presenter

import com.github.xiaomi007.pokedex.models.PokemonRepository
import com.github.xiaomi007.pokedex.models.dao.PokemonDao
import com.github.xiaomi007.pokedex.models.network.PokemonServer

/**
 * Presenter for Splash Activity
 *
 * @author Damien
 */
class SplashActivityPresenter(server: PokemonServer, dao: PokemonDao) : BasePresenter() {

    /**
     *  Repository to handler persistent and online data for Pokemon
     */
    private val mPokemonRepo = PokemonRepository(server, dao)

    /**
     * Fetch the data in order to initialized the application.
     */
    fun initializedData() {
        proceedToFetchData(
                mPokemonRepo.fetchProducts()
        )
    }
}
