package io.dcloud.H58E83894.data.know;

import java.util.List;

/**
 * Created by fire on 2017/8/11  14:28.
 */

public class KnowTypeData {


    /**
     * data : [{"id":"394","name":"备考"},{"id":"395","name":"基础"},{"id":"396","name":"技巧"},{"id":"397","name":"提高"}]
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
         * id : 394
         * name : 备考
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
