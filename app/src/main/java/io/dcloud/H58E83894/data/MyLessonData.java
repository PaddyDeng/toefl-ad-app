package io.dcloud.H58E83894.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by fire on 2017/8/11  14:28.
 */

public class MyLessonData implements Parcelable {


    /**
     * data : [{"id":"444","orderId":"846","contentId":"19708","image":"/files/attach/images/20170420/1492668616627351.jpg","contentName":"TOP50精讲解析课","catName":"在线课程","contentTag":"30课时以下,录播课程","num":"1","price":"880.00","userId":"15294","createTime":"1517210879","type":"0","expireTime":"1525017600","datails":[{"id":"85","cid":"19708","name":"雷哥托福TPO50阅读解析课3课时Bella ","sdk":"Zs9KxNKsp9","pwd":"702570 ","fileAddress":null,"createTime":"1514696491"}]}]
     * pageStr : <li class="iPage on">1</li>
     */

    private String pageStr;

    public int getCountPage() {
        return countPage;
    }

    public void setCountPage(int countPage) {
        this.countPage = countPage;
    }

    private int countPage;
    private List<DataBean> data;

    protected MyLessonData(Parcel in) {
        pageStr = in.readString();

    }

    public static final Creator<MyLessonData> CREATOR = new Creator<MyLessonData>() {
        @Override
        public MyLessonData createFromParcel(Parcel in) {
            return new MyLessonData(in);
        }

        @Override
        public MyLessonData[] newArray(int size) {
            return new MyLessonData[size];
        }
    };

    public String getPageStr() {
        return pageStr;
    }

    public void setPageStr(String pageStr) {
        this.pageStr = pageStr;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pageStr);


    }

    public static class DataBean implements  Parcelable{
        /**
         * id : 444
         * orderId : 846
         * contentId : 19708
         * image : /files/attach/images/20170420/1492668616627351.jpg
         * contentName : TOP50精讲解析课
         * catName : 在线课程
         * contentTag : 30课时以下,录播课程
         * num : 1
         * price : 880.00
         * userId : 15294
         * createTime : 1517210879
         * type : 0
         *expire Time : 1525017600
         * datails : [{"id":"85","cid":"19708","name":"雷哥托福TPO50阅读解析课3课时Bella ","sdk":"Zs9KxNKsp9","pwd":"702570 ","fileAddress":null,"createTime":"1514696491"}]
         */

        private String id;
        private String orderId;
        private String contentId;
        private String image;
        private String contentName;
        private String catName;
        private String contentTag;
        private String num;
        private String price;
        private String userId;
        private String createTime;
        private String type;
        private String expireTime;
        private List<DatailsBean> datails;

        protected DataBean(Parcel in) {
            id = in.readString();
            orderId = in.readString();
            contentId = in.readString();
            image = in.readString();
            contentName = in.readString();
            catName = in.readString();
            contentTag = in.readString();
            num = in.readString();
            price = in.readString();
            userId = in.readString();
            createTime = in.readString();
            type = in.readString();
            expireTime = in.readString();
            datails = in.createTypedArrayList(DatailsBean.CREATOR);
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getContentId() {
            return contentId;
        }

        public void setContentId(String contentId) {
            this.contentId = contentId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getContentName() {
            return contentName;
        }

        public void setContentName(String contentName) {
            this.contentName = contentName;
        }

        public String getCatName() {
            return catName;
        }

        public void setCatName(String catName) {
            this.catName = catName;
        }

        public String getContentTag() {
            return contentTag;
        }

        public void setContentTag(String contentTag) {
            this.contentTag = contentTag;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }

        public List<DatailsBean> getDatails() {
            return datails;
        }

        public void setDatails(List<DatailsBean> datails) {
            this.datails = datails;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(orderId);
            dest.writeString(contentId);
            dest.writeString(image);
            dest.writeString(contentName);
            dest.writeString(catName);
            dest.writeString(contentTag);
            dest.writeString(num);
            dest.writeString(price);
            dest.writeString(userId);
            dest.writeString(createTime);
            dest.writeString(type);
            dest.writeString(expireTime);
            dest.writeTypedList(datails);
        }

        public static class DatailsBean  implements Parcelable{
//            http://order.gmatonline.cn/pay/video/video?id=444&videoId=85
            /**
             * id : 85
             * cid : 19708
             * name : 雷哥托福TPO50阅读解析课3课时Bella
             * sdk : Zs9KxNKsp9
             * pwd : 702570
             * fileAddress : null
             * createTime : 1514696491
             */

            private String id;
            private String cid;
            private String name;
            private String sdk;
            private String pwd;
            private Object fileAddress;
            private String createTime;

            protected DatailsBean(Parcel in) {
                id = in.readString();
                cid = in.readString();
                name = in.readString();
                sdk = in.readString();
                pwd = in.readString();
                createTime = in.readString();
            }

            public static final Creator<DatailsBean> CREATOR = new Creator<DatailsBean>() {
                @Override
                public DatailsBean createFromParcel(Parcel in) {
                    return new DatailsBean(in);
                }

                @Override
                public DatailsBean[] newArray(int size) {
                    return new DatailsBean[size];
                }
            };

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSdk() {
                return sdk;
            }

            public void setSdk(String sdk) {
                this.sdk = sdk;
            }

            public String getPwd() {
                return pwd;
            }

            public void setPwd(String pwd) {
                this.pwd = pwd;
            }

            public Object getFileAddress() {
                return fileAddress;
            }

            public void setFileAddress(Object fileAddress) {
                this.fileAddress = fileAddress;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(id);
                dest.writeString(cid);
                dest.writeString(name);
                dest.writeString(sdk);
                dest.writeString(pwd);
                dest.writeString(createTime);
            }
        }
    }
}
