package com.dmaso.android.popularmovies.service;

import com.dmaso.android.popularmovies.models.GetMoviesResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbInterface {
    class SortConstants {
        public static final String POPULARITY = "popularity.dsc";
        public static final String HIGHEST_RATED = "vote_average.dsc";

    }
    @GET("movie/popular")
    Call<GetMoviesResult> getPopularMovies(@Query("api_key") String apiKey);
    @GET("movie/top_rated")
    Call<GetMoviesResult> getTopRatedMovies(@Query("api_key") String apiKey);


}
