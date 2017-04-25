package com.allie.pre90secs;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allie.pre90secs.Data.ExerciseItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.CustomViewHolder> {

//    private List mList;
    private List<ExerciseItem> mList;
    private Array instructionList;
    private Context mContext;

    public CustomRecyclerViewAdapter(List list, Context context) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.recyclerview_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        int currentIndex = 0;

        List instructionList = mList.get(position).getInstructions();
        TextView instructionText = holder.instructionText;

        instructionText.setText(instructionList.get(currentIndex).toString());
        currentIndex++;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView instructionText;

        public CustomViewHolder(View itemView) {
            super(itemView);

            instructionText = (TextView) itemView.findViewById(R.id.instructionText);
        }
    }
}


