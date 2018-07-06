package com.dmaso.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dmaso.android.popularmovies.activity.MovieDetailActivity;
import com.dmaso.android.popularmovies.adapters.MovieAdapter;
import com.dmaso.android.popularmovies.models.GetMoviesResult;
import com.dmaso.android.popularmovies.models.Movie;
import com.dmaso.android.popularmovies.service.MovieFetchingService;
import com.dmaso.android.popularmovies.service.TmdbInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieSelectedListener {
    TmdbInterface tmdb;
    @BindView(R.id.poster_grid)
    RecyclerView posterGrid;
    @BindView(R.id.toolbar) Toolbar toolbar;
    private SharedPreferences preferences;
    private String mSortBy;
    private static final String PREFERENCES_NAME = "popularMovies";
    private static final String MOVIE_SORT_KEY = "sortBy";
    final List<Movie> mMovieList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        posterGrid.setLayoutManager(new GridLayoutManager(this, 3));
        tmdb = MovieFetchingService.getTmdbClient().create(TmdbInterface.class);
        preferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume(){
        super.onResume();
        fetchMovies();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences.Editor editor = preferences.edit();
        int id = item.getItemId();
        switch (id){
            case R.id.popularity:
                editor.putString(MOVIE_SORT_KEY, TmdbInterface.SortConstants.POPULARITY);
                break;
            case R.id.top_rated:
                editor.putString(MOVIE_SORT_KEY, TmdbInterface.SortConstants.HIGHEST_RATED);
                break;
                default:
                    return false;

        }
        editor.apply();
        fetchMovies();
        return true;
    }

    private void fetchMovies(){
        Log.v(this.getClass().getSimpleName(),"fetching movies...");
        mSortBy = preferences.getString(MOVIE_SORT_KEY,TmdbInterface.SortConstants.HIGHEST_RATED);
        Call<GetMoviesResult> getMovies = mSortBy.equals(TmdbInterface.SortConstants.HIGHEST_RATED)?
                tmdb.getTopRatedMovies(BuildConfig.API_KEY): tmdb.getPopularMovies(BuildConfig.API_KEY);
        final MainActivity activity = this;
        final Comparator<Movie> popularity = (Movie m1, Movie m2) -> {
          return m2.getPopularity().compareTo(m1.getPopularity());
        };
        final Comparator<Movie> topRated = (Movie m1, Movie m2) -> {
          return  m2.getVoteAverage().compareTo(m1.getVoteAverage());
        };
        getMovies.enqueue(new Callback<GetMoviesResult>() {
            @Override
            public void onResponse(Call<GetMoviesResult> call, Response<GetMoviesResult> response) {
                GetMoviesResult result = response.body();
                List<Movie> resultMovies = result.getResults();
               Collections.sort(resultMovies,
                        mSortBy.equals(TmdbInterface.SortConstants.HIGHEST_RATED)? topRated :
                        popularity);
               mMovieList.clear();
               mMovieList.addAll(resultMovies);
                MovieAdapter adapter = new MovieAdapter(getApplicationContext(), mMovieList);
                adapter.setOnMovieSelectedListener(activity);
                posterGrid.swapAdapter(adapter, true);
            }

            @Override
            public void onFailure(Call<GetMoviesResult> call, Throwable t) {
                Log.e("We failed at calling", t.getMessage());
            }
        });
    }

    @Override
    public void onMovieSelected(View view, int position) {
        Intent detailIntent = new Intent(this, MovieDetailActivity.class);
        detailIntent.putExtra("parcel_data",  mMovieList.get(position));
        startActivity(detailIntent);
    }
}
