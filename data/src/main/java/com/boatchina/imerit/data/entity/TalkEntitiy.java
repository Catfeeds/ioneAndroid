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
public abstract class TalkEntitiy implements TalkModel {
    @NonNull
    public static Builder builder() {
        return new AutoValue_TalkEntitiy.Builder();
    }

    public static final TalkModel.Factory<TalkEntitiy> FACTORY = new TalkModel.Factory<>(AutoValue_TalkEntitiy::new);
    public static final RowMapper<TalkEntitiy> MAPPER = FACTORY.get_allMapper();


    public static TypeAdapter<TalkEntitiy> typeAdapter(Gson gson) {
        return new AutoValue_TalkEntitiy.GsonTypeAdapter(gson);
    }
    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder _id(@Nullable Integer id);
        public abstract Builder imei(@Nullable String imei);
        public abstract Builder myimei(@Nullable String myimei);
        public abstract Builder name(@Nullable String name);
        public abstract Builder myname(@Nullable String myname);
        public abstract Builder filepath(@Nullable String filepath);
        public abstract Builder status(boolean issend);
        public abstract Builder success( boolean success);
        public abstract Builder create_at(long create_at);
        public abstract Builder duration(int duration);

        public abstract TalkEntitiy build();
    }


}
