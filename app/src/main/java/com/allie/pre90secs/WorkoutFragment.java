package com.allie.pre90secs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WorkoutFragment extends Fragment {

    private ImageView mImageView;
    private TextView mTitleView;
    private Button startButton;
    private Button workoutTimer;
    private Handler mTimerHandler = new Handler();
    private int mTotalTime = 0;
    private RecyclerView mRecyclerView;
    private List mInstructionList;
    private Boolean mBeepPlayed = false;

    private String titleParam;
    private String imageParam;
    private ArrayList instructionParam;

    private static final String ARG_TITLE = "title";
    private static final String ARG_IMAGE = "image";
    private static final String ARG_INSTRUCTIONS = "instructions";

    private OnWorkoutWorkoutCompletedListener mListener;

    public WorkoutFragment() {
        // Required empty public constructor
    }

    public static WorkoutFragment newInstance(String title, String image, List instructions) {
        WorkoutFragment fragment = new WorkoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_IMAGE, image);
        args.putStringArrayList(ARG_INSTRUCTIONS, new ArrayList<String>(instructions));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            titleParam = getArguments().getString(ARG_TITLE);
            imageParam = getArguments().getString(ARG_IMAGE);
            instructionParam = getArguments().getStringArrayList(ARG_INSTRUCTIONS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workout, container, false);

        mTitleView = (TextView) v.findViewById(R.id.title);
        mImageView = (ImageView) v.findViewById(R.id.workoutImage);
        mImageView.setImageResource(R.drawable.knee_high);
        startButton = (Button) v.findViewById(R.id.startButton);
        workoutTimer = (Button) v.findViewById(R.id.workoutTimer);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadUi();

        startButton.setOnClickListener(v -> startTimer());
    }

    private void setRecyclerView() {
        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.recycler_divider);
        RecyclerView.ItemDecoration dividerItemDecoration = new RecyclerDivider(dividerDrawable);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        CustomRecyclerViewAdapter mAdapter= new CustomRecyclerViewAdapter(mInstructionList, getContext());

        mRecyclerView.setAdapter(mAdapter);

    }

    private void loadUi() {
        mTitleView.setText(titleParam);
        mInstructionList = instructionParam;
        Resources resources = getContext().getResources();
        int resourceId = resources.getIdentifier(imageParam, "drawable", getContext().getPackageName());
        Drawable drawable = getContext().getDrawable(resourceId);
        mImageView.setImageDrawable(drawable);

        setRecyclerView();
    }

    private void updateTimerUi() {
        mTotalTime = mTotalTime + 1000;

        int mSeconds = 0;
        int mMinutes = 0;

        mSeconds = ((mTotalTime / 1000) % 60);
        mMinutes = ((mTotalTime / 1000) / 60);

        if(mTotalTime > 90000 && !mBeepPlayed){
            playBeep();
           stopTimer();
        }

        mTimerHandler.postDelayed(timerRunnable, 1000);
        workoutTimer.setText(String.format("%2d:%02d", mMinutes, mSeconds, 0));
    }

    Runnable timerRunnable = () -> updateTimerUi();

    private void playBeep() {

        final MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.beep);

        mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());

        mediaPlayer.setOnCompletionListener(mp -> {
            mBeepPlayed = true;
            mediaPlayer.release();
        });
    }

    private void resetTimerHandler() {
        mTimerHandler.removeCallbacks(timerRunnable);
    }

    private void startTimer() {
        workoutTimer.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.GONE);

        mBeepPlayed = false;
        updateTimerUi();
    }

    private void stopTimer() {
        workoutTimer.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);

        resetTimerHandler();
        backToFetchWorkoutScreen();
    }

    public void backToFetchWorkoutScreen() {
        if (mListener != null) {
            mListener.onWorkoutCompleted();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWorkoutWorkoutCompletedListener) {
            mListener = (OnWorkoutWorkoutCompletedListener) context;
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

    public interface OnWorkoutWorkoutCompletedListener {
        //call back to MainActivity on completed
        void onWorkoutCompleted();
    }
}
