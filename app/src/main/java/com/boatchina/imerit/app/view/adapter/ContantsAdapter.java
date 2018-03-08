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

import com.boatchina.imerit.data.entity.ContactEntity;
import com.boatchina.imerit.app.R;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContantsAdapter extends RecyclerView.Adapter<ContantsAdapter.UserViewHolder> {

  public interface OnItemClickListener {
    void onImeiItemClicked(ContactEntity contactEntity);
    void onImeiItemLongClicked(ContactEntity contactEntity);
  }

  private List<ContactEntity> contactEntityList;
  private final LayoutInflater layoutInflater;

  private OnItemClickListener onItemClickListener;

  @Inject
  public ContantsAdapter(Context context) {
    this.layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.contactEntityList = Collections.emptyList();
  }

  @Override public int getItemCount() {
    return (this.contactEntityList != null) ? this.contactEntityList.size() : 0;
  }

  @Override public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = this.layoutInflater.inflate(R.layout.view_imei, parent, false);
    return new UserViewHolder(view);
  }

  @Override public void onBindViewHolder(UserViewHolder holder, final int position) {
    final ContactEntity contactEntity = this.contactEntityList.get(position);
      holder.tvImei.setText(contactEntity.getName()+"("+contactEntity.getNumber()+")");
    holder.tvImei.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (ContantsAdapter.this.onItemClickListener != null) {
          ContantsAdapter.this.onItemClickListener.onImeiItemClicked(contactEntity);
        }
      }
    });
    holder.tvImei.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (ContantsAdapter.this.onItemClickListener != null) {
          ContantsAdapter.this.onItemClickListener.onImeiItemLongClicked(contactEntity);
        }
        return false;
      }
    });
  }

  @Override public long getItemId(int position) {
    return position;
  }

  public void  setContantsList(Collection<ContactEntity> contactEntityList) {
    this.validateUsersCollection(contactEntityList);
    this.contactEntityList = (List<ContactEntity>) contactEntityList;
    this.notifyDataSetChanged();
  }

  public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  private void validateUsersCollection(Collection<ContactEntity> contactEntityCollection) {
    if (contactEntityCollection == null) {
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
