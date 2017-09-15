package io.dcloud.H58E83894.data.make;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fire on 2017/7/24  10:14.
 */

public class AudioData implements Parcelable {

    private String id;
    private String name;
    private String audio_time;
    private String content;
    private String title;
    private String formatType;
    private String filePath;
    private String picPath;
    private String html_content;
    private String type;

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

    public String getAudio_time() {
        return audio_time;
    }

    public void setAudio_time(String audio_time) {
        this.audio_time = audio_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) {
        this.formatType = formatType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getHtml_content() {
        return html_content;
    }

    public void setHtml_content(String html_content) {
        this.html_content = html_content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.audio_time);
        dest.writeString(this.content);
        dest.writeString(this.title);
        dest.writeString(this.formatType);
        dest.writeString(this.filePath);
        dest.writeString(this.picPath);
        dest.writeString(this.html_content);
        dest.writeString(this.type);
    }

    public AudioData() {
    }

    protected AudioData(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.audio_time = in.readString();
        this.content = in.readString();
        this.title = in.readString();
        this.formatType = in.readString();
        this.filePath = in.readString();
        this.picPath = in.readString();
        this.html_content = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<AudioData> CREATOR = new Parcelable.Creator<AudioData>() {
        @Override
        public AudioData createFromParcel(Parcel source) {
            return new AudioData(source);
        }

        @Override
        public AudioData[] newArray(int size) {
            return new AudioData[size];
        }
    };
}
