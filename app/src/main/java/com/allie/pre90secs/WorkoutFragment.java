package com.allie.pre90secs;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.allie.pre90secs.R.attr.title;

public class WorkoutFragment extends Fragment {

    private ImageView imageView;
    private TextView mTitle;
    private Button startButton;
    private Button workoutTimer;
    private Handler mTimerHandler = new Handler();
    private int mTotalTime = 0;
    private RecyclerView mRecyclerView;
    private CustomRecyclerViewAdapter mAdapter;
    private List mList;
    private LinearLayoutManager mLayoutManager;

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

        mTitle = (TextView) v.findViewById(R.id.title);
        imageView = (ImageView) v.findViewById(R.id.workoutImage);
        imageView.setImageResource(R.drawable.knee_high);
        startButton = (Button) v.findViewById(R.id.startButton);
        workoutTimer = (Button) v.findViewById(R.id.workoutTimer);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();

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

        mList = loadJSONFromAsset();

//        mTitle.setText("KNEE HIGH");
//        mList.add(0, "Find an open area in the room without any items on the floor around you");
//        mList.add(1, "Make sure the ceiling is high enough to jump without hitting");
//        mList.add(2, "Jump, tucking each knee to your chest");
//        mList.add(3, "Repeat");

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new CustomRecyclerViewAdapter(mList, getContext());

        mRecyclerView.setAdapter(mAdapter);

    }

    public List loadJSONFromAsset() {
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
        for (ExerciseItem exerciseItem : exerciseItemList){
            Log.i("Workout Details", exerciseItem.getTitle() + "-" + exerciseItem.getPhoto());
        }
        return  exerciseItemList;
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

    private void startTimer() {
        workoutTimer.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.GONE);

        updateTimerUi();
    }

    private void stopTimer() {
        workoutTimer.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);

        resetTimerHandler();

        backToFetchScreen(false);
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
