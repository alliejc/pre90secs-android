package com.allie.pre90secs;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.allie.pre90secs.Data.ExerciseItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.googlecode.totallylazy.Sequences.sequence;


public class MainActivity extends AppCompatActivity implements WorkoutFragment.OnWorkoutWorkoutCompletedListener, FetchWorkoutFragment.OnFetchWorkoutFragmentInteractionListener, FilterOptionsFragment.OnFilterFragmentInteractionListener {

    private FragmentManager mFragmentManager;
    private Toolbar mToolbar;

    private List<ExerciseItem> mJsonList;

    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            setContentView(R.layout.activity_main);

            mFragmentManager = getSupportFragmentManager();
            addFragmentOnTop(FetchWorkoutFragment.newInstance());
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setupTabViews();
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
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public List<ExerciseItem> getExerciseItemsFromJson() {
        if(mJsonList == null) {
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

    private ExerciseItem getFilteredList(List<ExerciseItem> exerciseItemList) {
        return getRandomItem(sequence(exerciseItemList).filter(item -> item.getDifficulty().contains("hard")).toList());
    }

    private ExerciseItem getRandomItem(List<ExerciseItem> exerciseItemList) {
        int max = exerciseItemList.size();
        Random r = new Random();
        int random = r.nextInt(max);

        return exerciseItemList.get(random);
    }

    private void setupTabViews() {

        // Get the ViewPager and set it's PagerAdapter so that it can display items
       ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
       TabPagerAdapter mPagerAdapter =
                new TabPagerAdapter(getSupportFragmentManager(), MainActivity.this, 2);
        mViewPager.setAdapter(mPagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(mPagerAdapter.getTabView(i));
        }

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition() == 0){
                    addFragmentOnTop(FetchWorkoutFragment.newInstance());
                }
                if(tab.getPosition() == 1){
                    addFragmentOnTop(FilterOptionsFragment.newInstance());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void addFragmentOnTop(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fragmentManager
                .beginTransaction()
                .replace(R.id.main_framelayout, fragment)
                .addToBackStack(BACK_STACK_ROOT_TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
       if (mFragmentManager.getBackStackEntryCount() > 1) {
            mFragmentManager.popBackStackImmediate();

        } else if (mFragmentManager.getBackStackEntryCount() <= 1) {
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
        ExerciseItem exerciseItem = getFilteredList(getExerciseItemsFromJson());
        addFragmentOnTop(WorkoutFragment.newInstance(exerciseItem.getTitle(), exerciseItem.getImage(), exerciseItem.getInstructions()));
    }

    @Override
    public void onFilterFragmentInteraction(String difficulty, List bodyRegionChoices) {
        //apply filters to JSON query
    }
}
