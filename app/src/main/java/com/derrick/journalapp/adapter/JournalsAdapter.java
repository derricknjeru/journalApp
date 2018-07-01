package com.derrick.journalapp.adapter;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.derrick.journalapp.R;
import com.derrick.journalapp.databinding.JournalListRowBinding;
import com.derrick.journalapp.pojos.Journal;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class JournalsAdapter extends RecyclerView.Adapter<JournalsAdapter.MyViewHolder> {
    private List<Journal> mJournalList;
    private final OnItemClickListener mListener;
    private Activity mActivity;

    public JournalsAdapter(List<Journal> mJournalList, OnItemClickListener listener, Activity activity) {
        this.mJournalList = mJournalList;
        this.mListener = listener;
        this.mActivity = activity;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());

        JournalListRowBinding itemBinding =
                JournalListRowBinding.inflate(layoutInflater, parent, false);

        return new MyViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Journal journal = mJournalList.get(position);

        holder.mBinding.tvTitle.setText(journal.getTitleForList());

        holder.mBinding.iconProfile.setImageResource(R.drawable.bg_circle);
        holder.mBinding.iconProfile.setColorFilter(getRandomMaterialColor("400"));

        // displaying the first letter of From in icon text
        holder.mBinding.iconText.setText(journal.getTitleForList().substring(0, 1));

        holder.mBinding.tvDescription.setText(journal.getDescription());

        //holder.mBinding.setText(journal.getDate());

        holder.mBinding.date.setText(journal.getDate());

        holder.mBinding.time.setText(journal.getTime());

        holder.mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(journal);
            }
        });


    }


    @Override
    public int getItemCount() {
        return mJournalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private JournalListRowBinding mBinding;

        public MyViewHolder(JournalListRowBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

    }

    public interface OnItemClickListener {
        void onItemClick(Journal item);
    }

    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = mActivity.getResources().getIdentifier("mdcolor_" + typeColor, "array", mActivity.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = mActivity.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }
}
