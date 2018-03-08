/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.boatchina.imerit.app.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.utils.T;
import com.boatchina.imerit.data.fence.FenceEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FenceAdapter extends RecyclerView.Adapter<FenceAdapter.UserViewHolder> {

  public interface OnItemClickListener {
    void onImeiItemClicked(FenceEntity fend);
    void onImeiItemLongClicked(FenceEntity fend);
  }

  private List<FenceEntity> imeiList;
  private final LayoutInflater layoutInflater;

  private OnItemClickListener onItemClickListener;
  private Activity activity;


  @Inject
  public FenceAdapter(Activity activity) {
    this.activity = activity;
    this.layoutInflater =
        (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.imeiList = Collections.emptyList();
  }

  @Override public int getItemCount() {
    return (this.imeiList != null) ? this.imeiList.size() : 0;
  }

  @Override public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = this.layoutInflater.inflate(R.layout.view_fend, parent, false);
    return new UserViewHolder(view);
  }

  @Override public void onBindViewHolder(final UserViewHolder holder, final int position) {
    final FenceEntity fend = this.imeiList.get(position);
    holder.tvName.setText(fend.getName());
    GeocodeSearch geocoderSearch = new GeocodeSearch(activity);
    geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
      @Override
      public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
          if (result != null && result.getRegeocodeAddress() != null
                  && result.getRegeocodeAddress().getFormatAddress() != null) {
            if(result.getRegeocodeAddress().getFormatAddress().equals("")) {
              holder.tvAddress.setText("没有地址");
            }else {
              String addressName = result.getRegeocodeAddress().getFormatAddress()
                      + "附近";
              holder.tvAddress.setText(addressName);
            }

          } else {
            T.showShort("no_result");
          }
        } else {
//          T.showShort(rCode+"");
        }
      }

      @Override
      public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

      }
    });
    RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(fend.getLat1(),fend.getLng1()), 200,GeocodeSearch.GPS);
    geocoderSearch.getFromLocationAsyn(query);
    holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (FenceAdapter.this.onItemClickListener != null) {
          FenceAdapter.this.onItemClickListener.onImeiItemClicked(fend);
        }
      }
    });

    holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {

      @Override
      public boolean onLongClick(View v) {
        if (FenceAdapter.this.onItemClickListener != null) {
          FenceAdapter.this.onItemClickListener.onImeiItemLongClicked(fend);
        }

        return false;
      }
    });
  }

  @Override public long getItemId(int position) {
    return position;
  }

  public void  setImeiList(Collection<FenceEntity> imeiList) {
    this.validateUsersCollection(imeiList);
    this.imeiList = (List<FenceEntity>) imeiList;
    this.notifyDataSetChanged();
  }

  public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  private void validateUsersCollection(Collection<FenceEntity> usersCollection) {
    if (usersCollection == null) {
      throw new IllegalArgumentException("The imeiList cannot be null");
    }
  }

  static class UserViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.tv_address) TextView tvAddress;
    @BindView(R.id.constraintLayout2)
    ConstraintLayout constraintLayout;
    public UserViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
