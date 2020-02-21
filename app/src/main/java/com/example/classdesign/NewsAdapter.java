package com.example.classdesign;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    private Context mContext;
    private List<News> mItemList;

    private OnItemClickListener mOnItemClickListener = null;

    public  interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
    public void refresh(List<News> itemList){
        mItemList = itemList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView timeView;
        TextView titleView;
        TextView ContentView;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            timeView = view.findViewById(R.id.article_time);
            titleView = view.findViewById(R.id.article_title);
            ContentView = view.findViewById(R.id.news_content);
        }
    }

    public NewsAdapter(List<News> itemList){
        mItemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.news_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        News item = mItemList.get(position);
        holder.ContentView.setText(item.getContent());
        holder.timeView.setText(item.getTime());
        holder.titleView.setText(item.getTitle());
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
        return mItemList.size();
    }
}
