package io.dcloud.H58E83894.data.make;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fire on 2017/8/3  11:34.
 */

public class ReadQuestionData implements Parcelable {
    private String count;
    private String contentId;
    private ReadQuestion question;
    private String nextId;
    private String lastId;
    private ReadArticle article;
    private String userAanswer;

    @Override
    public String toString() {
        return "ReadQuestionData{" +
                "count='" + count + '\'' +
                ", contentId='" + contentId + '\'' +
                ", question=" + question +
                ", nextId='" + nextId + '\'' +
                ", lastId='" + lastId + '\'' +
                ", article=" + article +
                ", userAanswer='" + userAanswer + '\'' +
                ", upId='" + upId + '\'' +
                '}';
    }

    private String upId;

    public String getUserAanswer() {
        return userAanswer;
    }

    public void setUserAanswer(String userAanswer) {
        this.userAanswer = userAanswer;
    }

    public String getUpId() {
        return upId;
    }
    public void setUpId(String upId) {
        this.upId = upId;
    }

    public String getLastId() {
        return lastId;
    }

    public void setLastId(String lastId) {
        this.lastId = lastId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public ReadQuestion getQuestion() {
        return question;
    }

    public void setQuestion(ReadQuestion question) {
        this.question = question;
    }

    public String getNextId() {
        return nextId;
    }

    public void setNextId(String nextId) {
        this.nextId = nextId;
    }

    public ReadArticle getArticle() {
        return article;
    }

    public void setArticle(ReadArticle article) {
        this.article = article;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.count);
        dest.writeString(this.contentId);
        dest.writeParcelable(this.question, flags);
        dest.writeString(this.nextId);
        dest.writeString(this.lastId);
        dest.writeParcelable(this.article, flags);
    }

    public ReadQuestionData() {
    }

    protected ReadQuestionData(Parcel in) {
        this.count = in.readString();
        this.contentId = in.readString();
        this.question = in.readParcelable(ReadQuestion.class.getClassLoader());
        this.nextId = in.readString();
        this.lastId = in.readString();
        this.article = in.readParcelable(ReadArticle.class.getClassLoader());
    }

    public static final Parcelable.Creator<ReadQuestionData> CREATOR = new Parcelable.Creator<ReadQuestionData>() {
        @Override
        public ReadQuestionData createFromParcel(Parcel source) {
            return new ReadQuestionData(source);
        }

        @Override
        public ReadQuestionData[] newArray(int size) {
            return new ReadQuestionData[size];
        }
    };
}
