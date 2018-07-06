package com.dmaso.android.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmaso.android.popularmovies.R;
import com.dmaso.android.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<Movie> movies;
    private LayoutInflater inflater;
    private Context mContext;
    private MovieSelectedListener mListener;
    public MovieAdapter(Context context, @Nullable final List<Movie> movies){
        inflater = LayoutInflater.from(context);
        mContext = context;
        this.movies = movies != null? movies : new ArrayList<Movie>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.poster_thumbnail) ImageView thumbnailView;
        @BindView(R.id.title_view)
        TextView titleView;
        public ViewHolder(final View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Movie current = movies.get(position);
        String uri = mContext.getString(R.string.tmdb_image_base)+ current.getPosterPath();
        Picasso.get().load(uri)
                .resize(300,400)
                .into(holder.thumbnailView);
            holder.thumbnailView.setOnClickListener((v) -> {
               mListener.onMovieSelected(v, position);
            });
         holder.titleView.setText(current.getTitle());
    }

    public interface MovieSelectedListener {
        void onMovieSelected(View view, int position);
    }

    public void setOnMovieSelectedListener(MovieSelectedListener listener){
        mListener = listener;
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }
}
