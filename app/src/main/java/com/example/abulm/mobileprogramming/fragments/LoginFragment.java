package com.example.abulm.mobileprogramming.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abulm.mobileprogramming.MainActivity;
import com.example.abulm.mobileprogramming.R;
import com.example.abulm.mobileprogramming.service.LoginService;
import com.example.abulm.mobileprogramming.utils.MyApp;

import java.util.Observable;
import java.util.Observer;

public class LoginFragment extends Fragment implements Observer {

    private Context context;
    private EditText usernameEditText,passwordEditText;
    private Button loginButton;
    private LoginService loginService;
    private MainActivity mainActivity;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public void setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        loginButton = view.findViewById(R.id.loginButton);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginService = new LoginService();
        loginService.addObserver(this);
        context = MyApp.getContext();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginService.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Boolean){
            Boolean response = (Boolean) arg;
            if(response){
                loginService.deleteObserver(this);
                MovieListFragment movieListFragment = MovieListFragment.newInstance();
                movieListFragment.setMainActivity(mainActivity);
                FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container,movieListFragment);
                transaction.commit();
            }
            else{
                Toast.makeText(context,"Invalid username or password!",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
