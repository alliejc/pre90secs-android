package com.allie.pre90secs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

public class WorkoutFragment extends Fragment {

    private ImageView imageView;
    private ImageView personImageView;

    private OnWorkoutFragmentInteractionListener mListener;

    public WorkoutFragment() {
        // Required empty public constructor
    }

    public static WorkoutFragment newInstance() {
        WorkoutFragment fragment = new WorkoutFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_workout, container, false);

        imageView = (ImageView) v.findViewById(R.id.imageView);
        personImageView = (ImageView) v.findViewById(R.id.personImageView);
        imageView.setImageResource(R.drawable.sample_workout_image);
        personImageView.setImageResource(R.drawable.wall_sit);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onWorkoutFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWorkoutFragmentInteractionListener) {
            mListener = (OnWorkoutFragmentInteractionListener) context;
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

    public interface OnWorkoutFragmentInteractionListener {
        // TODO: Update argument type and name
        void onWorkoutFragmentInteraction(Uri uri);
    }
}
