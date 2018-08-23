package com.github.xiaomi007.pokedex.models.item

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Pokemon Raw is the pokemon object which comes from outside (network).
 *
 * @author Damien
 */
data class PokemonRaw(
        @SerializedName("url") val url: String,
        @SerializedName("name") val name: String) {

    /**
     * Extract the Pokemon unique identifier from the url parameter.
     */
    fun getIdFromUrl(): Int {
        return url.substringAfter("pokemon/").substringBefore("/").toInt()
    }

    /**
     * Build the pokemon's image url from its id.
     *
     * @param id Pokemon unique identifier
     */
    fun getImageUrlFromId(id: Int): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
    }
}

/**
 *
 *
 * @author Damien
 */
@Entity(tableName = "pokemon")
data class Pokemon(
        @PrimaryKey @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
        val id: Int,
        @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT, index = true)
        val name: String,
        @ColumnInfo(name = "url", typeAffinity = ColumnInfo.TEXT)
        val url: String
) {
    companion object {
        /**
         * Make a clean Pokemon object from a PokemonRaw Object.
         *
         * @param pokeRaw Raw Pokemon Object
         * @return Clean Pokemon Object
         */
        fun makePokemon(pokeRaw: PokemonRaw): Pokemon {
            val id = pokeRaw.getIdFromUrl()
            val spriteUrl = pokeRaw.getImageUrlFromId(id)
            return Pokemon(id, pokeRaw.name, spriteUrl)
        }
    }
}