package com.example.abulm.mobileprogramming.service;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.abulm.mobileprogramming.api.LoginResource;
import com.example.abulm.mobileprogramming.localDatabase.AppDatabase;
import com.example.abulm.mobileprogramming.localDatabase.dao.UserDAO;
import com.example.abulm.mobileprogramming.model.UserDTO;
import com.example.abulm.mobileprogramming.utils.ComponentsUtils;
import com.example.abulm.mobileprogramming.utils.MyApp;
import com.example.abulm.mobileprogramming.utils.NetworkStatus;

import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginService extends Observable {

    private Boolean isLoggedIn = false;
    private AppDatabase offlineDatabase;
    private UserDAO userDAO;
    public static String token = "";
    public static Boolean offlineMode = false;

    public void initOfflineDatabase() {
        offlineDatabase = AppDatabase.getDatabase(MyApp.getContext());
        userDAO = offlineDatabase.userDAO();
    }

    public LoginService() {
        initOfflineDatabase();
    }

    public void login(final String username, final String password) {
        final Context context = MyApp.getContext();
        if(NetworkStatus.isNetworkAvailable()) {
            ComponentsUtils.startSpinner();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(LoginResource.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            LoginResource api = retrofit.create(LoginResource.class);
            Call<Void> call = api.login(new UserDTO(username, password));
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    ComponentsUtils.stopSpinner();
                    isLoggedIn = response.code() == 200;
                    if (isLoggedIn) {
                        Headers headers = response.headers();
                        token = headers.get("Authorization");
                        if (!verifyIfValidUser(username, password)) {
                            new InsertAsyncTask(userDAO).execute(new UserDTO(username, password));
                        }
                    }
                    setChanged();
                    notifyObservers(isLoggedIn);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    ComponentsUtils.stopSpinner();
                    Toast.makeText(context, "Server error! Starting app in offline mode.", Toast.LENGTH_LONG).show();
                    offlineModeLogin(username, password);
                }
            });
        }
        else{
            Toast.makeText(context, "Network error! Starting app in offline mode.", Toast.LENGTH_LONG).show();
            offlineModeLogin(username,password);
        }
    }

    private void offlineModeLogin(final String username, final String password){
        final Context context = MyApp.getContext();
        if(verifyIfValidUser(username,password)){
            ComponentsUtils.showOfflineModeTextView();
            offlineMode = true;
            isLoggedIn = true;
            setChanged();
            notifyObservers(isLoggedIn);
        }
        else{
            Toast.makeText(context,"Invalid username or password!",Toast.LENGTH_SHORT).show();
        }
    }

    private static class InsertAsyncTask extends AsyncTask<UserDTO, Void, Void> {

        private UserDAO mAsyncTaskDao;

        InsertAsyncTask(UserDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserDTO... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private Boolean verifyIfValidUser(final String username, final String password){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Integer> callable = () -> userDAO.checkIfValidLogin(username,password);
        Future<Integer> future = executor.submit(callable);
        try {
            return future.get().equals(1);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}


