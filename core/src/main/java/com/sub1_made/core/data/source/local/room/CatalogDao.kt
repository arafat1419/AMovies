package com.sub1_made.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.sub1_made.core.data.source.local.entity.MovieEntity
import com.sub1_made.core.data.source.local.entity.TvEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CatalogDao {

    @RawQuery(observedEntities = [MovieEntity::class])
    fun getListMovies(query: SupportSQLiteQuery): Flow<List<MovieEntity>>

    @RawQuery(observedEntities = [TvEntity::class])
    fun getListTvShows(query: SupportSQLiteQuery): Flow<List<TvEntity>>

    @Query("SELECT * FROM movies_entities WHERE isFavorite = 1")
    fun getListFavMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM tvShows_entities WHERE isFavorite = 1")
    fun getListFavTvShows(): Flow<List<TvEntity>>

    @Query("SELECT * FROM movies_entities WHERE id = :movieId")
    fun getDetailMovieById(movieId: Int): LiveData<MovieEntity>

    @Query("SELECT * FROM tvShows_entities WHERE id = :tvShowId")
    fun getDetailTvShowById(tvShowId: Int): LiveData<TvEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = MovieEntity::class)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = TvEntity::class)
    suspend fun insertTvShows(tvShows: List<TvEntity>)

    @Update(entity = MovieEntity::class)
    fun updateMovie(movie: MovieEntity)

    @Update(entity = TvEntity::class)
    fun updateTvShow(tvShows: TvEntity)
}