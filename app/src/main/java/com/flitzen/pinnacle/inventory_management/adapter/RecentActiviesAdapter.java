package com.flitzen.pinnacle.inventory_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.model.DashBoardDataLocalModel;
import com.flitzen.pinnacle.inventory_management.model.DashBoardDataModel;

import java.util.ArrayList;

public class RecentActiviesAdapter extends RecyclerView.Adapter {

    private static final int LAYOUT_DATE = 0;
    private static final int LAYOUT_LIST = 1;
    private Context context;
    private ArrayList<DashBoardDataLocalModel> recentActivityArrayList;

    public RecentActiviesAdapter(Context context, ArrayList<DashBoardDataLocalModel> recentActivityArrayList) {
        this.context = context;
        this.recentActivityArrayList = recentActivityArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == LAYOUT_DATE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_date_layout, parent, false);
            viewHolder = new ViewHolderDate(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recent_activity_layout, parent, false);
            viewHolder = new ViewHolderList(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case LAYOUT_DATE:
                ViewHolderDate viewHolderDate = (ViewHolderDate) holder;
                viewHolderDate.txtDate.setText(recentActivityArrayList.get(position).getDate());
                break;

            case LAYOUT_LIST:
                ViewHolderList viewHolderList = (ViewHolderList) holder;
                viewHolderList.txtName.setText(recentActivityArrayList.get(position).getUserName());
                viewHolderList.txtTime.setText(recentActivityArrayList.get(position).getTime());
                viewHolderList.txtActivityStatus.setText(recentActivityArrayList.get(position).getRemarks());
                break;
        }
    }


    @Override
    public int getItemCount() {
        return recentActivityArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (recentActivityArrayList.get(position).getType().equalsIgnoreCase("0")) {
            return LAYOUT_DATE;
        } else {
            return LAYOUT_LIST;
        }
    }

    public static class ViewHolderList extends RecyclerView.ViewHolder {
        public ImageView imgPerson;
        public TextView txtName, txtTime, txtActivityStatus;
        public RelativeLayout btnViewDetails;

        public ViewHolderList(View itemView) {
            super(itemView);
            this.imgPerson = itemView.findViewById(R.id.imgPerson);
            this.txtName = itemView.findViewById(R.id.txtName);
            this.btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            this.txtTime = itemView.findViewById(R.id.txtTime);
            this.txtActivityStatus = itemView.findViewById(R.id.txtActivityStatus);
        }
    }

    public static class ViewHolderDate extends RecyclerView.ViewHolder {
        public TextView txtDate;

        public ViewHolderDate(View itemView) {
            super(itemView);
            this.txtDate = itemView.findViewById(R.id.txtDate);
        }
    }

    public void updateUI(ArrayList<DashBoardDataLocalModel> recentActivityArrayList) {
        this.recentActivityArrayList = recentActivityArrayList;
        notifyDataSetChanged();
    }
}
