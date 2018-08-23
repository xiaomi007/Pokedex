@file:Suppress("UNCHECKED_CAST")

package com.github.xiaomi007.pokedex.presenter

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.github.xiaomi007.pokedex.models.dao.PokemonDao
import com.github.xiaomi007.pokedex.models.network.PokemonServer

/**
 * Factories for Presenter in order to pass custom parameters.
 *
 * @author Damien
 */
class SplashPresenterFactory(
        private val server: PokemonServer,
        private val dao: PokemonDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashActivityPresenter::class.java)) {
            return SplashActivityPresenter(server, dao) as T
        }
        throw IllegalArgumentException("Unknown SplashActivityPresenter class")
    }
}

class PokemonPresenterFactory(
        private val dao: PokemonDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonActivityPresenter::class.java)) {
            return PokemonActivityPresenter(dao) as T
        }
        throw IllegalArgumentException("Unknown PokemonActivityPresenter class")
    }
}
