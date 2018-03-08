package com.boatchina.imerit.app.view.talk;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boatchina.imerit.data.entity.TalkEntitiy;
import com.boatchina.imerit.app.R;

import java.util.Collections;
import java.util.List;

/**
 * @param
 * @author ldm
 * @description ListView数据适配器
 * @time 2016/6/25 11:05
 */
public class RecoderAdapter extends BaseAdapter {

    private Context mContext;
    private List<TalkEntitiy> mDatas;
    //item的最小宽度
    private int mMinWidth;
    //item的最大宽度
    private int mMaxWidth;
    private LayoutInflater mInflater;
    ClickListener clickListener;
    public List<TalkEntitiy> getTalkList() {
        return mDatas;
    }
    public RecoderAdapter(Context context) {

        mContext = context;
        mDatas = Collections.emptyList();

        //获取屏幕的宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        //最大宽度为屏幕宽度的百分之七十
        mMaxWidth = (int) (outMetrics.widthPixels * 0.7f);
        //最大宽度为屏幕宽度的百分之十五
        mMinWidth = (int) (outMetrics.widthPixels * 0.15f);
        mInflater = LayoutInflater.from(context);
    }
    interface ClickListener {
        void clickListener(View v, int position);
        void retry(String filepath, Integer integer);
    }
    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
    public void setList(List<TalkEntitiy> datas) {
        mDatas = datas;
        this.notifyDataSetChanged();
    }
    final class ViewHolder {
        // 显示时间
        TextView seconds;
        //控件Item显示的长度
        View length;
        ImageView ivFail;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder holder = null;
        switch (type) {
            case 0:

                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_recoder_left, null);
                    holder = new ViewHolder();
                    holder.seconds = (TextView) convertView.findViewById(R.id.id_recoder_time);
                    holder.length = convertView.findViewById(R.id.id_recoder_lenght);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.length.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.clickListener(v,position);
                    }
                });
                holder.seconds.setText(Math.round(mDatas.get(position).duration()) + "\"");
                ViewGroup.LayoutParams lp = holder.length.getLayoutParams();
                lp.width = (int) (mMinWidth + (mMaxWidth / 60f) * mDatas.get(position).duration());
                break;
            case 1:
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_recoder, null);
                    holder = new ViewHolder();
                    holder.seconds = (TextView) convertView.findViewById(R.id.id_recoder_time);
                    holder.length = convertView.findViewById(R.id.id_recoder_lenght);
                    holder.ivFail = (ImageView) convertView.findViewById(R.id.iv_fail);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if(mDatas.get(position).success()) {
                    holder.ivFail.setVisibility(View.GONE);
                }else {
                    holder.ivFail.setVisibility(View.VISIBLE);
                }
                holder.ivFail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        T.showShort("xxx"+mDatas.get(position)._id());
//                        upLoadData(mDatas.get(position).filepath(),mDatas.get(position)._id());
                        clickListener.retry(mDatas.get(position).filepath(),mDatas.get(position)._id());
                    }
                });
                holder.length.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.clickListener(v,position);
                    }
                });
                holder.seconds.setText(Math.round(mDatas.get(position).duration()) + "\"");
                ViewGroup.LayoutParams lp1 = holder.length.getLayoutParams();
                lp1.width = (int) (mMinWidth + (mMaxWidth / 60f) * mDatas.get(position).duration());

                break;
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if(mDatas.get(position).status()) {
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

}
