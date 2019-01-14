package com.example.abulm.mobileprogramming.fragments.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abulm.mobileprogramming.R;
import com.example.abulm.mobileprogramming.fragments.DetailedMovieFragment;
import com.example.abulm.mobileprogramming.model.Movie;
import com.example.abulm.mobileprogramming.utils.MyApp;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private List<Movie> moviesList;
    private final View.OnClickListener onClickListener = new ListItemClickListener();
    private RecyclerView recyclerView;
    private AppCompatActivity parent;

    public MovieListAdapter(List<Movie> moviesList, RecyclerView recyclerView, AppCompatActivity parent) {
        this.moviesList = moviesList;
        this.recyclerView = recyclerView;
        this.parent = parent;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ConstraintLayout view = (ConstraintLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_layout, viewGroup, false);
        view.setOnClickListener(onClickListener);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        Movie currentMovie = moviesList.get(i);
        TextView titleTextView = (TextView) movieViewHolder.constraintLayout.getViewById(R.id.titleTextView);
        titleTextView.setText(currentMovie.getName());
        TextView releaseYearTextView = (TextView) movieViewHolder.constraintLayout.getViewById(R.id.releaseYearTextView);
        String releaseYearString = "Release year: " + currentMovie.getReleaseYear();
        releaseYearTextView.setText(releaseYearString);
        TextView ratingTextView = (TextView) movieViewHolder.constraintLayout.getViewById(R.id.ratingTextView);
        String ratingString = "Rating: " + currentMovie.getRating();
        ratingTextView.setText(ratingString);

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ConstraintLayout constraintLayout;
        public MovieViewHolder(ConstraintLayout v) {
            super(v);
            constraintLayout = v;
        }
    }

    class ListItemClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            DetailedMovieFragment detailedMovieFragment= DetailedMovieFragment.newInstance();
            detailedMovieFragment.setMainActivity(parent);
            detailedMovieFragment.setMovie(moviesList.get(itemPosition));
            parent.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, detailedMovieFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}


