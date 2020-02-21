package com.example.classdesign;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.ViewHolder>{
    private Context context;
    private List<CarItem> courseList;

    private OnItemClickListener mOnItemClickListener = null;

    public void refresh(List<CarItem> itemList){
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
        TextView courseName;
        TextView courseTeacherName;
        TextView classRoomAddress;
        TextView courseTime;


        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            courseName = view.findViewById(R.id.timetable_name);
            classRoomAddress = view.findViewById(R.id.timetable_classroom_address);
            courseTime = view.findViewById(R.id.timetable_course_time);
            courseTeacherName = view.findViewById(R.id.timetable_teacher_name);
        }
    }

    public TimeTableAdapter(List<CarItem> itemList){
        courseList = itemList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.timetable_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CarItem item = courseList.get(position);
        holder.courseName.setText(item.getCname());
        holder.classRoomAddress.setText("地址："+item.getAddress());
        holder.courseTime.setText("时间："+item.getTime());
        holder.courseTeacherName.setText("授课教师："+item.getTeachername());
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
