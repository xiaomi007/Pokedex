package com.github.xiaomi007.pokedex.models

import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import android.util.Log
import com.github.xiaomi007.pokedex.models.dao.PokemonDao
import com.github.xiaomi007.pokedex.models.item.Pokemon
import com.github.xiaomi007.pokedex.models.item.PokemonRaw
import com.github.xiaomi007.pokedex.models.network.CallWrapper
import com.github.xiaomi007.pokedex.models.network.PokemonServer
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Pokemon Repository link the view and the data
 *
 * @author Damien
 */
class PokemonRepository(private val server: PokemonServer, private val dao: PokemonDao) {

    companion object {
        private const val TAG = "PokemonRepository"
        private const val PAGE_SIZE = 20
        private const val ENABLE_PLACEHOLDERS = true
    }

    fun fetchProducts(): Flowable<CallWrapper> {
        return server.getAllPokemon(1000)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { callWrapper ->
                    Log.d(TAG, "fetchProduct on ${Thread.currentThread().name}")
                    insertAll(callWrapper.results)
                }
    }

    private fun insertAll(list: List<PokemonRaw>?) {
        if (list != null && list.isNotEmpty()) {
            Observable.fromIterable(list.asIterable())
                    .subscribeOn(Schedulers.computation())
                    .map { pRaw ->
                        Pokemon.makePokemon(pRaw)
                    }
                    .filter { pokemon -> pokemon.id < 1000 }
                    .toList()
                    .observeOn(Schedulers.io())
                    .doOnSuccess {
                        Log.d(TAG, "Pokemon register into DB ${it.size}")
                        dao.insertAll(it)
                    }
                    .subscribe()
        }

    }

    fun getPhotosPerCategoryPaging(name: String): Flowable<PagedList<Pokemon>> {
        return RxPagedListBuilder(
                dao.getAllNamesByName(name),
                pagedListConfig
        ).buildFlowable(BackpressureStrategy.LATEST)

    }

    private val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(ENABLE_PLACEHOLDERS)
            .build()

}