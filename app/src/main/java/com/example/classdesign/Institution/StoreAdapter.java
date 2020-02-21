package com.example.classdesign.Institution;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.classdesign.R;

import org.w3c.dom.Text;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder>{

    private Context context;
    private List<Store> mStoreList;

    private OnItemClickListener mOnItemClickListener = null;

    public void refresh(List<Store> itemList){
        mStoreList = itemList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView storeName;
        TextView userName;
        TextView address;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            storeName = view.findViewById(R.id.store_name);
            userName = view.findViewById(R.id.institution_username);
            address = view.findViewById(R.id.store_address);
        }
    }

    public StoreAdapter(List<Store> itemList){
        mStoreList = itemList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.store_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Store item = mStoreList.get(position);
        holder.storeName.setText(item.getSname());
        holder.userName.setText(item.getUsername());
        holder.address.setText(item.getAddress());
        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mStoreList.size();
    }
}
