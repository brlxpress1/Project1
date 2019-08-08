package com.tur.job1.models;

import java.io.Serializable;

public class DetailsUpdateResponse implements Serializable {

    private Integer userId;
    private boolean isUpdateComplete;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isUpdateComplete() {
        return isUpdateComplete;
    }

    public void setUpdateComplete(boolean updateComplete) {
        isUpdateComplete = updateComplete;
    }
}
