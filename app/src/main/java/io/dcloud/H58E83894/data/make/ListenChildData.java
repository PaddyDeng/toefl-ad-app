package io.dcloud.H58E83894.data.make;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fire on 2017/8/8  10:59.
 */

public class ListenChildData implements Parcelable {

    private String id;
    private String name;
    private String fileAdd;
    private String questType;
    private String questSelect;
    private String file;
    private String answer;
    private String userChooseAnswer;

    public String getUserChooseAnswer() {
        return userChooseAnswer;
    }

    public void setUserChooseAnswer(String userAnswer) {
        this.userChooseAnswer = userAnswer;
    }

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

    public String getFileAdd() {
        return fileAdd;
    }

    public void setFileAdd(String fileAdd) {
        this.fileAdd = fileAdd;
    }

    public String getQuestType() {
        return questType;
    }

    public void setQuestType(String questType) {
        this.questType = questType;
    }

    public String getQuestSelect() {
        return questSelect;
    }

    public void setQuestSelect(String questSelect) {
        this.questSelect = questSelect;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.fileAdd);
        dest.writeString(this.questType);
        dest.writeString(this.questSelect);
        dest.writeString(this.file);
        dest.writeString(this.answer);
        dest.writeString(this.userChooseAnswer);
    }

    public ListenChildData() {
    }

    protected ListenChildData(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.fileAdd = in.readString();
        this.questType = in.readString();
        this.questSelect = in.readString();
        this.file = in.readString();
        this.answer = in.readString();
        this.userChooseAnswer = in.readString();
    }

    public static final Parcelable.Creator<ListenChildData> CREATOR = new Parcelable.Creator<ListenChildData>() {
        @Override
        public ListenChildData createFromParcel(Parcel source) {
            return new ListenChildData(source);
        }

        @Override
        public ListenChildData[] newArray(int size) {
            return new ListenChildData[size];
        }
    };
}
