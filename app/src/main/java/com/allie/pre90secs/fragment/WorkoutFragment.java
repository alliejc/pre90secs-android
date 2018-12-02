package com.allie.pre90secs.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.allie.pre90secs.service.ExerciseService;
import com.allie.pre90secs.R;
import com.allie.pre90secs.adapter.InstructionAdapter;
import com.allie.pre90secs.model.ExerciseItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.googlecode.totallylazy.Sequences.sequence;

public class WorkoutFragment extends Fragment {
    private static final String TAG = WorkoutFragment.class.getSimpleName();

    private final String BASE_URL_EXERCISE_ITEMS = "https://raw.githubusercontent.com/alliejc/alliejc.github.io/master/";
    private final String BASE_URL_EXERCISE_IMAGE = "https://alliejc.github.io/img/";

    private ImageView mImageView;
    private TextView mTitleView;
    private Button startButton;
    private Button workoutTimer;
    private Handler mTimerHandler = new Handler();
    private int mTotalTime = 0;
    private RecyclerView mRecyclerView;
    private Boolean mBeepPlayed = false;

    private ExerciseItem selectedItem;
    private SharedPreferences myPrefs;
    private Set<String> bodyRegionDefault = new HashSet<String>();
    public static final String PREFS_FILE = "MyPrefsFile";
    private String difficultyDefault = "easy";
    private Boolean limitedSpaceDefault = false;

    private OnWorkoutWorkoutCompletedListener mListener;

    public WorkoutFragment() {
        // Required empty public constructor
    }

    public static WorkoutFragment newInstance() {
        return new WorkoutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPrefs = this.getActivity().getApplicationContext().getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        bodyRegionDefault.add("whole");
        getExerciseItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workout, container, false);

        mTitleView = (TextView) v.findViewById(R.id.title);
        mImageView = (ImageView) v.findViewById(R.id.workoutImage);
        startButton = (Button) v.findViewById(R.id.startButton);
        workoutTimer = (Button) v.findViewById(R.id.workoutTimer);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startButton.setOnClickListener(v -> startTimer());
    }

    private void setRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        InstructionAdapter mAdapter= new InstructionAdapter(selectedItem.getInstructions(), getContext());

        mRecyclerView.setAdapter(mAdapter);

    }

    public void getExerciseItems() {
        ExerciseService service = new ExerciseService(BASE_URL_EXERCISE_ITEMS);
        service.getExerciseItemList().enqueue(new Callback<List<ExerciseItem>>() {
            @Override
            public void onResponse(Call<List<ExerciseItem>> call, Response<List<ExerciseItem>> response) {
                if (response.isSuccessful() && response.body() != null){
                    selectedItem = getRandomItem(getFilteredList(response.body()));
                    if (selectedItem != null) {
                        if (selectedItem.getTitle() != null) {
                            mTitleView.setText(selectedItem.getTitle());
                        }

                        setRecyclerView();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ExerciseItem>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private List<ExerciseItem> getFilteredList(List<ExerciseItem> items) {

        //current body region setting list
        List <String> list = new ArrayList<String>(myPrefs.getStringSet("body_region", bodyRegionDefault));

        //current difficulty setting
        String difficulty = myPrefs.getString("difficulty", difficultyDefault);

        //current space setting
        Boolean limitedSpace = myPrefs.getBoolean("limited_space", limitedSpaceDefault);

        List<ExerciseItem> exerciseItems = sequence(items)
                .filter(item -> item.getSpace().equals(limitedSpace))
                .filter(item -> this.intersects(item.getBodyRegion(), list))
                .filter(item -> item.getDifficulty().contains(difficulty))
                .toList();

        return exerciseItems;
    }

    private ExerciseItem getRandomItem(List<ExerciseItem> exerciseItemList) {
        if (exerciseItemList != null && !exerciseItemList.isEmpty()) {
            int max = exerciseItemList.size();
            Random r = new Random();
            int random = r.nextInt(max);

            ExerciseItem item = exerciseItemList.get(random);
            Picasso.get().load(Uri.parse(BASE_URL_EXERCISE_IMAGE + item.getImage())).into(mImageView);

            return item;
        }
        return null;
    }

    private Boolean intersects(List regions, List selectedRegions) {
        //some logic to check if they exist
        Set<String> allRegions = new HashSet<String>(regions);
        Set<String> sRegions = new HashSet<String>(selectedRegions);
        Set<String> concat = new HashSet<String>();

        concat.addAll(allRegions);
        concat.addAll(sRegions);

        return concat.size() != 0;
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
