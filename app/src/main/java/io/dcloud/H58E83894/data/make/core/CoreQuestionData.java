package io.dcloud.H58E83894.data.make.core;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fire on 2017/7/19  10:21.
 */

public class CoreQuestionData implements Parcelable {

    private String id;
    private String pid;
    private String catId;
    private String name;
    private String title;
    private String image;
    private String createTime;
    private String userId;
    private String viewCount;
    private String show;
    private String catName;
    private String duration;
    private String numbering;
    private String answer;
    private String alternatives;
    private String sentenceNumber;
    private String listeningFile;
    private String cnName;
    private String article;
    private String problemComplement;

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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getNumbering() {
        return numbering;
    }

    public void setNumbering(String numbering) {
        this.numbering = numbering;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(String alternatives) {
        this.alternatives = alternatives;
    }

    public String getSentenceNumber() {
        return sentenceNumber;
    }

    public void setSentenceNumber(String sentenceNumber) {
        this.sentenceNumber = sentenceNumber;
    }

    public String getListeningFile() {
        return listeningFile;
    }

    public void setListeningFile(String listeningFile) {
        this.listeningFile = listeningFile;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getProblemComplement() {
        return problemComplement;
    }

    public void setProblemComplement(String problemComplement) {
        this.problemComplement = problemComplement;
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
        dest.writeString(this.userId);
        dest.writeString(this.viewCount);
        dest.writeString(this.show);
        dest.writeString(this.catName);
        dest.writeString(this.duration);
        dest.writeString(this.numbering);
        dest.writeString(this.answer);
        dest.writeString(this.alternatives);
        dest.writeString(this.sentenceNumber);
        dest.writeString(this.listeningFile);
        dest.writeString(this.cnName);
        dest.writeString(this.article);
        dest.writeString(this.problemComplement);
    }

    public CoreQuestionData() {
    }

    protected CoreQuestionData(Parcel in) {
        this.id = in.readString();
        this.pid = in.readString();
        this.catId = in.readString();
        this.name = in.readString();
        this.title = in.readString();
        this.image = in.readString();
        this.createTime = in.readString();
        this.userId = in.readString();
        this.viewCount = in.readString();
        this.show = in.readString();
        this.catName = in.readString();
        this.duration = in.readString();
        this.numbering = in.readString();
        this.answer = in.readString();
        this.alternatives = in.readString();
        this.sentenceNumber = in.readString();
        this.listeningFile = in.readString();
        this.cnName = in.readString();
        this.article = in.readString();
        this.problemComplement = in.readString();
    }

    public static final Parcelable.Creator<CoreQuestionData> CREATOR = new Parcelable.Creator<CoreQuestionData>() {
        @Override
        public CoreQuestionData createFromParcel(Parcel source) {
            return new CoreQuestionData(source);
        }

        @Override
        public CoreQuestionData[] newArray(int size) {
            return new CoreQuestionData[size];
        }
    };
}
