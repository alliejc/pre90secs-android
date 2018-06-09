package com.allie.pre90secs.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allie.pre90secs.R;

import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.CustomViewHolder> {

    private List mInstructionList;
    private Context mContext;

    public InstructionAdapter(List list, Context context) {
        this.mInstructionList = list;
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
        String exerciseItem = mInstructionList.get(position).toString();
        TextView instructionText = holder.instructionText;
        TextView instructionCount = holder.instructionCount;
        instructionText.setText(exerciseItem);
        instructionCount.setText(String.valueOf(holder.getAdapterPosition()+1) + ". ");
    }

    @Override
    public int getItemCount() {
        return mInstructionList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView instructionText;
        public TextView instructionCount;

        public CustomViewHolder(View itemView) {
            super(itemView);
            instructionCount = (TextView) itemView.findViewById(R.id.instruction_count);
            instructionText = (TextView) itemView.findViewById(R.id.instructionText);
        }
    }
}


