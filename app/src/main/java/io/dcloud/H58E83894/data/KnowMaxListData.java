package io.dcloud.H58E83894.data;


import java.util.List;

public class KnowMaxListData {


    /**
     * data : [{"count":4,"num":13465},{"count":3,"num":10658},{"count":6,"num":21212},{"count":3,"num":24654}]
     * code : 1
     */

    private int code;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * count : 4
         * num : 13465
         */

        private int count;
        private int num;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
