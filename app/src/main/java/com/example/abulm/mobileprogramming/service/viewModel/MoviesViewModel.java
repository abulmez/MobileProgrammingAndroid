package com.example.abulm.mobileprogramming.service.viewModel;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.abulm.mobileprogramming.api.MovieResource;
import com.example.abulm.mobileprogramming.fragments.MovieListFragment;
import com.example.abulm.mobileprogramming.localDatabase.AppDatabase;
import com.example.abulm.mobileprogramming.localDatabase.dao.MovieDAO;
import com.example.abulm.mobileprogramming.model.Movie;
import com.example.abulm.mobileprogramming.service.LoginService;
import com.example.abulm.mobileprogramming.service.MovieService;
import com.example.abulm.mobileprogramming.utils.MyApp;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesViewModel extends ViewModel {
    private MutableLiveData<List<Movie>> moviesList;
    private MutableLiveData<List<Movie>> localMoviesList;

    private AppDatabase offlineDatabase;
    private MovieDAO movieDAO;
    private MovieListFragment movieListFragment;
    public Boolean isSyncNeeded = false;

    public void initOfflineDatabase() {
        offlineDatabase = AppDatabase.getDatabase(MyApp.getContext());
        movieDAO = offlineDatabase.movieDAO();
    }

    public MoviesViewModel(MovieListFragment movieListFragment) {
        initOfflineDatabase();
        this.movieListFragment = movieListFragment;

    }

    public LiveData<List<Movie>> getMovies() {
        if (moviesList == null) {
            moviesList = new MutableLiveData<>();
            localMoviesList = new MutableLiveData<>();
            loadMovies();
        }
        return moviesList;
    }

    private void loadMovies() {

        if (!LoginService.offlineMode) {

            final Context context = MyApp.getContext();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MovieResource.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MovieResource api = retrofit.create(MovieResource.class);
            Call<List<Movie>> call = api.getMovies(LoginService.token);
            call.enqueue(new Callback<List<Movie>>() {
                @Override
                public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                    moviesList.setValue(response.body());
                    retrieveMoviesListFromLocalDB();
                    checkIfSyncNeeded();
                }

                @Override
                public void onFailure(Call<List<Movie>> call, Throwable t) {
                    Toast.makeText(context, "Server error!", Toast.LENGTH_LONG).show();
                }
            });

        } else {
            retrieveMoviesListFromLocalDB();
            moviesList = localMoviesList;
        }
    }

    private void checkIfSyncNeeded() {
        moviesList.observe(movieListFragment, remoteMovies -> localMoviesList.observe(movieListFragment, localMovies -> {
            if (remoteMovies == null && localMovies == null) {
                isSyncNeeded = false;
            } else if (remoteMovies == null || localMovies == null) {
                isSyncNeeded = true;
            } else if (remoteMovies.size() != localMovies.size()) {
                isSyncNeeded = true;
            } else {
                isSyncNeeded = false;
                for (int i = 0; i < remoteMovies.size(); i++) {
                    if (!remoteMovies.get(i).equals(localMovies.get(i))) {
                        isSyncNeeded = true;
                        break;
                    }
                }
            }
            if (movieListFragment.isResumed && isSyncNeeded) {
                movieListFragment.showSyncDialog();
            }
            localMoviesList.removeObservers(movieListFragment);
        }));
    }

    private void retrieveMoviesListFromLocalDB() {
        movieDAO.getAllMovies().observe(movieListFragment, (movies) -> {
            if (movies == null) {
                return;
            }
            localMoviesList.setValue(movies);
        });
    }

    public void keepLocalData() {
        localMoviesList.observe(movieListFragment, localMovies -> {
            MovieService.replaceMovieListServerRequest(localMovies);
            moviesList = localMoviesList;
        });
    }

    public void keepServerData() {
        moviesList.observe(movieListFragment, this::persistMoviesInLocalDatabase);
    }

    private static class InsertAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MovieDAO mAsyncTaskDao;

        InsertAsyncTask(MovieDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Movie... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class DeleteThread implements Runnable {

        private MovieDAO mThreadDao;

        public DeleteThread(MovieDAO dao) {
            mThreadDao = dao;
        }

        @Override
        public void run() {
            mThreadDao.deleteAll();
        }
    }

    private void persistMoviesInLocalDatabase(List<Movie> movies) {
        Thread thr = new Thread(new MoviesViewModel.DeleteThread(movieDAO));
        thr.start();
        try {
            thr.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Movie movie : movies) {
            new InsertAsyncTask(movieDAO).execute(movie);
        }
    }
}
