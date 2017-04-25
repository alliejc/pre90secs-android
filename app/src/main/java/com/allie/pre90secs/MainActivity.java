package com.allie.pre90secs;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity implements WorkoutFragment.OnWorkoutFragmentInteractionListener, FetchWorkoutFragment.OnFetchWorkoutFragmentInteractionListener {

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            setContentView(R.layout.activity_main);

            mFragmentManager = getSupportFragmentManager();
            showInitialScreen();
        }

    }

//    private void manageFragmentStack(boolean loadWorkout) {
//
//            if (loadWorkout) {
//                mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                WorkoutFragment workoutFragment = WorkoutFragment.newInstance();
//                mFragmentManager.beginTransaction().add(R.id.main_framelayout, workoutFragment, "fragment").addToBackStack(null)
//                        .addToBackStack(null)
//                        .commit();
//            } else {
//                mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                FetchWorkoutFragment fetchWorkoutFragment = FetchWorkoutFragment.newInstance();
//                mFragmentManager.beginTransaction().add(R.id.main_framelayout, fetchWorkoutFragment, "fragment").addToBackStack(null)
//                        .addToBackStack(null)
//                        .commit();
//            }
//    }

    private void showInitialScreen() {
        FetchWorkoutFragment fetchWorkoutFragment = FetchWorkoutFragment.newInstance();
        mFragmentManager.beginTransaction().add(R.id.main_framelayout, fetchWorkoutFragment, "fragment").addToBackStack(null)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onWorkoutFragmentInteraction() {
//        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FetchWorkoutFragment fetchWorkoutFragment = FetchWorkoutFragment.newInstance();
        mFragmentManager.beginTransaction().add(R.id.main_framelayout, fetchWorkoutFragment, "fragment").addToBackStack(null)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onFetchWorkoutFragmentInteraction() {

//        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        WorkoutFragment workoutFragment = WorkoutFragment.newInstance();
        mFragmentManager.beginTransaction().add(R.id.main_framelayout, workoutFragment, "fragment").addToBackStack(null)
                .addToBackStack(null)
                .commit();
    }
}
