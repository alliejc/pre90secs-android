package com.allie.pre90secs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.allie.pre90secs.Data.ExerciseItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.allie.pre90secs.R.id.list;

public class WorkoutFragment extends Fragment {

    private ImageView mImageView;
    private TextView mTitleView;
    private Button startButton;
    private Button workoutTimer;
    private Handler mTimerHandler = new Handler();
    private int mTotalTime = 0;
    private RecyclerView mRecyclerView;
    private CustomRecyclerViewAdapter mAdapter;
    private List mInstructionList;
    private LinearLayoutManager mLayoutManager;
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

    public static WorkoutFragment newInstance(String title, String image, ArrayList<Parcelable> instructions) {
        WorkoutFragment fragment = new WorkoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_IMAGE, image);
        args.putParcelableArrayList(ARG_INSTRUCTIONS, new ArrayList<>(instructions));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            titleParam = getArguments().getString(ARG_TITLE);
            imageParam = getArguments().getString(ARG_IMAGE);
            instructionParam = getArguments().getParcelableArrayList(ARG_INSTRUCTIONS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

        setupUi();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });
    }

    private void setupRecyclerView() {
        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.recycler_divider);
        RecyclerView.ItemDecoration dividerItemDecoration = new RecyclerDivider(dividerDrawable);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new CustomRecyclerViewAdapter(mInstructionList, getContext());

        mRecyclerView.setAdapter(mAdapter);

    }

    private void setupUi() {
        mTitleView.setText(titleParam);
        mInstructionList = instructionParam;
        Resources resources = getContext().getResources();
        int resourceId = resources.getIdentifier(imageParam, "drawable", getContext().getPackageName());
        Drawable drawable = getContext().getDrawable(resourceId);
        mImageView.setImageDrawable(drawable);

        setupRecyclerView();


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

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            updateTimerUi();

        }
    };

    private void playBeep() {

        final MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.beep);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mBeepPlayed = true;
                mediaPlayer.release();
            }
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
        void onWorkoutCompleted();

    }
}
