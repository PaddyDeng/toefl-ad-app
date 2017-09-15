package io.dcloud.H58E83894.data.make.type;

import io.dcloud.H58E83894.utils.C;

/**
 * Created by fire on 2017/7/27  10:06.
 */

public class TpoTitle implements TypeTpo {

    private String catName;

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    @Override
    public int getType() {
        return C.TYPE_TITLE;
    }
}
