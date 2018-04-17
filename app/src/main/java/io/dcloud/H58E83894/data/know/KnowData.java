package io.dcloud.H58E83894.data.know;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import io.dcloud.H58E83894.data.circle.CommunityData;

public class KnowData implements Parcelable {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String catid;
    private String catapp;
    private String catlite;
    private String catname;
    private String catimg;
    private String caturl;
    private String catuseurl;
    private String catparent;
    private String catdes;
    private String cattpl;
    private String catmanager;
    private String catinmenu;
    private String catindex;
    private String name;
    private String count;
    private String num;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public CommunityData getData() {
        return data;
    }

    public void setData(CommunityData data) {
        this.data = data;
    }

    private CommunityData data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private List<CategoryTypeBean> categoryType;
    private int sum;
    private int views;

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getCatapp() {
        return catapp;
    }

    public void setCatapp(String catapp) {
        this.catapp = catapp;
    }

    public String getCatlite() {
        return catlite;
    }

    public void setCatlite(String catlite) {
        this.catlite = catlite;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getCatimg() {
        return catimg;
    }

    public void setCatimg(String catimg) {
        this.catimg = catimg;
    }

    public String getCaturl() {
        return caturl;
    }

    public void setCaturl(String caturl) {
        this.caturl = caturl;
    }

    public String getCatuseurl() {
        return catuseurl;
    }

    public void setCatuseurl(String catuseurl) {
        this.catuseurl = catuseurl;
    }

    public String getCatparent() {
        return catparent;
    }

    public void setCatparent(String catparent) {
        this.catparent = catparent;
    }

    public String getCatdes() {
        return catdes;
    }

    public void setCatdes(String catdes) {
        this.catdes = catdes;
    }

    public String getCattpl() {
        return cattpl;
    }

    public void setCattpl(String cattpl) {
        this.cattpl = cattpl;
    }

    public String getCatmanager() {
        return catmanager;
    }

    public void setCatmanager(String catmanager) {
        this.catmanager = catmanager;
    }

    public String getCatinmenu() {
        return catinmenu;
    }

    public void setCatinmenu(String catinmenu) {
        this.catinmenu = catinmenu;
    }

    public String getCatindex() {
        return catindex;
    }

    public void setCatindex(String catindex) {
        this.catindex = catindex;
    }

    public List<CategoryTypeBean> getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(List<CategoryTypeBean> categoryType) {
        this.categoryType = categoryType;
    }

    public static class CategoryTypeBean implements Parcelable {

        private String catid;
        private String catapp;
        private String catlite;
        private String catname;
        private String catimg;
        private String caturl;
        private String catuseurl;
        private String catparent;
        private String catdes;
        private String cattpl;
        private String catmanager;
        private String catinmenu;
        private String catindex;
        private List<CategoryContentBean> categoryContent;

        public String getCatid() {
            return catid;
        }

        public void setCatid(String catid) {
            this.catid = catid;
        }

        public String getCatapp() {
            return catapp;
        }

        public void setCatapp(String catapp) {
            this.catapp = catapp;
        }

        public String getCatlite() {
            return catlite;
        }

        public void setCatlite(String catlite) {
            this.catlite = catlite;
        }

        public String getCatname() {
            return catname;
        }

        public void setCatname(String catname) {
            this.catname = catname;
        }

        public String getCatimg() {
            return catimg;
        }

        public void setCatimg(String catimg) {
            this.catimg = catimg;
        }

        public String getCaturl() {
            return caturl;
        }

        public void setCaturl(String caturl) {
            this.caturl = caturl;
        }

        public String getCatuseurl() {
            return catuseurl;
        }

        public void setCatuseurl(String catuseurl) {
            this.catuseurl = catuseurl;
        }

        public String getCatparent() {
            return catparent;
        }

        public void setCatparent(String catparent) {
            this.catparent = catparent;
        }

        public String getCatdes() {
            return catdes;
        }

        public void setCatdes(String catdes) {
            this.catdes = catdes;
        }

        public String getCattpl() {
            return cattpl;
        }

        public void setCattpl(String cattpl) {
            this.cattpl = cattpl;
        }

        public String getCatmanager() {
            return catmanager;
        }

        public void setCatmanager(String catmanager) {
            this.catmanager = catmanager;
        }

        public String getCatinmenu() {
            return catinmenu;
        }

        public void setCatinmenu(String catinmenu) {
            this.catinmenu = catinmenu;
        }

        public String getCatindex() {
            return catindex;
        }

        public void setCatindex(String catindex) {
            this.catindex = catindex;
        }

        public List<CategoryContentBean> getCategoryContent() {
            return categoryContent;
        }

        public void setCategoryContent(List<CategoryContentBean> categoryContent) {
            this.categoryContent = categoryContent;
        }

        public static class CategoryContentBean implements Parcelable {

            private String contentid;
            private String contenttitle;

            public String getContentid() {
                return contentid;
            }

            public void setContentid(String contentid) {
                this.contentid = contentid;
            }

            public String getContenttitle() {
                return contenttitle;
            }

            public void setContenttitle(String contenttitle) {
                this.contenttitle = contenttitle;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.contentid);
                dest.writeString(this.contenttitle);
            }

            public CategoryContentBean() {
            }

            protected CategoryContentBean(Parcel in) {
                this.contentid = in.readString();
                this.contenttitle = in.readString();
            }

            public static final Creator<CategoryContentBean> CREATOR = new Creator<CategoryContentBean>() {
                @Override
                public CategoryContentBean createFromParcel(Parcel source) {
                    return new CategoryContentBean(source);
                }

                @Override
                public CategoryContentBean[] newArray(int size) {
                    return new CategoryContentBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.catid);
            dest.writeString(this.catapp);
            dest.writeString(this.catlite);
            dest.writeString(this.catname);
            dest.writeString(this.catimg);
            dest.writeString(this.caturl);
            dest.writeString(this.catuseurl);
            dest.writeString(this.catparent);
            dest.writeString(this.catdes);
            dest.writeString(this.cattpl);
            dest.writeString(this.catmanager);
            dest.writeString(this.catinmenu);
            dest.writeString(this.catindex);
            dest.writeList(this.categoryContent);
        }

        public CategoryTypeBean() {
        }

        protected CategoryTypeBean(Parcel in) {
            this.catid = in.readString();
            this.catapp = in.readString();
            this.catlite = in.readString();
            this.catname = in.readString();
            this.catimg = in.readString();
            this.caturl = in.readString();
            this.catuseurl = in.readString();
            this.catparent = in.readString();
            this.catdes = in.readString();
            this.cattpl = in.readString();
            this.catmanager = in.readString();
            this.catinmenu = in.readString();
            this.catindex = in.readString();
            this.categoryContent = new ArrayList<CategoryContentBean>();
            in.readList(this.categoryContent, CategoryContentBean.class.getClassLoader());
        }

        public static final Creator<CategoryTypeBean> CREATOR = new Creator<CategoryTypeBean>() {
            @Override
            public CategoryTypeBean createFromParcel(Parcel source) {
                return new CategoryTypeBean(source);
            }

            @Override
            public CategoryTypeBean[] newArray(int size) {
                return new CategoryTypeBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.catid);
        dest.writeString(this.catapp);
        dest.writeString(this.catlite);
        dest.writeString(this.catname);
        dest.writeString(this.catimg);
        dest.writeString(this.caturl);
        dest.writeString(this.catuseurl);
        dest.writeString(this.catparent);
        dest.writeString(this.catdes);
        dest.writeString(this.cattpl);
        dest.writeString(this.catmanager);
        dest.writeString(this.catinmenu);
        dest.writeString(this.catindex);
        dest.writeTypedList(this.categoryType);
        dest.writeInt(this.sum);
        dest.writeInt(this.views);
    }

    public KnowData() {
    }

    protected KnowData(Parcel in) {
        this.catid = in.readString();
        this.catapp = in.readString();
        this.catlite = in.readString();
        this.catname = in.readString();
        this.catimg = in.readString();
        this.caturl = in.readString();
        this.catuseurl = in.readString();
        this.catparent = in.readString();
        this.catdes = in.readString();
        this.cattpl = in.readString();
        this.catmanager = in.readString();
        this.catinmenu = in.readString();
        this.catindex = in.readString();
        this.categoryType = in.createTypedArrayList(CategoryTypeBean.CREATOR);
        this.sum = in.readInt();
        this.views = in.readInt();
    }

    public static final Creator<KnowData> CREATOR = new Creator<KnowData>() {
        @Override
        public KnowData createFromParcel(Parcel source) {
            return new KnowData(source);
        }

        @Override
        public KnowData[] newArray(int size) {
            return new KnowData[size];
        }
    };
}
