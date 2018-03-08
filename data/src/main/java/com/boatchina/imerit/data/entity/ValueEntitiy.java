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
public abstract class ValueEntitiy implements ValueModel {
    @NonNull
    public static Builder builder() {
        return new AutoValue_ValueEntitiy.Builder();
    }

    public static final ValueModel.Factory<ValueEntitiy> FACTORY = new ValueModel.Factory<>(AutoValue_ValueEntitiy::new);
    public static final RowMapper<ValueEntitiy> MAPPER = FACTORY.get_allMapper();


    public static TypeAdapter<ValueEntitiy> typeAdapter(Gson gson) {
        return new AutoValue_ValueEntitiy.GsonTypeAdapter(gson);
    }
    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder _id(@Nullable  Integer id);
        public abstract Builder key_id(long key_id);

        public abstract Builder key(String key);
        public abstract Builder value(String value);




        public abstract ValueEntitiy build();
    }


}
