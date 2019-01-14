package com.example.abulm.mobileprogramming.fragments;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.abulm.mobileprogramming.MainActivity;
import com.example.abulm.mobileprogramming.R;
import com.example.abulm.mobileprogramming.fragments.adapters.MovieListAdapter;
import com.example.abulm.mobileprogramming.model.Movie;
import com.example.abulm.mobileprogramming.utils.MyApp;
import com.example.abulm.mobileprogramming.service.viewModel.MoviesViewModel;

import java.util.List;

public class MovieListFragment extends Fragment {

    RecyclerView moviesRecyclerView;
    MoviesViewModel moviesViewModel;
    AppCompatActivity mainActivity;
    public boolean isResumed = false;

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }

    public void setMainActivity(AppCompatActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.movie_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        isResumed = false;
        super.onViewCreated(view, savedInstanceState);
        moviesRecyclerView = view.findViewById(R.id.moviesListRecyclerView);
        moviesRecyclerView.setHasFixedSize(true);
        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(MyApp.getContext()));
        moviesViewModel = new MoviesViewModel(this);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(moviesViewModel.isSyncNeeded!=null && moviesViewModel.isSyncNeeded){
            showSyncDialog();
        }
        isResumed = true;
    }

    public void showSyncDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Local data is different from the server data.");
        builder.setPositiveButton("Keep local data",
                (dialog, which) -> { })
        .setNegativeButton("Keep server data",
                (dialog, which) -> { })
        .setCancelable(false);
        final AlertDialog dialog = builder.create();

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            try {
                moviesViewModel.keepLocalData();
                reloadMovieList();
                dialog.dismiss();
            }
            catch (RuntimeException e){
                Toast.makeText(MyApp.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> {
            moviesViewModel.keepServerData();
            dialog.dismiss();
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reloadMovieList();
    }

    public void reloadMovieList(){
        moviesViewModel.getMovies().observe(this, movies -> {
            MovieListAdapter moviesAdapter = new MovieListAdapter(movies,moviesRecyclerView,mainActivity);
            moviesRecyclerView.setAdapter(moviesAdapter);
        });
    }

}
