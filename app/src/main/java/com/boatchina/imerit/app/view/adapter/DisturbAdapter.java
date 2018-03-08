/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.boatchina.imerit.app.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boatchina.imerit.data.entity.TimeConfigEntity;
import com.boatchina.imerit.app.R;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class DisturbAdapter extends RecyclerView.Adapter<DisturbAdapter.UserViewHolder> {

  public interface OnItemClickListener {
    void onImeiItemClicked(TimeConfigEntity contactEntity);
    void onImeiItemLongClicked(TimeConfigEntity timeConfigEntity);
  }

  private List<TimeConfigEntity> contactEntityList;
  private final LayoutInflater layoutInflater;

  private OnItemClickListener onItemClickListener;

  @Inject
  public DisturbAdapter(Context context) {
    this.layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.contactEntityList = Collections.emptyList();
  }

  @Override public int getItemCount() {
    return (this.contactEntityList != null) ? this.contactEntityList.size() : 0;
  }

  @Override public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = this.layoutInflater.inflate(R.layout.view_disturb, parent, false);
    return new UserViewHolder(view);
  }

  @Override public void onBindViewHolder(UserViewHolder holder, final int position) {
    final TimeConfigEntity contactEntity = this.contactEntityList.get(position);
    holder.ll.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (DisturbAdapter.this.onItemClickListener != null) {
          DisturbAdapter.this.onItemClickListener.onImeiItemClicked(contactEntity);
        }
      }
    });
    holder.ll.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (DisturbAdapter.this.onItemClickListener != null) {
          DisturbAdapter.this.onItemClickListener.onImeiItemLongClicked(contactEntity);
        }
        return false;
      }
    });
    holder.startTime.setText(contactEntity.getBegin());
    holder.endTime.setText(contactEntity.getEnd());
    String repeat = contactEntity.getRepeat();
    if(TextUtils.isEmpty(repeat)) {
      return;
    }
    if(repeat.substring(0,1).equals("1")) {
      holder.cb7.setChecked(true);
    }else {
      holder.cb7.setChecked(false);
    }
    if(repeat.substring(1,2).equals("1")) {
      holder.cb1.setChecked(true);
    }else {
      holder.cb1.setChecked(false);
    }
    if(repeat.substring(2,3).equals("1")) {
      holder.cb2.setChecked(true);
    }else {
      holder.cb2.setChecked(false);
    }
    if(repeat.substring(3,4).equals("1")) {
      holder.cb3.setChecked(true);
    }else {
      holder.cb3.setChecked(false);
    }
    if(repeat.substring(4,5).equals("1")) {
      holder.cb4.setChecked(true);
    }else {
      holder.cb4.setChecked(false);
    }
    if(repeat.substring(5,6).equals("1")) {
      holder.cb5.setChecked(true);
    }else {
      holder.cb5.setChecked(false);
    }
    if(repeat.substring(6,7).equals("1")) {
      holder.cb6.setChecked(true);
    }else {
      holder.cb6.setChecked(false);
    }
  }

  @Override public long getItemId(int position) {
    return position;
  }

  public void  setContantsList(Collection<TimeConfigEntity> contactEntityList) {
    this.validateUsersCollection(contactEntityList);
    this.contactEntityList = (List<TimeConfigEntity>) contactEntityList;
    this.notifyDataSetChanged();
  }

  public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  private void validateUsersCollection(Collection<TimeConfigEntity> contactEntityCollection) {
    if (contactEntityCollection == null) {
      throw new IllegalArgumentException("The imeiList cannot be null");
    }
  }

  static class UserViewHolder extends RecyclerView.ViewHolder {
     CheckBox cb7 ;
     CheckBox cb1 ;
     CheckBox cb2 ;
     CheckBox cb3 ;
     CheckBox cb4 ;
     CheckBox cb5 ;
     CheckBox cb6 ;
     TextView startTime;
     TextView endTime;
    LinearLayout ll;
    public UserViewHolder(View itemView) {
      super(itemView);
      startTime = (TextView) itemView.findViewById(R.id.tv_starttime);
      endTime = (TextView) itemView.findViewById(R.id.tv_endtime);
      cb7 = (CheckBox) itemView.findViewById(R.id.cb_7);
      cb1 = (CheckBox) itemView.findViewById(R.id.cb_1);
      cb2 = (CheckBox) itemView.findViewById(R.id.cb_2);
      cb3 = (CheckBox) itemView.findViewById(R.id.cb_3);
      cb4 = (CheckBox) itemView.findViewById(R.id.cb_4);
      cb5 = (CheckBox) itemView.findViewById(R.id.cb_5);
      cb6 = (CheckBox) itemView.findViewById(R.id.cb_6);
      ll = (LinearLayout) itemView.findViewById(R.id.ll_layout);
      cb7.setClickable(false);
      cb1.setClickable(false);
      cb2.setClickable(false);
      cb3.setClickable(false);
      cb4.setClickable(false);
      cb5.setClickable(false);
      cb6.setClickable(false);
    }
  }
}
