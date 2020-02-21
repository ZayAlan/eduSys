package com.example.classdesign;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> implements View.OnClickListener{
    private Context mContext;
    private List<Item> mItemList;

    private OnItemClickListener mOnItemClickListener = null;

    public static interface OnItemClickListener{
        void onClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imageView;
        TextView textView;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            textView = view.findViewById(R.id.item_text);
            imageView = view.findViewById(R.id.item_picture);
        }
    }

    public ItemAdapter(List<Item> itemList){
        mItemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_item, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = mItemList.get(position);
        holder.textView.setText(item.getItemText());
        holder.imageView.setImageResource(item.getItemPicture());
        holder.itemView.setTag(position);
    }

    @Override
    public void onClick(View v) {
        if (mItemList != null){
            mOnItemClickListener.onClick(v,(int)v.getTag());
        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
