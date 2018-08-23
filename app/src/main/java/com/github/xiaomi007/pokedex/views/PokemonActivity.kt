package com.github.xiaomi007.pokedex.views

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.widget.Toast
import com.github.xiaomi007.pokedex.R
import com.github.xiaomi007.pokedex.models.dao.PokemonDatabase
import com.github.xiaomi007.pokedex.presenter.PokemonActivityPresenter
import com.github.xiaomi007.pokedex.presenter.PokemonListAdapter
import com.github.xiaomi007.pokedex.presenter.PokemonPresenterFactory
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_pokemon.*

/**
 * Display a list of pokemon and a search view to filter the list by name.
 *
 * @author Damien
 */
class PokemonActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "PokemonActivity"
        private const val SAVE_LAYOUT_STATE = "save_layout_state"
    }

    private lateinit var mPokemonPresenterFactory: PokemonPresenterFactory
    private lateinit var mPokemonActivityPresenter: PokemonActivityPresenter

    private val disposables = CompositeDisposable()

    private val mAdapter = PokemonListAdapter()
    private val mLayoutManager by lazy { LinearLayoutManager(this) }
    private val mItemDecoration by lazy { DividerItemDecoration(this, DividerItemDecoration.VERTICAL) }
    private var restoredListState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon)

        mPokemonPresenterFactory = PokemonPresenterFactory(PokemonDatabase.getInstance(this).pokemonDao())
        mPokemonActivityPresenter = ViewModelProviders.of(this, mPokemonPresenterFactory).get(PokemonActivityPresenter::class.java)

    }

    override fun onStart() {
        super.onStart()
        recycler.adapter = mAdapter
        recycler.layoutManager = mLayoutManager
        recycler.addItemDecoration(mItemDecoration)
        loadList("")

        searchView.setIconifiedByDefault(false)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val text = newText ?: ""
                loadList(text)
                return true
            }
        })

    }

    fun loadList(text: String) {
        disposables.clear()
        disposables.addAll(
                mPokemonActivityPresenter.queryPagingPokemon(text)
                        .subscribe(
                                {
                                    mAdapter.submitList(it)
                                    if (restoredListState != null) {
                                        Log.d(TAG, "Restore layout state")
                                        mLayoutManager.onRestoreInstanceState(restoredListState)
                                        restoredListState = null
                                    }
                                },
                                {
                                    Log.e(TAG, "Photo error $it")
                                    Toast.makeText(
                                            this@PokemonActivity,
                                            R.string.launcher_network_error,
                                            Toast.LENGTH_LONG
                                    ).show()
                                }
                        )
        )
    }

    override fun onStop() {
        disposables.clear()
        recycler.adapter = null
        recycler.layoutManager = null
        recycler.removeItemDecoration(mItemDecoration)
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(SAVE_LAYOUT_STATE, mLayoutManager.onSaveInstanceState())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            restoredListState = savedInstanceState.getParcelable(SAVE_LAYOUT_STATE)
        }
        super.onRestoreInstanceState(savedInstanceState)
    }


}
