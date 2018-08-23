package com.github.xiaomi007.pokedex.models.dao

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.github.xiaomi007.pokedex.models.item.Pokemon

/**
 * Pokemon Dao: Represent the operation on the Pokemon Table
 *
 * @author Damien
 */
@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon WHERE name LIKE '%' || :sub || '%'")
    fun getAllNamesByName(sub: String): DataSource.Factory<Int, Pokemon>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(names: List<Pokemon>)
}