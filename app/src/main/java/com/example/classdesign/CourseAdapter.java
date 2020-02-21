package com.example.classdesign;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder>{
    private Context context;
    private List<Course> courseList;

    private OnItemClickListener mOnItemClickListener = null;

    public void refresh(List<Course> itemList){
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
        TextView courseFare;
        TextView courseUserName;
        TextView courseTeacherName;
        TextView haopingdu;
        TextView classRoomAddress;
        TextView courseTime;
        TextView courseType;
        TextView courseContent;


        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            courseName = view.findViewById(R.id.course_name);
            courseFare = view.findViewById(R.id.course_fare);
            courseUserName = view.findViewById(R.id.course_username);
            classRoomAddress = view.findViewById(R.id.classroom_address);
            courseTime = view.findViewById(R.id.course_time);
            courseType = view.findViewById(R.id.course_type);
            courseTeacherName = view.findViewById(R.id.course_teacher_name);
            courseContent = view.findViewById(R.id.course_content);
            haopingdu = view.findViewById(R.id.haopingdu);
        }
    }

    public CourseAdapter(List<Course> itemList){
        courseList = itemList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.course_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Course item = courseList.get(position);
        holder.courseName.setText(item.getCname());
        holder.courseFare.setText(Double.toString(item.getPrice())+"元");
        holder.courseUserName.setText("发布者："+item.getUsername());
        holder.classRoomAddress.setText("地址："+item.getAddress());
        holder.courseTime.setText("时间："+item.getTime());
        holder.courseTeacherName.setText("授课教师："+item.getTeachername());
        holder.courseType.setText("类型："+item.getType());
        holder.courseContent.setText("课程简介："+item.getContent());
        holder.haopingdu.setText("好评度："+Double.toString(item.getEvaluation()));
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
