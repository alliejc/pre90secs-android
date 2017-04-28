package com.allie.pre90secs;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.RadialGradient;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FilterOptionsFragment extends Fragment {

    private OnFilterFragmentInteractionListener mListener;
    private RadioGroup mDifficultyRadioGroup;
    private CheckBox mUpperBodyCheck;
    private CheckBox mLowerBodyCheck;
    private CheckBox mCoreCheck;
    private CheckBox mCardioCheck;
    private CheckBox mWholeBodyCheck;
    private SharedPreferences.Editor editor;

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

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filter_options, container, false);
        mDifficultyRadioGroup = (RadioGroup) v.findViewById(R.id.difficultyRadioGroup);
        mUpperBodyCheck = (CheckBox) v.findViewById(R.id.upperRegionCheckBox);
        mLowerBodyCheck = (CheckBox) v.findViewById(R.id.lowerRegionCheckBox);
        mWholeBodyCheck = (CheckBox) v.findViewById(R.id.wholeBodyRegionCheckBox);
        mCardioCheck = (CheckBox) v.findViewById(R.id.cardioRegionCheckBox);
        mCoreCheck = (CheckBox) v.findViewById(R.id.coreRegionCheckBox);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRadioButtons();
        setupCheckBoxes();
    }

    private void setupRadioButtons() {
        mDifficultyRadioGroup.setOnCheckedChangeListener((group, checkedId) -> editor.putInt("difficulty", checkedId).commit());
    }

    private void setupCheckBoxes() {
        mUpperBodyCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("upper_body", isChecked);
        });
        mLowerBodyCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("lower_body", isChecked);
        });
        mWholeBodyCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("whole_body", isChecked);
        });
        mCardioCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("cardio", isChecked);
        });
        mCoreCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("core", isChecked);
        });
    }

    public void onButtonPressed(String difficulty, List bodyRegionChoices) {
        if (mListener != null) {
            mListener.onFilterFragmentInteraction(difficulty, bodyRegionChoices);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFilterFragmentInteractionListener {
        void onFilterFragmentInteraction(String difficulty, List bodyRegionChoices);
    }
}
