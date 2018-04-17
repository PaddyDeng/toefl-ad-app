package io.dcloud.H58E83894.data;

import java.io.Serializable;

public class ItemModelData implements Serializable {

    public static final int ONE = 1001;
    public static final int TWO = 1002;

    public int contentId;
    public int token;

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    //    }

    public ItemModelData(int contentId, int token) {
        this.contentId = contentId;
        this.token = token;
    }
}