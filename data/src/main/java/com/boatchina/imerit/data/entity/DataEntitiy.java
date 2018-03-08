package com.boatchina.imerit.data.entity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.squareup.sqldelight.RowMapper;

/**
 * Created by fflamingogo on 2016/9/26.
 */
@AutoValue
public abstract class DataEntitiy implements DataModel {
    @NonNull
    public static Builder builder() {
        return new AutoValue_DataEntitiy.Builder();
    }

    public static final DataModel.Factory<DataEntitiy> FACTORY = new DataModel.Factory<>(AutoValue_DataEntitiy::new);
    public static final RowMapper<DataEntitiy> MAPPER = FACTORY.get_allMapper();


    public static TypeAdapter<DataEntitiy> typeAdapter(Gson gson) {
        return new AutoValue_DataEntitiy.GsonTypeAdapter(gson);
    }
    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder _id(@Nullable  Integer id);
        public abstract Builder imei(@Nullable String imei);
        public abstract Builder type( int type);

        public abstract Builder isread(boolean isread);
        public abstract Builder create_at(long create_at);



        public abstract DataEntitiy build();
    }


}
