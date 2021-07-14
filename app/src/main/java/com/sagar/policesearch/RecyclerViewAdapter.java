package com.sagar.policesearch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
//import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.sagar.policesearch.Constant.SHARE_INTENT_ACTION;
import static com.sagar.policesearch.Constant.SHARE_TEXT_ID;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    public List<PolicePojo> mItemList;
    private Context context;

    private static Set<PolicePojo> selectedPolicePojos = new HashSet<>();

    public RecyclerViewAdapter(List<PolicePojo> itemList) {
        mItemList = itemList;
    }

    public static void setSelectedPolicePojos(Set<PolicePojo> set){
        selectedPolicePojos = set;
    }

    public static Set<PolicePojo> getSelectedPolicePojos() {
        return selectedPolicePojos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_ITEM :
                return new ItemViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_row, parent, false)
                );
            case VIEW_TYPE_LOADING :
                return new LoadingViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_loading, parent, false)
                );
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows ((ItemViewHolder) viewHolder, position);
        }
        else {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed
    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {
        PolicePojo item = mItemList.get(position);
        viewHolder.textViewSno.setText(item.getSno());
        viewHolder.textViewPsname.setText(item.getPoliceStation());
        viewHolder.textViewDistrict.setText(item.getDistrict());
        viewHolder.textViewPhoneno.setText(item.getPhoneNumber());
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewSno;
        public TextView textViewPsname;
        public TextView textViewDistrict;
        public TextView textViewPhoneno;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSno = itemView.findViewById(R.id.textSno);
            textViewPsname = itemView.findViewById(R.id.textPsname);
            textViewDistrict = itemView.findViewById(R.id.textDistrict);
            textViewPhoneno = itemView.findViewById(R.id.textPhoneno);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    PolicePojo posData = mItemList.get(pos);
                    selectedPolicePojos.add(posData);
                    itemView.setBackgroundColor(Color.parseColor("#DC746C"));
                }
            });
        }
    }
}
