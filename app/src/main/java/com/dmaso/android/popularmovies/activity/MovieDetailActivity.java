package com.dmaso.android.popularmovies.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmaso.android.popularmovies.R;
import com.dmaso.android.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends Activity {
    private Movie mMovie;
    @BindView(R.id.movie_title_header)
    TextView titleHeader;
    @BindView(R.id.movie_poster)
    ImageView posterDetail;
    @BindView(R.id.release_value)
    TextView releaseDate;
    @BindView(R.id.rating_value)
    TextView rating;
    @BindView(R.id.synopsys_value)
    TextView synopsys;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        ButterKnife.bind(this);
        mMovie = getIntent().getParcelableExtra("parcel_data");
    }

    @Override
    protected void onResume() {
        super.onResume();
        titleHeader.setText(mMovie.getTitle());
        String uri = getString(R.string.tmdb_image_base)+ mMovie.getPosterPath();
        Picasso.get().load(uri)
                .resize(500,750)
                .into(posterDetail);
        releaseDate.setText(mMovie.getReleaseDate());
        rating.setText(String.format("%f",mMovie.getVoteAverage()));
        synopsys.setText(mMovie.getOverview());
    }
}
