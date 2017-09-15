package io.dcloud.H58E83894.data.make;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fire on 2017/8/3  11:39.
 */

public class ReadArticle implements Parcelable {
    private String question;
    private String title;
    private String name;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.question);
        dest.writeString(this.title);
        dest.writeString(this.name);
    }

    public ReadArticle() {
    }

    protected ReadArticle(Parcel in) {
        this.question = in.readString();
        this.title = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<ReadArticle> CREATOR = new Parcelable.Creator<ReadArticle>() {
        @Override
        public ReadArticle createFromParcel(Parcel source) {
            return new ReadArticle(source);
        }

        @Override
        public ReadArticle[] newArray(int size) {
            return new ReadArticle[size];
        }
    };
}
