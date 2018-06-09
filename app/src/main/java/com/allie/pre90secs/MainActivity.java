package com.allie.pre90secs;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.allie.pre90secs.model.ExerciseItem;
import com.allie.pre90secs.fragment.FetchWorkoutFragment;
import com.allie.pre90secs.fragment.FilterOptionsFragment;
import com.allie.pre90secs.fragment.WorkoutFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.googlecode.totallylazy.Sequences.sequence;


public class MainActivity extends AppCompatActivity implements WorkoutFragment.OnWorkoutWorkoutCompletedListener, FetchWorkoutFragment.OnFetchWorkoutFragmentInteractionListener, FilterOptionsFragment.OnFilterFragmentInteractionListener {

    private FragmentManager mFragmentManager;
    private ActionBar mActionBar;
    private SharedPreferences myPrefs;
    private Set<String> bodyRegionDefault = new HashSet<String>();
    public static final String PREFS_FILE = "MyPrefsFile";
    private String difficultyDefault = "easy";
    private Boolean limitedSpaceDefault = false;
    private static List<ExerciseItem> mJsonList;

    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    private static final String ROOT = "root";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            setContentView(R.layout.activity_main);

            mFragmentManager = getFragmentManager();
            mFragmentManager.beginTransaction().replace(R.id.main_framelayout, FetchWorkoutFragment.newInstance()).addToBackStack(ROOT).commit();
        }
        myPrefs = getApplicationContext().getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        bodyRegionDefault.add("whole");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(R.string.app_name);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                addFragmentOnTop(FilterOptionsFragment.newInstance());
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public List<ExerciseItem> getExerciseItemsFromJson() {
        if (mJsonList == null) {
            String json = null;
            try {
                InputStream is = MainActivity.this.getAssets().open("ExerciseObject.json");
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
            Type type = new TypeToken<List<ExerciseItem>>() {
            }.getType();
            mJsonList = gson.fromJson(json, type);

            for (ExerciseItem exerciseItem : mJsonList) {
                Log.d("List", mJsonList.toString());
                Log.i("Workout Details", exerciseItem.getTitle() + exerciseItem.getImage());
            }
        }
        return mJsonList;
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

    private List<ExerciseItem> getFilteredList() {

        //current body region setting list
        List <String> list = new ArrayList<String>(myPrefs.getStringSet("body_region", bodyRegionDefault));

        //current difficulty setting
        String difficulty = myPrefs.getString("difficulty", difficultyDefault);

        //current space setting
        Boolean limitedSpace = myPrefs.getBoolean("limited_space", limitedSpaceDefault);

        List<ExerciseItem> exerciseItems = sequence(getExerciseItemsFromJson())
                .filter(item -> item.getSpace().equals(limitedSpace))
                .filter(item -> this.intersects(item.getBodyRegion(), list))
                .filter(item -> item.getDifficulty().contains(difficulty))
                .toList();

        return exerciseItems;
    }

    private ExerciseItem getRandomItem(List<ExerciseItem> exerciseItemList) {
        int max = exerciseItemList.size();
        Random r = new Random();
        int random = r.nextInt(max);

        return exerciseItemList.get(random);
    }

    public void addFragmentOnTop(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fragmentManager
                .beginTransaction()
                .replace(R.id.main_framelayout, fragment)
                .addToBackStack(BACK_STACK_ROOT_TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() >= 1) {
            mFragmentManager.popBackStackImmediate();

        } else if (mFragmentManager.getBackStackEntryCount() < 1) {
            moveTaskToBack(true);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onWorkoutCompleted() {
        addFragmentOnTop(FetchWorkoutFragment.newInstance());
    }

    @Override
    public void onFetchWorkoutFragmentInteraction() {

        ExerciseItem exerciseItem = getRandomItem(getFilteredList());
        addFragmentOnTop(WorkoutFragment.newInstance(exerciseItem.getTitle(), exerciseItem.getImage(), exerciseItem.getInstructions()));
    }

    @Override
    public void onFilterFragmentInteraction() {
    }

}
