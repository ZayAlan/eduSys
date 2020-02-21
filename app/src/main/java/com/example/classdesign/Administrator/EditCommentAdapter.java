package com.example.classdesign.Administrator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.classdesign.R;
import com.example.classdesign.Remark;

import java.util.List;

public class EditCommentAdapter extends RecyclerView.Adapter<EditCommentAdapter.ViewHolder>{
    private Context context;
    private List<Remark> courseList;


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

    public EditCommentAdapter(List<Remark> itemList){
        courseList = itemList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.edit_comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Remark item = courseList.get(position);
        holder.commentPerson.setText(item.getRemarker());
        holder.score.setText(Integer.toString(item.getScore())+"分");
        holder.article.setText(item.getArticle());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}
