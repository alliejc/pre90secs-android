package com.allie.pre90secs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FetchWorkoutFragment extends Fragment {

    private OnFetchWorkoutFragmentInteractionListener mListener;
    private Button mFetchWorkoutButton;

    public FetchWorkoutFragment() {
        // Required empty public constructor
    }

    public static FetchWorkoutFragment newInstance() {
        FetchWorkoutFragment fragment = new FetchWorkoutFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fetch_workout, container, false);

       mFetchWorkoutButton = (Button) v.findViewById(R.id.fetchWorkoutButton);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFetchWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fetchWorkout();
            }
        });
    }

    public void fetchWorkout() {
        if (mListener != null) {
            mListener.onFetchWorkoutFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFetchWorkoutFragmentInteractionListener) {
            mListener = (OnFetchWorkoutFragmentInteractionListener) context;
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

    public interface OnFetchWorkoutFragmentInteractionListener {
        void onFetchWorkoutFragmentInteraction();
    }
}
