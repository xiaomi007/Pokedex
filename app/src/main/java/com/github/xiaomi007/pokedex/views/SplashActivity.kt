package com.github.xiaomi007.pokedex.views

import android.app.ActivityOptions
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.github.xiaomi007.pokedex.R
import com.github.xiaomi007.pokedex.models.dao.PokemonDatabase
import com.github.xiaomi007.pokedex.models.network.PokemonServer
import com.github.xiaomi007.pokedex.presenter.SplashActivityPresenter
import com.github.xiaomi007.pokedex.presenter.SplashPresenterFactory
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Splash Activity will fetch the data. If successful it will start Pokemon Activity. If failure, a
 * button retry will show up.
 *
 * @author Damien
 */
class SplashActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val TAG = "SplashActivity"
    }

    private lateinit var mSplashPresenterFactory: SplashPresenterFactory
    private lateinit var mSplashActivityPresenter: SplashActivityPresenter
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val db = PokemonDatabase.getInstance(this)
        mSplashPresenterFactory = SplashPresenterFactory(
                PokemonServer.get(),
                db.pokemonDao()
        )
        mSplashActivityPresenter = ViewModelProviders.of(this, mSplashPresenterFactory)
                .get(SplashActivityPresenter::class.java)
        setLoadingState()
        mSplashActivityPresenter.initializedData()
    }

    override fun onStart() {
        super.onStart()
        disposables.addAll(
                mSplashActivityPresenter.observeLoading()
                        .subscribe { isLoading ->
                            if (isLoading) {
                                setLoadingState()
                            }
                        },
                mSplashActivityPresenter.observeQueryStatus()
                        .subscribe { status ->
                            if (status.error == null) {
                                startPokemonActivity()
                                finish()
                            } else {
                                Log.e(TAG, "Error on product query ${status.error.message}")
                                setErrorState()
                            }
                        }
        )
    }

    override fun onStop() {
        disposables.clear()
        super.onStop()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.retry_btn -> {
                mSplashActivityPresenter.initializedData()
            }
        }
    }

    private fun setLoadingState() {
        loading_tv.setText(R.string.launcher_loading_data)
        loading_pb.visibility = View.VISIBLE
        retry_btn.visibility = View.GONE
        retry_btn.setOnClickListener(null)
    }

    private fun setErrorState() {
        loading_tv.setText(R.string.launcher_network_error)
        loading_pb.visibility = View.INVISIBLE
        retry_btn.visibility = View.VISIBLE
        retry_btn.setOnClickListener(this)
    }

    private fun startPokemonActivity() {
        val intent = Intent(applicationContext, PokemonActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(
                applicationContext,
                android.R.anim.fade_in,
                android.R.anim.fade_out
        )
        startActivity(intent, options.toBundle())
    }

}