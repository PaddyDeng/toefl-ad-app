package io.dcloud.H58E83894.data.make;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by fire on 2017/7/24  10:50.
 */

public class PracticeQuestionData implements Parcelable {
    private List<SentenceData> sentence;
    private AudioData audio;

    public AudioData getAudio() {
        return audio;
    }

    public void setAudio(AudioData audio) {
        this.audio = audio;
    }

    public List<SentenceData> getSentence() {
        return sentence;
    }

    public void setSentence(List<SentenceData> sentence) {
        this.sentence = sentence;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.sentence);
        dest.writeParcelable(this.audio, flags);
    }

    public PracticeQuestionData() {
    }

    protected PracticeQuestionData(Parcel in) {
        this.sentence = in.createTypedArrayList(SentenceData.CREATOR);
        this.audio = in.readParcelable(AudioData.class.getClassLoader());
    }

    public static final Parcelable.Creator<PracticeQuestionData> CREATOR = new Parcelable.Creator<PracticeQuestionData>() {
        @Override
        public PracticeQuestionData createFromParcel(Parcel source) {
            return new PracticeQuestionData(source);
        }

        @Override
        public PracticeQuestionData[] newArray(int size) {
            return new PracticeQuestionData[size];
        }
    };
}
