package com.allie.pre90secs;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements WorkoutFragment.OnWorkoutFragmentInteractionListener, FetchWorkoutFragment.OnFetchWorkoutFragmentInteractionListener {

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();
        showInitialScreen(false);
    }

    private void manageFragmentStack(boolean loadWorkout) {

            if (loadWorkout) {
//                mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                WorkoutFragment workoutFragment = WorkoutFragment.newInstance();
                mFragmentManager.beginTransaction().add(R.id.main_framelayout, workoutFragment, "fragment").addToBackStack(null)
                        .addToBackStack(null)
                        .commit();
            } else {
//                mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FetchWorkoutFragment fetchWorkoutFragment = FetchWorkoutFragment.newInstance();
                mFragmentManager.beginTransaction().add(R.id.main_framelayout, fetchWorkoutFragment, "fragment").addToBackStack(null)
                        .addToBackStack(null)
                        .commit();
            }
    }

    private void showInitialScreen(boolean loadWorkout) {
        manageFragmentStack(loadWorkout);
    }

    @Override
    public void onWorkoutFragmentInteraction(boolean loadWorkout) {

        manageFragmentStack(loadWorkout);
    }

    @Override
    public void onFetchWorkoutFragmentInteraction(boolean loadWorkout) {

        manageFragmentStack(loadWorkout);
    }
}
