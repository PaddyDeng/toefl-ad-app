package io.dcloud.H58E83894.data.prelesson;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fire on 2017/7/21  14:16.
 */

public class TeacherItemBean implements Parcelable {

    private String id;
    private String pid;
    private String catId;
    private String name;
    private String title;
    private String image;
    private String createTime;
    private String sort;
    private String userId;
    private String viewCount;
    private String show;
    private String catName;

    private String numbering;

    private String description;
    private String performance;
    private String speak;

    public String getNumbering() {
        return numbering;
    }

    public void setNumbering(String numbering) {
        this.numbering = numbering;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getSpeak() {
        return speak;
    }

    public void setSpeak(String speak) {
        this.speak = speak;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.pid);
        dest.writeString(this.catId);
        dest.writeString(this.name);
        dest.writeString(this.title);
        dest.writeString(this.image);
        dest.writeString(this.createTime);
        dest.writeString(this.sort);
        dest.writeString(this.userId);
        dest.writeString(this.viewCount);
        dest.writeString(this.show);
        dest.writeString(this.catName);
        dest.writeString(this.numbering);
        dest.writeString(this.description);
        dest.writeString(this.performance);
        dest.writeString(this.speak);
    }

    public TeacherItemBean() {
    }

    protected TeacherItemBean(Parcel in) {
        this.id = in.readString();
        this.pid = in.readString();
        this.catId = in.readString();
        this.name = in.readString();
        this.title = in.readString();
        this.image = in.readString();
        this.createTime = in.readString();
        this.sort = in.readString();
        this.userId = in.readString();
        this.viewCount = in.readString();
        this.show = in.readString();
        this.catName = in.readString();
        this.numbering = in.readString();
        this.description = in.readString();
        this.performance = in.readString();
        this.speak = in.readString();
    }

    public static final Parcelable.Creator<TeacherItemBean> CREATOR = new Parcelable.Creator<TeacherItemBean>() {
        @Override
        public TeacherItemBean createFromParcel(Parcel source) {
            return new TeacherItemBean(source);
        }

        @Override
        public TeacherItemBean[] newArray(int size) {
            return new TeacherItemBean[size];
        }
    };
}
