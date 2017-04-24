package com.allie.pre90secs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

public class WorkoutFragment extends Fragment {

    private ImageView imageView;
    private ImageView personImageView;
    private Button startButton;
    private Button workoutTimer;
    private Handler mTimerHandler = new Handler();
    private int mTotalTime = 0;

    private OnWorkoutFragmentInteractionListener mListener;

    public WorkoutFragment() {
        // Required empty public constructor
    }

    public static WorkoutFragment newInstance() {
        WorkoutFragment fragment = new WorkoutFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_workout, container, false);

        imageView = (ImageView) v.findViewById(R.id.imageView);
        personImageView = (ImageView) v.findViewById(R.id.personImageView);
        imageView.setImageResource(R.drawable.sample_workout_image);
        personImageView.setImageResource(R.drawable.wall_sit);
        startButton = (Button) v.findViewById(R.id.startButton);
        workoutTimer = (Button) v.findViewById(R.id.workoutTimer);

        return v;
    }

    private void updateTimerUi() {
        mTotalTime = mTotalTime + 1000;

        int mSeconds = 0;
        int mMinutes = 0;

        mSeconds = ((mTotalTime / 1000) % 60);
        mMinutes = ((mTotalTime / 1000) / 60);

        if(mTotalTime > 90000){
           stopTimer();
        }

        mTimerHandler.postDelayed(timerRunnable, 1000);
        workoutTimer.setText(String.format("%2d:%02d", mMinutes, mSeconds, 0));
    }

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            updateTimerUi();

        }
    };

    private void resetTimerHandler() {
        mTimerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });
    }

    private void startTimer() {
        workoutTimer.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.GONE);

        updateTimerUi();
    }

    private void stopTimer() {
        workoutTimer.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);

        resetTimerHandler();

        backToFetchScreen(true);
    }

    public void backToFetchScreen(boolean loadWorkout) {
        if (mListener != null) {
            mListener.onWorkoutFragmentInteraction(loadWorkout);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWorkoutFragmentInteractionListener) {
            mListener = (OnWorkoutFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        resetTimerHandler();
    }

    public interface OnWorkoutFragmentInteractionListener {
        void onWorkoutFragmentInteraction(boolean loadWorkout);
    }
}
