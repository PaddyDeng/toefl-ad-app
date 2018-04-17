package io.dcloud.H58E83894.data;

import com.google.gson.annotations.SerializedName;

public class PayJson {


    /**
     * data : {"actInfo":"","biz_content":"","fmId":"SHT1A1439O1336764703","msg":"","pay_acount":"","pay_ebcode":10004,"pay_id":"微信APP支付","pay_order":{"appid":"wx038388890192c0bb","mch_id":"1485850592","nonce_str":"-1729301640","package":"Sign=WXPay","prepay_id":"wx20170720155908dbc919243c0431713106","sign":"3732646067ECA2D51136780D0159BEF0","timestamp":"1500537549","trade_type":""},"pay_transId":"81562997864005890","statusCode":100}
     * errcode : 100
     * errmsg : 订单支付成功
     */

    private DataBean data;
    private int errcode;
    private String errmsg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public static class DataBean {
        /**
         * actInfo :
         * biz_content :
         * fmId : SHT1A1439O1336764703
         * msg :
         * pay_acount :
         * pay_ebcode : 10004
         * pay_id : 微信APP支付
         * pay_order : {"appid":"wx038388890192c0bb","mch_id":"1485850592","nonce_str":"-1729301640","package":"Sign=WXPay","prepay_id":"wx20170720155908dbc919243c0431713106","sign":"3732646067ECA2D51136780D0159BEF0","timestamp":"1500537549","trade_type":""}
         * pay_transId : 81562997864005890
         * statusCode : 100
         */

        private String actInfo;
        private String biz_content;
        private String fmId;
        private String msg;
        private String pay_acount;
        private int pay_ebcode;
        private String pay_id;
        private PayOrderBean pay_order;
        private String pay_transId;
        private int statusCode;

        public String getActInfo() {
            return actInfo;
        }

        public void setActInfo(String actInfo) {
            this.actInfo = actInfo;
        }

        public String getBiz_content() {
            return biz_content;
        }

        public void setBiz_content(String biz_content) {
            this.biz_content = biz_content;
        }

        public String getFmId() {
            return fmId;
        }

        public void setFmId(String fmId) {
            this.fmId = fmId;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getPay_acount() {
            return pay_acount;
        }

        public void setPay_acount(String pay_acount) {
            this.pay_acount = pay_acount;
        }

        public int getPay_ebcode() {
            return pay_ebcode;
        }

        public void setPay_ebcode(int pay_ebcode) {
            this.pay_ebcode = pay_ebcode;
        }

        public String getPay_id() {
            return pay_id;
        }

        public void setPay_id(String pay_id) {
            this.pay_id = pay_id;
        }

        public PayOrderBean getPay_order() {
            return pay_order;
        }

        public void setPay_order(PayOrderBean pay_order) {
            this.pay_order = pay_order;
        }

        public String getPay_transId() {
            return pay_transId;
        }

        public void setPay_transId(String pay_transId) {
            this.pay_transId = pay_transId;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public static class PayOrderBean {
            /**
             * appid : wx038388890192c0bb
             * mch_id : 1485850592                      partnerId
             * nonce_str : -1729301640
             * package : Sign=WXPay
             * prepay_id : wx20170720155908dbc919243c0431713106
             * sign : 3732646067ECA2D51136780D0159BEF0
             * timestamp : 1500537549
             * trade_type :
             */

            private String appid;
            private String mch_id;
            private String nonce_str;
            @SerializedName("package")
            private String packageX;
            private String prepay_id;
            private String sign;
            private String timestamp;
            private String trade_type;

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public String getMch_id() {
                return mch_id;
            }

            public void setMch_id(String mch_id) {
                this.mch_id = mch_id;
            }

            public String getNonce_str() {
                return nonce_str;
            }

            public void setNonce_str(String nonce_str) {
                this.nonce_str = nonce_str;
            }

            public String getPackageX() {
                return packageX;
            }

            public void setPackageX(String packageX) {
                this.packageX = packageX;
            }

            public String getPrepay_id() {
                return prepay_id;
            }

            public void setPrepay_id(String prepay_id) {
                this.prepay_id = prepay_id;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public String getTrade_type() {
                return trade_type;
            }

            public void setTrade_type(String trade_type) {
                this.trade_type = trade_type;
            }
        }
    }
}