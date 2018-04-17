package io.dcloud.H58E83894.ui.center.leidou.recharge;

import java.io.Serializable;
import java.util.List;

import io.dcloud.H58E83894.data.LeidouRechargeData;

/**
 * Created by WangChang on 2016/4/1.
 */
public class ItemModel implements Serializable {

    public static final int ONE = 1001;
    public static final int TWO = 1002;

    public int type;
    public String data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    //    private List<LeidouRechargeData> data;

//    public List<LeidouRechargeData> getData() {
//        return data;
//    }
//
//    public void setData(List<LeidouRechargeData> data) {
//        this.data = data;
//    }

    public ItemModel(int type, String data) {
        this.type = type;
        this.data = data;
    }
}
