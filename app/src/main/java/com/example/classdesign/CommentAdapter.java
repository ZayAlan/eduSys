package com.example.classdesign;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    private Context context;
    private List<Remark> courseList;

    private CommentAdapter.OnItemClickListener mOnItemClickListener = null;

    public  interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setOnItemClickListener(CommentAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void refresh(List<Remark> itemList){
        courseList = itemList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView commentPerson;
        TextView score;
        TextView article;


        public ViewHolder(View view){
            super(view);
            commentPerson = view.findViewById(R.id.comment_user_name);
            score = view.findViewById(R.id.comment_evaluate);
            article = view.findViewById(R.id.comment_content);
        }
    }

    public CommentAdapter(List<Remark> itemList){
        courseList = itemList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Remark item = courseList.get(position);
        holder.commentPerson.setText(item.getRemarker());
        holder.score.setText(Integer.toString(item.getScore())+"分");
        holder.article.setText(item.getArticle());
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
        return courseList.size();
    }
}
