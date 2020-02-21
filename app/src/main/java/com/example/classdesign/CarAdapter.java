package com.example.classdesign;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class CarAdapter extends BaseAdapter {
    private boolean isShow = true;//是否显示编辑/完成
    private List<CarItem> shoppingCartBeanList;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private Context context;

    public CarAdapter(Context context) {
        this.context = context;
    }

    public void setShoppingCartBeanList(List<CarItem> shoppingCartBeanList) {
        this.shoppingCartBeanList = shoppingCartBeanList;
        notifyDataSetChanged();
    }

    public void refresh(List<CarItem> itemList){
        shoppingCartBeanList = itemList;
        notifyDataSetChanged();
    }
    /**
     * 单选接口
     *
     * @param checkInterface
     */
    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    @Override
    public int getCount() {
        return shoppingCartBeanList == null ? 0 : shoppingCartBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return shoppingCartBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * 是否显示可编辑
     *
     * @param flag
     */
    public void isShow(boolean flag) {
        isShow = flag;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.car_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CarItem shoppingCartBean = shoppingCartBeanList.get(position);
        holder.courseName.setText(shoppingCartBean.getCname());
        holder.userName.setText("卖家: " + shoppingCartBean.getPublisher());
        holder.time.setText("上课时间: " + shoppingCartBean.getTime());
        holder.fare.setText("￥: " + shoppingCartBean.getPrice());
        holder.ck_chose.setChecked(shoppingCartBean.isChose());

        //单选框按钮
        holder.ck_chose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shoppingCartBean.setChose(((CheckBox) v).isChecked());
                        checkInterface.checkGroup(position, ((CheckBox) v).isChecked());//向外暴露接口
                    }
                }
        );

        //删除弹窗
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert = new AlertDialog.Builder(context).create();
                alert.setTitle("操作提示");
                alert.setMessage("您确定要将这个课程从购物车中移除吗？");
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //删除项目！发送数据
                                modifyCountInterface.childDelete(position);
                            }
                        });
                alert.show();
            }
        });

        //判断是否在编辑状态下
        if (isShow) {
            holder.tv_delete.setVisibility(View.GONE);
        } else {
            holder.tv_delete.setVisibility(View.VISIBLE);
        }

        return convertView;
    }


    //初始化控件
    class ViewHolder {
        TextView courseName, userName, time, fare, tv_delete;
        CheckBox ck_chose;

        public ViewHolder(View itemView) {
            ck_chose = (CheckBox) itemView.findViewById(R.id.ck_chose);
            courseName = (TextView) itemView.findViewById(R.id.tv_commodity_name);
            userName = (TextView) itemView.findViewById(R.id.tv_fabric);
            fare = (TextView) itemView.findViewById(R.id.tv_price);
            tv_delete = (TextView) itemView.findViewById(R.id.tv_delete);
            time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
