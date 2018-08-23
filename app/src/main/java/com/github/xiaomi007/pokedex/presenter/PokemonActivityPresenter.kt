package com.github.xiaomi007.pokedex.presenter

import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import com.github.xiaomi007.pokedex.models.PokemonRepository
import com.github.xiaomi007.pokedex.models.dao.PokemonDao
import com.github.xiaomi007.pokedex.models.item.Pokemon
import com.github.xiaomi007.pokedex.models.network.PokemonServer
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Presenter for Pokemon Activity
 *
 * @author Damien
 */
class PokemonActivityPresenter(dao: PokemonDao) : ViewModel() {

    private val mRepo = PokemonRepository(PokemonServer.get(), dao)

    fun queryPagingPokemon(name: String): Flowable<PagedList<Pokemon>> {
        return mRepo.getPhotosPerCategoryPaging(name)
                .observeOn(AndroidSchedulers.mainThread())
    }

}