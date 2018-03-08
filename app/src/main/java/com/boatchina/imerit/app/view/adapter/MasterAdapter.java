/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.boatchina.imerit.app.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.data.entity.UsersEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterAdapter extends RecyclerView.Adapter<MasterAdapter.UserViewHolder> {

  public interface OnItemClickListener {
    void onImeiItemClicked(UsersEntity usersEntity);
  }

  private List<UsersEntity> userList;
  private final LayoutInflater layoutInflater;

  private OnItemClickListener onItemClickListener;
  Context context;
  @Inject
  public MasterAdapter(Context context) {
    this.context = context;
    this.layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.userList = Collections.emptyList();
  }

  @Override public int getItemCount() {
    return (this.userList != null) ? this.userList.size() : 0;
  }

  @Override public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = this.layoutInflater.inflate(R.layout.view_imei, parent, false);
    return new UserViewHolder(view);
  }

  @Override public void onBindViewHolder(UserViewHolder holder, final int position) {
    final UsersEntity usersEntity = this.userList.get(position);
    String username = PreferencesUtils.getString(context, "username");
    if(usersEntity.getName().equals(username)) {
      holder.tvImei.setText(usersEntity.getName()+"(自己)");
    }else {
      holder.tvImei.setText(usersEntity.getName());
    }

    holder.tvImei.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (MasterAdapter.this.onItemClickListener != null) {
          MasterAdapter.this.onItemClickListener.onImeiItemClicked(usersEntity);
        }
      }
    });
  }

  @Override public long getItemId(int position) {
    return position;
  }

  public void  setImeiList(Collection<UsersEntity> imeiList) {
    this.validateUsersCollection(imeiList);
    this.userList = (List<UsersEntity>) imeiList;
    this.notifyDataSetChanged();
  }

  public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  private void validateUsersCollection(Collection<UsersEntity> usersCollection) {
    if (usersCollection == null) {
      throw new IllegalArgumentException("The imeiList cannot be null");
    }
  }

  static class UserViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_imei) TextView tvImei;

    public UserViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
