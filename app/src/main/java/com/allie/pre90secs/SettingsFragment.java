//package com.allie.pre90secs;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v7.preference.CheckBoxPreference;
//import android.support.v7.preference.ListPreference;
//import android.support.v7.preference.Preference;
//import android.support.v7.preference.PreferenceCategory;
//import android.support.v7.preference.PreferenceFragmentCompat;
//import android.support.v7.preference.PreferenceManager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
//
//    private OnFragmentInteractionListener mListener;
//    private static final String TAG = SettingsFragment.class.getSimpleName();
//    private ListPreference mListPreference;
//    private Preference.OnPreferenceChangeListener preListener;
//
//    SharedPreferences sharedPreferences;
//
//
//    public SettingsFragment() {
//        // Required empty public constructor
//    }
//
//    public static SettingsFragment newInstance() {
//        SettingsFragment fragment = new SettingsFragment();
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        addPreferencesFromResource(R.xml.settings);
//        getPreferenceManager().createPreferenceScreen(getContext());
//
//    }
//
//
//    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
////        setPreferencesFromResource(R.xml.settings, rootKey);
//
//        mListPreference = (ListPreference) getPreferenceManager().findPreference("pref_key_difficulty_list");
//        mListPreference.setOnPreferenceChangeListener((Preference preference, Object newValue) -> {
//            onSharedPreferenceChanged(sharedPreferences, "pref_key_difficulty_list");
//            return true;
//        });
//
//        preListener = (preference, newValue) -> true;
//
//
//        CheckBoxPreference upperCheck = (CheckBoxPreference) findPreference("pref_title_upper_body");
//        upperCheck.setOnPreferenceChangeListener(preListener);
////        CheckBoxPreference lowerCheck = (CheckBoxPreference) findPreference("pref_title_lower_body");
////        lowerCheck.setOnPreferenceChangeListener(preListener);
////        CheckBoxPreference coreCheck = (CheckBoxPreference) findPreference("pref_title_core");
////        coreCheck.setOnPreferenceChangeListener(preListener);
////        CheckBoxPreference wholeCheck = (CheckBoxPreference) findPreference("pref_title_whole_body");
////        wholeCheck.setOnPreferenceChangeListener(preListener);
////        CheckBoxPreference cardioCheck = (CheckBoxPreference) findPreference("pref_title_cardio_body");
////        cardioCheck.setOnPreferenceChangeListener(preListener);
//
//        onSharedPreferenceChanged(sharedPreferences, "pref_key_body_region");
//        onSharedPreferenceChanged(sharedPreferences, "pref_title_upper_body");
//        onSharedPreferenceChanged(sharedPreferences, "pref_title_lower_body");
//        onSharedPreferenceChanged(sharedPreferences, "pref_title_whole_body");
//        onSharedPreferenceChanged(sharedPreferences, "pref_title_core");
//        onSharedPreferenceChanged(sharedPreferences, "pref_title_cardio");
//
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        //unregister the preferenceChange listener
//        getPreferenceScreen().getSharedPreferences()
//                .registerOnSharedPreferenceChangeListener(this);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        //unregister the preference change listener
//        getPreferenceScreen().getSharedPreferences()
//                .unregisterOnSharedPreferenceChangeListener(this);
//    }
//
//
//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        Preference preference = findPreference(key);
//        if (preference instanceof ListPreference) {
//            ListPreference listPreference = (ListPreference) preference;
//            int prefIndex = listPreference.findIndexOfValue(sharedPreferences.getString(key, ""));
//            if (prefIndex >= 0) {
//                preference.setSummary(listPreference.getEntries()[prefIndex]);
//            }
//        } else {
////                preference.setSummary(sharedPreferences.getString(key, ""));
//            getPreferenceManager().findPreference(key);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//}
