package com.example.abulm.mobileprogramming.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abulm.mobileprogramming.MainActivity;
import com.example.abulm.mobileprogramming.R;
import com.example.abulm.mobileprogramming.model.Movie;
import com.example.abulm.mobileprogramming.service.MovieService;
import com.example.abulm.mobileprogramming.utils.MyApp;

import java.util.Observable;
import java.util.Observer;

public class DetailedMovieFragment extends Fragment implements Observer {

    private MovieService movieService;
    private EditText titleEditText, releaseYearEditText, ratingEditText;
    private Button updateButton, cancelButton;
    private AppCompatActivity mainActivity;
    private Movie movie;

    public static DetailedMovieFragment newInstance() {
        return new DetailedMovieFragment();
    }

    private Observer thisFragment = this;

    public void setMainActivity(AppCompatActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void setMovie(Movie movie){
        this.movie = movie;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.detailed_movie_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleEditText = view.findViewById(R.id.titleEditText);
        releaseYearEditText = view.findViewById(R.id.releaseYearEditText);
        ratingEditText = view.findViewById(R.id.ratingEditText);
        updateButton = view.findViewById(R.id.updateButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        movieService = new MovieService();
        movieService.addObserver(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        titleEditText.setText(movie.getName());
        releaseYearEditText.setText(movie.getReleaseYear());
        ratingEditText.setText(movie.getRating().toString());
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(titleEditText.getText().toString().equals("")){
                    Toast.makeText(MyApp.getContext(),"Title cannot be empty!",Toast.LENGTH_LONG).show();
                }
                else if(releaseYearEditText.getText().toString().equals("")){
                    Toast.makeText(MyApp.getContext(),"Release year cannot be empty!",Toast.LENGTH_LONG).show();
                }
                else if(ratingEditText.getText().toString().equals("")){
                    Toast.makeText(MyApp.getContext(),"Rating cannot be empty!",Toast.LENGTH_LONG).show();
                }
                else {
                    movie.setName(titleEditText.getText().toString());
                    movie.setReleaseYear(releaseYearEditText.getText().toString());
                    movie.setRating(Double.parseDouble(ratingEditText.getText().toString()));
                    movieService.updateMovie(movie);
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieService.deleteObserver(thisFragment);
                mainActivity.getSupportFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Boolean){
            Boolean response = (Boolean) arg;
            if(response) {
                Toast.makeText(MyApp.getContext(),"Movie updated successfully!",Toast.LENGTH_LONG).show();
                movieService.deleteObserver(thisFragment);
                mainActivity.getSupportFragmentManager().popBackStack();
            }
        }
    }
}
