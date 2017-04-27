package com.allie.pre90secs;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class FilterOptionsFragment extends Fragment {

    private OnFilterFragmentInteractionListener mListener;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_options, container, false);
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
