package io.dcloud.H58E83894.data.make;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fire on 2017/8/3  11:37.
 */

public class ReadQuestion implements Parcelable {
    private String pid;
    private String title;
    private String question;
    private String answerA;
    private String answerB;
    private String answer;
    private String postionD;
    private String postionW;
    private String hint;

    private String questionType;

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPostionD() {
        return postionD;
    }

    public void setPostionD(String postionD) {
        this.postionD = postionD;
    }

    public String getPostionW() {
        return postionW;
    }

    public void setPostionW(String postionW) {
        this.postionW = postionW;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
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
        dest.writeString(this.pid);
        dest.writeString(this.title);
        dest.writeString(this.question);
        dest.writeString(this.answerA);
        dest.writeString(this.answerB);
        dest.writeString(this.answer);
        dest.writeString(this.questionType);
        dest.writeString(this.postionD);
        dest.writeString(this.postionW);
        dest.writeString(this.hint);
    }

    public ReadQuestion() {
    }

    protected ReadQuestion(Parcel in) {
        this.pid = in.readString();
        this.title = in.readString();
        this.question = in.readString();
        this.answerA = in.readString();
        this.answerB = in.readString();
        this.answer = in.readString();
        this.questionType = in.readString();
        this.postionD = in.readString();
        this.postionW = in.readString();
        this.hint = in.readString();
    }

    public static final Parcelable.Creator<ReadQuestion> CREATOR = new Parcelable.Creator<ReadQuestion>() {
        @Override
        public ReadQuestion createFromParcel(Parcel source) {
            return new ReadQuestion(source);
        }

        @Override
        public ReadQuestion[] newArray(int size) {
            return new ReadQuestion[size];
        }
    };
}
