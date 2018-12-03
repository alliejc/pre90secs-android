package com.allie.pre90secs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.allie.pre90secs.R;

public class FetchWorkoutFragment extends Fragment {

    private OnFetchWorkoutFragmentInteractionListener fetchWorkoutListener;
    private OnAddWorkoutInteractionListener addWorkoutListener;
    private Button mFetchWorkoutButton;
    private FloatingActionButton fab;

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

        fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWorkoutListener.onAddWorkoutInteraction();
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFetchWorkoutButton.setOnClickListener(v -> fetchWorkout());
    }

    public void fetchWorkout() {
        if (fetchWorkoutListener != null) {
            fetchWorkoutListener.onFetchWorkoutFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFetchWorkoutFragmentInteractionListener) {
            fetchWorkoutListener = (OnFetchWorkoutFragmentInteractionListener) context;
            addWorkoutListener = (OnAddWorkoutInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fetchWorkoutListener = null;
        addWorkoutListener = null;
    }

    public interface OnFetchWorkoutFragmentInteractionListener {
        void onFetchWorkoutFragmentInteraction();
    }

    public interface OnAddWorkoutInteractionListener {
        void onAddWorkoutInteraction();
    }
}
