package com.example.classdesign.Administrator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.classdesign.Institution.Institution;
import com.example.classdesign.R;
import com.example.classdesign.Teacher.Teacher;

import java.util.List;

public class CheckTeaItemAdapter extends RecyclerView.Adapter<CheckTeaItemAdapter.ViewHolder>{
    private Context context;
    private List<Teacher> courseList;

    private OnItemClickListener mOnItemClickListener = null;

    public void refresh(List<Teacher> itemList){
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
        TextView name;
        TextView sex;
        TextView age;
        TextView region;
        TextView teachYear;
        TextView code;
        TextView suitAge;
        TextView contact;
        TextView resume;


        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            username = view.findViewById(R.id.check_teacher_username);
            name = view.findViewById(R.id.check_teacher_name);
            sex = view.findViewById(R.id.check_teacher_sex);
            age = view.findViewById(R.id.check_teacher_age);
            region = view.findViewById(R.id.check_teacher_region);
            teachYear = view.findViewById(R.id.check_teacher_teach_year);
            code = view.findViewById(R.id.check_teacher_code);
            suitAge = view.findViewById(R.id.check_teacher_suit_age);
            contact = view.findViewById(R.id.check_teacher_contact);
            resume = view.findViewById(R.id.check_teacher_resume);
        }
    }

    public CheckTeaItemAdapter(List<Teacher> itemList){
        courseList = itemList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.check_teacher_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Teacher item = courseList.get(position);
        holder.username.setText(item.getUsername());
        holder.name.setText("姓名："+item.getName());
        holder.sex.setText("性别："+item.getSex());
        holder.age.setText("年龄："+item.getAge());
        holder.region.setText("教育领域："+item.getTerritory());
        holder.teachYear.setText("教育年限："+item.getCareer());
        holder.code.setText("标识码："+item.getId());
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
