package com.github.xiaomi007.pokedex.presenter

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.xiaomi007.pokedex.R
import com.github.xiaomi007.pokedex.models.item.Pokemon
import com.squareup.picasso.Picasso

/**
 * PagedList Adapter to display a list of Pokemon
 *
 * @author Damien
 */
class PokemonListAdapter : PagedListAdapter<Pokemon, PokemonListAdapter.PokemonVH>(
        object : DiffUtil.ItemCallback<Pokemon>() {

            override fun areItemsTheSame(oldItem: Pokemon?, newItem: Pokemon?): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Pokemon?, newItem: Pokemon?): Boolean {
                return oldItem == newItem
            }
        }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonVH {
        return PokemonVH(LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false))
    }

    override fun onBindViewHolder(holder: PokemonVH, position: Int) {
        val pokemon = getItem(position)
        pokemon?.let {
            holder.name.text = pokemon.name
            holder.id.text = pokemon.id.toString()
            Picasso.get()
                    .load(pokemon.url)
                    .placeholder(R.drawable.pokeball_silhouette)
                    .error(R.drawable.pokeball_silhouette)
                    .into(holder.sprite)
        }
    }

    class PokemonVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: AppCompatTextView = itemView.findViewById(R.id.pokemon_name)
        val id: AppCompatTextView = itemView.findViewById(R.id.pokemon_id)
        val sprite: AppCompatImageView = itemView.findViewById(R.id.pokemon_image)
    }
}