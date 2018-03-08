/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.boatchina.imerit.app.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.utils.TimeUtils;
import com.boatchina.imerit.data.entity.MyNews;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.UserViewHolder> {

  public interface OnItemClickListener {
    void onImeiItemClicked(MyNews newsEntitiy);
  }

  private List<MyNews> imeiList = new ArrayList<>();
  private final LayoutInflater layoutInflater;

  private OnItemClickListener onItemClickListener;
  private Activity activity;
  @Inject
  public NewsAdapter(Activity activity) {
    this.activity = activity;
    this.layoutInflater =
        (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);





  }

  @Override public int getItemCount() {
    return imeiList.size();
  }

  @Override public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = layoutInflater.inflate(R.layout.view_info, parent, false);
    return new UserViewHolder(view);
  }

  @Override
  public int getItemViewType(int position) {
    if(imeiList.get(position).getType()==1) {
      return 1;
    }else if(imeiList.get(position).getType()==2) {
      return 2;
    }else if(imeiList.get(position).getType()==3) {
      return 3;
    }else if(imeiList.get(position).getType()==4) {
      return 4;
    }else if(imeiList.get(position).getType()==5) {
      return 5;
    }
    return super.getItemViewType(position);
  }

  @Override public void onBindViewHolder(final UserViewHolder holder, final int position) {

    final MyNews newEntitiy = this.imeiList.get(position);

    if(getItemViewType(position)==1) {
      String status =Boolean.parseBoolean(newEntitiy.getStatus())  ?activity.getString(R.string.desc_into_fence):activity.getString(R.string.desc_outo_fence);
      holder.tvDesc.setText(newEntitiy.getImei()+"  "+status+" "+newEntitiy.getFine());
    }else if(getItemViewType(position)==2) {
      holder.tvDesc.setText(newEntitiy.getImei()+" 电量不足 "+newEntitiy.getPower()+"%");
    }else if(getItemViewType(position)==3) {
      holder.tvDesc.setText(newEntitiy.getUser()+" 请求绑定 "+newEntitiy.getImei()+" 消息:"+newEntitiy.getMsg());
    }else if(getItemViewType(position)==4) {
      String s = newEntitiy.getResult().equals("1") ? "同意" : "拒绝";
      holder.tvDesc.setText(newEntitiy.getImei()+" 的请求 "+s);
    }else if(getItemViewType(position)==5) {
      holder.tvDesc.setText("收到新的语音");
    }

    holder.tvTime.setText(TimeUtils.timeFormat(newEntitiy.getCreate_at()));
    holder.constraintLayout2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onItemClickListener.onImeiItemClicked(newEntitiy);


      }
    });
    if(newEntitiy.isread()) {

      holder.constraintLayout2.setBackgroundColor(Color.GRAY);
    }else {
      holder.constraintLayout2.setBackgroundColor(Color.TRANSPARENT);
    }
  }

  @Override public long getItemId(int position) {
    return position;
  }

  public void  setImeiList(List<MyNews> imeiList) {
    this.validateUsersCollection(imeiList);

    this.imeiList.addAll(imeiList);
    this.notifyDataSetChanged();
  }

  public void clearData() {
    imeiList.clear();
  }

  public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  private void validateUsersCollection(Collection<MyNews> usersCollection) {
    if (usersCollection == null) {
      throw new IllegalArgumentException("The imeiList cannot be null");
    }
  }

  static class UserViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_desc) TextView tvDesc;
    @BindView(R.id.tv_time) TextView tvTime;
    @BindView(R.id.constraintLayout2)
    ConstraintLayout constraintLayout2;

    public UserViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
