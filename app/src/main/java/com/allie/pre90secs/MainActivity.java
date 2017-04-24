package com.allie.pre90secs;


import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements WorkoutFragment.OnWorkoutFragmentInteractionListener, FetchWorkoutFragment.OnFetchWorkoutFragmentInteractionListener {

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();

        manageFragmentStack();

    }

    private void manageFragmentStack() {
        FetchWorkoutFragment fragment = FetchWorkoutFragment.newInstance();
        mFragmentManager.beginTransaction().add(R.id.main_framelayout, fragment, "fragment").addToBackStack(null)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onWorkoutFragmentInteraction(Uri uri) {
        manageFragmentStack();
    }

    @Override
    public void onFetchWorkoutFragmentInteraction(Boolean fetchWorkout) {
        WorkoutFragment fragment = WorkoutFragment.newInstance();
        mFragmentManager.beginTransaction().add(R.id.main_framelayout, fragment, "fragment").addToBackStack(null)
                .addToBackStack(null)
                .commit();
    }
}
