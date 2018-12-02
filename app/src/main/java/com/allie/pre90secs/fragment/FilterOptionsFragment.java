package com.allie.pre90secs.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.allie.pre90secs.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterOptionsFragment extends Fragment {

    private OnFilterFragmentInteractionListener mListener;
    private RadioGroup mDifficultyRadioGroup;
    private CheckBox mUpperBodyCheck;
    private CheckBox mLowerBodyCheck;
    private CheckBox mCoreCheck;
    private CheckBox mCardioCheck;
    private CheckBox mWholeBodyCheck;
    private CheckBox mLimitedSpace;
    private Button mSaveButton;
    private RadioButton mEasyButton;
    private RadioButton mMediumButton;
    private RadioButton mHardButton;
    private SharedPreferences.Editor editor;
    private Set<String> bodyRegions = new HashSet<String>();
    private Set<String> bodyRegionDefault = new HashSet<String>();
    private String difficultyDefault = "easy";
    private String difficulty;
    private Boolean limitedSpace;
    private Boolean limitedSpaceDefault = false;
    private SharedPreferences pref;

    public static final String PREFS_FILE = "MyPrefsFile";


    public FilterOptionsFragment() {
        // Required empty public constructor
    }

    public static FilterOptionsFragment newInstance() {
        FilterOptionsFragment fragment = new FilterOptionsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeSharedPreferences();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_filter_options, container, false);
        mDifficultyRadioGroup = (RadioGroup) v.findViewById(R.id.difficultyRadioGroup);
        mEasyButton = (RadioButton) v.findViewById(R.id.easyRadioButton);
        mMediumButton = (RadioButton) v.findViewById(R.id.mediumRadioButton);
        mHardButton = (RadioButton) v.findViewById(R.id.hardRadioButton);
        mUpperBodyCheck = (CheckBox) v.findViewById(R.id.upperRegionCheckBox);
        mLowerBodyCheck = (CheckBox) v.findViewById(R.id.lowerRegionCheckBox);
        mWholeBodyCheck = (CheckBox) v.findViewById(R.id.wholeBodyRegionCheckBox);
        mCardioCheck = (CheckBox) v.findViewById(R.id.cardioRegionCheckBox);
        mCoreCheck = (CheckBox) v.findViewById(R.id.coreRegionCheckBox);
        mLimitedSpace = (CheckBox) v.findViewById(R.id.limitedSpaceCheckBox);
        mSaveButton = (Button) v.findViewById(R.id.save_button);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSaveButton.setOnClickListener(v -> onSaveClicked());
        setupRadioButtons();
        setCheckBoxChangeListeners();

        List<String> list = new ArrayList<String>(pref.getStringSet("body_region", bodyRegionDefault));
        String diff = pref.getString("difficulty", difficultyDefault);
        Boolean b = pref.getBoolean("limited_space", limitedSpaceDefault);
        updateCheckBoxUi(list);
        updateRadioButtonUi(diff);
        setLimitedSpace(b);
    }

    private void updateRadioButtonUi(String difficulty) {
        if(difficulty.equals("easy")){
            mEasyButton.setChecked(true);
        }
        if(difficulty.equals("medium")){
            mMediumButton.setChecked(true);
        }
        if(difficulty.contains("hard")){
            mHardButton.setChecked(true);
        }
    }

    private void initializeSharedPreferences() {
        bodyRegionDefault.add("whole");

        pref = getActivity().getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        difficulty = pref.getString("difficulty", difficultyDefault);
        bodyRegions = pref.getStringSet("body_region", bodyRegionDefault);
        limitedSpace = pref.getBoolean("limited_space", limitedSpaceDefault);

        setDifficulty(difficulty);
//        setLimitedSpace(limitedSpace);
    }

    private void onSaveClicked() {

        pref.edit().putStringSet("body_region", bodyRegions).apply();
        pref.edit().putString("difficulty", difficulty).apply();
        pref.edit().putBoolean("limited_space", limitedSpace).apply();
    }

    public void onRadioButtonClicked(int id) {

        switch (id) {
            case R.id.easyRadioButton:
                setDifficulty("easy");
                break;
            case R.id.mediumRadioButton:
                setDifficulty("medium");
                break;
            case R.id.hardRadioButton:
               setDifficulty("hard");
        }
        }

    private void setDifficulty(String name) {
        difficulty = name;
    }

    private void setLimitedSpace(Boolean b){
        limitedSpace = b;
        if(b){
            mLimitedSpace.setChecked(true);
        } else {
            mLimitedSpace.setChecked(false);
        }
    }

    private void setupRadioButtons() {
        mDifficultyRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            onRadioButtonClicked(checkedId);
        });
    }

    private void setCheckBoxChangeListeners() {
        mUpperBodyCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setCheckBox(isChecked, "upper");
        });
        mLowerBodyCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setCheckBox(isChecked, "lower");
        });
        mWholeBodyCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setCheckBox(isChecked, "whole");
        });
        mCardioCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setCheckBox(isChecked, "cardio");
        });
        mCoreCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
           setCheckBox(isChecked, "core");
        });
        mLimitedSpace.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setLimitedSpace(isChecked);
        });
    }

    private void updateCheckBoxUi(List list){
        if(list.contains("lower")){
            mLowerBodyCheck.setChecked(true);
        }
        if(list.contains("upper")){
            mUpperBodyCheck.setChecked(true);
        }
        if(list.contains("core")){
            mCoreCheck.setChecked(true);
        }
        if(list.contains("cardio")){
            mCardioCheck.setChecked(true);
        }
        if(list.contains("whole")){
            mWholeBodyCheck.setChecked(true);
        }
    }

    private void setCheckBox(boolean isChecked, String name) {
        if(isChecked) {
            bodyRegions.add(name);
        } else {
            bodyRegions.remove(name);
        }
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFilterFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFilterFragmentInteractionListener) {
            mListener = (OnFilterFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeSharedPreferences();
    }

    @Override
    public void onPause() {
        super.onPause();
        onSaveClicked();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFilterFragmentInteractionListener {
        void onFilterFragmentInteraction();
    }
}
