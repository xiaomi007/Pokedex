package com.github.xiaomi007.pokedex.models.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.github.xiaomi007.pokedex.models.item.Pokemon

/**
 * Database which contains the information about Pokemons
 *
 * @author Damien
 */
@Database(
        entities = [Pokemon::class],
        version = 1,
        exportSchema = false
)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao

    companion object {
        @Volatile
        private var INSTANCE: PokemonDatabase? = null

        /**
         * Create an unique instance of the DB.
         *
         * @param context application's context
         * @return a database instance
         */
        fun getInstance(context: Context): PokemonDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        /**
         * Helper to build the PokemonDatabase object
         *
         * @param context application's context
         * @return a database object
         */
        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(
                        context.applicationContext,
                        PokemonDatabase::class.java,
                        "PokemonDB.db"
                ).build()
    }
}