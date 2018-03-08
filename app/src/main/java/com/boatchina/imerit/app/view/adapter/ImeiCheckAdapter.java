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
import android.widget.CompoundButton;
import android.widget.TextView;

import com.boatchina.imerit.app.R;
import com.boatchina.imerit.data.device.DeviceEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImeiCheckAdapter extends RecyclerView.Adapter<ImeiCheckAdapter.UserViewHolder> {

  public interface OnItemClickListener {
    void onImeiItemClicked(DeviceEntity bond);
  }

  public List<DeviceEntity> getImeiList() {
    return imeiList;
  }

  private List<DeviceEntity> imeiList;
  private List<String> checkImeiList;
  private final LayoutInflater layoutInflater;

  private OnItemClickListener onItemClickListener;

  @Inject
  public ImeiCheckAdapter(Context context) {
    this.layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.imeiList = Collections.emptyList();
  }

  @Override public int getItemCount() {
    return (this.imeiList != null) ? this.imeiList.size() : 0;
  }

  @Override public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = this.layoutInflater.inflate(R.layout.view_imei_check, parent, false);
    return new UserViewHolder(view);
  }

  @Override public void onBindViewHolder(UserViewHolder holder, final int position) {
    final DeviceEntity bond = this.imeiList.get(position);
    if(checkImeiList!=null&&checkImeiList.contains(bond.getImei())) {
      bond.setChecked(true);
    }else {
      bond.setChecked(false);
    }
    if(bond.isChecked()) {
      holder.checkBox.setChecked(true);
    }else {
      holder.checkBox.setChecked(false);
    }
    holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
          bond.setChecked(true);
        }else {
          bond.setChecked(false);
        }
      }
    });

    if(TextUtils.isEmpty(bond.getName())) {
      holder.tvImei.setText(bond.getImei());
    }else {
      holder.tvImei.setText(bond.getImei()+" （"+ bond.getName()+"）");
    }
    holder.tvImei.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (ImeiCheckAdapter.this.onItemClickListener != null) {
          ImeiCheckAdapter.this.onItemClickListener.onImeiItemClicked(bond);
        }
      }
    });
  }

  @Override public long getItemId(int position) {
    return position;
  }

  public void  setImeiList(Collection<DeviceEntity> imeiList) {
    this.validateUsersCollection(imeiList);
    this.imeiList = (List<DeviceEntity>) imeiList;
    this.notifyDataSetChanged();
  }
  public void  setImeiCheckList(Collection<String> imeiList) {
    this.checkImeiList = (List<String>) imeiList;
    this.notifyDataSetChanged();
  }

  public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  private void validateUsersCollection(Collection<DeviceEntity> usersCollection) {
    if (usersCollection == null) {
      throw new IllegalArgumentException("The imeiList cannot be null");
    }
  }

  static class UserViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_imei) TextView tvImei;
    @BindView(R.id.checkbox)
    CheckBox checkBox;

    public UserViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
