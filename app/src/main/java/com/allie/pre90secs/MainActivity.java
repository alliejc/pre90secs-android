package com.allie.pre90secs;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.allie.pre90secs.fragment.AddCustomWorkoutFragment;
import com.allie.pre90secs.fragment.FetchWorkoutFragment;
import com.allie.pre90secs.fragment.FilterOptionsFragment;
import com.allie.pre90secs.fragment.WorkoutFragment;

public class MainActivity extends AppCompatActivity implements WorkoutFragment.OnWorkoutWorkoutCompletedListener, FetchWorkoutFragment.OnFetchWorkoutFragmentInteractionListener, FilterOptionsFragment.OnFilterFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    private static final String ROOT = "root";
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            setContentView(R.layout.activity_main);

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_framelayout, FetchWorkoutFragment.newInstance()).addToBackStack(ROOT).commit();
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.app_name);
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setHomeButtonEnabled(true);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragmentOnTop(new AddCustomWorkoutFragment());
            }
        });
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
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() >= 1) {
            fragmentManager.popBackStackImmediate();

        } else if (fragmentManager.getBackStackEntryCount() < 1) {
            moveTaskToBack(true);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        fab.setVisibility(View.GONE);
    }

    @Override
    public void onWorkoutCompleted() {
        addFragmentOnTop(FetchWorkoutFragment.newInstance());
    }

    @Override
    public void onFetchWorkoutFragmentInteraction() {
        addFragmentOnTop(WorkoutFragment.newInstance());
    }

    @Override
    public void onFilterFragmentInteraction() {
    }

}
