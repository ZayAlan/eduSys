package com.example.classdesign.Administrator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.classdesign.Course;
import com.example.classdesign.Institution.Institution;
import com.example.classdesign.R;

import java.util.List;

public class CheckInsItemAdapter extends RecyclerView.Adapter<CheckInsItemAdapter.ViewHolder>{
    private Context context;
    private List<Institution> courseList;

    private OnItemClickListener mOnItemClickListener = null;

    public void refresh(List<Institution> itemList){
        courseList = itemList;
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
        TextView username;
        TextView region;
        TextView code;
        TextView address;
        TextView suitAge;
        TextView contact;
        TextView resume;


        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            username = view.findViewById(R.id.check_institution_username);
            region = view.findViewById(R.id.check_institution_region);
            code = view.findViewById(R.id.check_institution_code);
            address = view.findViewById(R.id.check_institution_address);
            suitAge = view.findViewById(R.id.check_institution_suit_age);
            contact = view.findViewById(R.id.check_institution_contact);
            resume = view.findViewById(R.id.check_institution_resume);
        }
    }

    public CheckInsItemAdapter(List<Institution> itemList){
        courseList = itemList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.check_institution_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Institution item = courseList.get(position);
        holder.username.setText(item.getUsername());
        holder.region.setText("教育领域："+item.getTerritory());
        holder.code.setText("标识码："+item.getEid());
        holder.address.setText("地址："+item.getAddress());
        holder.suitAge.setText("教育适合年龄："+item.getAdapagel()+" - "+item.getAdapageh());
        holder.contact.setText("联系方式："+item.getCmethod());
        holder.resume.setText("简介："+item.getResume());
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
