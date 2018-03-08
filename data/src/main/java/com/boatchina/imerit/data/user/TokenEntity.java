package com.boatchina.imerit.data.user;

import com.boatchina.imerit.data.entity.BaseEntity;

/**
 * Created by fflamingogo on 2016/7/18.
 */
public class TokenEntity extends BaseEntity {

        private String token;
        private String alias;
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

}
