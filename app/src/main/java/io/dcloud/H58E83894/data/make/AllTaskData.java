package io.dcloud.H58E83894.data.make;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by fire on 2017/8/14  09:30.
 */

public class AllTaskData implements Serializable{


    /**
     * data : [{"details":[{"answer":"/files/upload/spoken/5aaa4a0a6b96b.mp3","status":"1","score":null,"teacher":null,"comment":null,"author":"哈哈哈239","image":null,"contentId":"20491"}],"count":0,"time":"03月15日","question":"If you could have any job or career you wanted, which would you choose and why? Give specific details to explain your response. "}]
     * code : 1
     */

    private int code;
    private List<DataBean> data;

    protected AllTaskData(Parcel in) {
        code = in.readInt();
    }



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }



    public static class DataBean implements Serializable{
        /**
         * details : [{"answer":"/files/upload/spoken/5aaa4a0a6b96b.mp3","status":"1","score":null,"teacher":null,"comment":null,"author":"哈哈哈239","image":null,"contentId":"20491"}]
         * count : 0
         * time : 03月15日
         * question : If you could have any job or career you wanted, which would you choose and why? Give specific details to explain your response.
         */

        private int count;
        private String time;



        @Override
        public String toString() {
            return "DataBean{" +
                    "count=" + count +
                    ", time='" + time + '\'' +
                    ", question='" + question + '\'' +
                    ", details=" + details +
                    '}';
        }

        private String question;
        private List<DetailsBean> details;




        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public List<DetailsBean> getDetails() {
            return details;
        }

        public void setDetails(List<DetailsBean> details) {
            this.details = details;
        }


        public static class DetailsBean implements Serializable{
            /**
             * answer : /files/upload/spoken/5aaa4a0a6b96b.mp3
             * status : 1
             * score : null
             * teacher : null
             * comment : null
             * author : 哈哈哈239
             * image : null
             * contentId : 20491
             */

            private String answer;
            private String status;
            private String score;
            private String teacher;



            @Override
            public String toString() {
                return "DetailsBean{" +
                        "answer='" + answer + '\'' +
                        ", status='" + status + '\'' +
                        ", score='" + score + '\'' +
                        ", teacher='" + teacher + '\'' +
                        ", comment='" + comment + '\'' +
                        ", author='" + author + '\'' +
                        ", image='" + image + '\'' +
                        ", mDisposable=" + mDisposable +
                        ", contentId='" + contentId + '\'' +
                        '}';
            }

            private String comment;
            private String author;
            private String image;

            protected DetailsBean(Parcel in) {
                answer = in.readString();
                status = in.readString();
                score = in.readString();
                teacher = in.readString();
                comment = in.readString();
                author = in.readString();
                image = in.readString();
                contentId = in.readString();
            }


            public Disposable getmDisposable() {
                return mDisposable;
            }

            public void setmDisposable(Disposable mDisposable) {
                this.mDisposable = mDisposable;
            }

            public Disposable mDisposable;

            public String getAnswer() {
                return answer;
            }

            public void setAnswer(String answer) {
                this.answer = answer;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public String getTeacher() {
                return teacher;
            }

            public void setTeacher(String teacher) {
                this.teacher = teacher;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getContentId() {
                return contentId;
            }

            public void setContentId(String contentId) {
                this.contentId = contentId;
            }

            private String contentId;


        }
    }
}
