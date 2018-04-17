package io.dcloud.H58E83894.data;

import java.util.List;

/**
 * Created by fire on 2017/8/11  14:28.
 */

public class LeidouReData {


    /**
     * integral : 110
     * details : [{"id":"1837631","uid":"27178","username":"lgw27178","behavior":"听力做题一道","type":"1","integral":"2","createTime":"2018-01-16"},{"id":"1837629","uid":"27178","username":"lgw27178","behavior":"听力做题一道","type":"1","integral":"2","createTime":"2018-01-16"},{"id":"1837628","uid":"27178","username":"lgw27178","behavior":"听力做题一道","type":"1","integral":"2","createTime":"2018-01-16"},{"id":"1837626","uid":"27178","username":"lgw27178","behavior":"听力做题一道","type":"1","integral":"2","createTime":"2018-01-16"},{"id":"1837624","uid":"27178","username":"lgw27178","behavior":"听力做题一道","type":"1","integral":"2","createTime":"2018-01-16"},{"id":"1721374","uid":"27178","username":"lgw27178","behavior":"注册成功","type":"1","integral":"100","createTime":"2018-01-04"}]
     * count : 6
     */


    private String integral;
    private String count;
    private List<DetailsBean> details;

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<DetailsBean> getDetails() {
        return details;
    }

    public void setDetails(List<DetailsBean> details) {
        this.details = details;
    }

    public static class DetailsBean {
        /**
         * id : 1837631
         * uid : 27178
         * username : lgw27178
         * behavior : 听力做题一道
         * type : 1
         * integral : 2
         * createTime : 2018-01-16
         */

        private String id;
        private String uid;
        private String username;
        private String behavior;
        private String type;
        private String integral;
        private String createTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getBehavior() {
            return behavior;
        }

        public void setBehavior(String behavior) {
            this.behavior = behavior;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
