package io.dcloud.H58E83894.data.make;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fire on 2017/7/24  10:13.
 */

public class SentenceData implements Parcelable {

    private String id;
    private String content;
    private String cnSentence;
    private int parent;
    private int seq;
    private String start_time;
    private String audio_time;
    private int section;
    private String fav;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCnSentence() {
        return cnSentence;
    }

    public void setCnSentence(String cnSentence) {
        this.cnSentence = cnSentence;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getAudio_time() {
        return audio_time;
    }

    public void setAudio_time(String audio_time) {
        this.audio_time = audio_time;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.content);
        dest.writeString(this.cnSentence);
        dest.writeInt(this.parent);
        dest.writeInt(this.seq);
        dest.writeString(this.start_time);
        dest.writeString(this.audio_time);
        dest.writeInt(this.section);
        dest.writeString(this.fav);
    }

    public SentenceData() {
    }

    protected SentenceData(Parcel in) {
        this.id = in.readString();
        this.content = in.readString();
        this.cnSentence = in.readString();
        this.parent = in.readInt();
        this.seq = in.readInt();
        this.start_time = in.readString();
        this.audio_time = in.readString();
        this.section = in.readInt();
        this.fav = in.readString();
    }

    public static final Parcelable.Creator<SentenceData> CREATOR = new Parcelable.Creator<SentenceData>() {
        @Override
        public SentenceData createFromParcel(Parcel source) {
            return new SentenceData(source);
        }

        @Override
        public SentenceData[] newArray(int size) {
            return new SentenceData[size];
        }
    };
}
