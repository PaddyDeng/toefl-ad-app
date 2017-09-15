package io.dcloud.H58E83894.data.make;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fire on 2017/7/28  09:25.
 */

public class SpeakData implements Parcelable {

    private int allPageSize;
    private int currentPage;
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
    private String file;
    private String article;
    private String description;
    private String articleFile;
    private String question;
    private String questionFile;

    public int getAllPageSize() {
        return allPageSize;
    }

    public void setAllPageSize(int allPageSize) {
        this.allPageSize = allPageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArticleFile() {
        return articleFile;
    }

    public void setArticleFile(String articleFile) {
        this.articleFile = articleFile;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionFile() {
        return questionFile;
    }

    public void setQuestionFile(String questionFile) {
        this.questionFile = questionFile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.allPageSize);
        dest.writeInt(this.currentPage);
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
        dest.writeString(this.file);
        dest.writeString(this.article);
        dest.writeString(this.description);
        dest.writeString(this.articleFile);
        dest.writeString(this.question);
        dest.writeString(this.questionFile);
    }

    public SpeakData() {
    }

    protected SpeakData(Parcel in) {
        this.allPageSize = in.readInt();
        this.currentPage = in.readInt();
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
        this.file = in.readString();
        this.article = in.readString();
        this.description = in.readString();
        this.articleFile = in.readString();
        this.question = in.readString();
        this.questionFile = in.readString();
    }

    public static final Parcelable.Creator<SpeakData> CREATOR = new Parcelable.Creator<SpeakData>() {
        @Override
        public SpeakData createFromParcel(Parcel source) {
            return new SpeakData(source);
        }

        @Override
        public SpeakData[] newArray(int size) {
            return new SpeakData[size];
        }
    };
}
