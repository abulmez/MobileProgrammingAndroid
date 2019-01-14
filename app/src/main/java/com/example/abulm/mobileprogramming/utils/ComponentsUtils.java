package com.example.abulm.mobileprogramming.utils;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ComponentsUtils {

    private ComponentsUtils(){}

    private static TextView offlineModeTextView;

    private static ProgressBar spinner;

    public static void setSpinner(ProgressBar spinner){
        ComponentsUtils.spinner = spinner;
    }

    public static void setOfflineModeTextView(TextView offlineModeTextView) {
        ComponentsUtils.offlineModeTextView = offlineModeTextView;
    }

    public static void startSpinner(){
        spinner.setVisibility(View.VISIBLE);
        spinner.bringToFront();
    }

    public static void stopSpinner(){
        spinner.setVisibility(View.GONE);
    }

    public static void hideOfflineModeTextView(){
        offlineModeTextView.setVisibility(View.GONE);
    }

    public static void showOfflineModeTextView() {
        offlineModeTextView.setVisibility(View.VISIBLE);
        offlineModeTextView.bringToFront();
    }
}
