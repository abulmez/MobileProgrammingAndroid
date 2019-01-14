package com.example.abulm.mobileprogramming;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.abulm.mobileprogramming.fragments.LoginFragment;
import com.example.abulm.mobileprogramming.utils.ComponentsUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialComponentsSetUp();

        if (savedInstanceState == null) {
            LoginFragment loginFragment = LoginFragment.newInstance();
            loginFragment.setMainActivity(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, loginFragment)
                    .commitNow();
        }
    }

    private void initialComponentsSetUp(){
        ProgressBar spinner = findViewById(R.id.progressBar);
        TextView offlineModeTextView = findViewById(R.id.offlineModeTextView);
        ComponentsUtils.setSpinner(spinner);
        ComponentsUtils.setOfflineModeTextView(offlineModeTextView);
    }


}
