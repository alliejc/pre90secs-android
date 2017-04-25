package com.allie.pre90secs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.List;
import java.util.Random;

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

        ExerciseItem itemToDisplay = loadJSONFromAsset();
        mTitleView.setText(itemToDisplay.getTitle());
        mInstructionList = itemToDisplay.getInstructions();

        String image = itemToDisplay.getImage();
        Resources resources = getContext().getResources();

        int resourceId = resources.getIdentifier(image, "drawable", getContext().getPackageName());
        Drawable drawable = resources.getDrawable(resourceId);

        mImageView.setImageDrawable(drawable);
        setupRecyclerView();

    }

    public ExerciseItem loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("ExerciseObject.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<ExerciseItem>>(){}.getType();
        List<ExerciseItem> exerciseItemList = gson.fromJson(json, type);

        for (ExerciseItem exerciseItem : exerciseItemList) {
            Log.i("Workout Details", exerciseItem.getTitle() + exerciseItem.getImage());
        }

        return  getRandomItem(exerciseItemList);
    }

    private ExerciseItem getRandomItem(List<ExerciseItem> exerciseItemList) {
        int max = exerciseItemList.size();
        Random r = new Random();
        int random = r.nextInt(max);

        return exerciseItemList.get(random);
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

        backToFetchScreen();
    }

    public void backToFetchScreen() {
        if (mListener != null) {
            mListener.onWorkoutFragmentInteraction();
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
        void onWorkoutFragmentInteraction();
    }
}
