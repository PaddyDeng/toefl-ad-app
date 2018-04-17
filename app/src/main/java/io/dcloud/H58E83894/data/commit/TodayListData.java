package io.dcloud.H58E83894.data.commit;


import java.util.List;

import io.reactivex.disposables.Disposable;

public class TodayListData {


    /**
     * data : [{"answer":"http://tf.cn/cn/app-api/da","status":"1","score":null,"author":null,"contentId":"18802"}]
     * code : 1
     * question : Talk about a time when you accomplished something you did not think you could do. What do you accomplish? Why did you think you could not do it?
     */

    private int code;
    private String question;
    private List<DataBean> data;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(String isComplete) {
        this.isComplete = isComplete;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    private String isComplete;
    private String file;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * answer : http://tf.cn/cn/app-api/da
         * status : 1
         * score : null
         * author : null
         * contentId : 18802
         *
         */

        private String answer;
        private String status;
        private String score;
        private String image;
        private String teacher;
       public Disposable mDisposable;

        public Disposable getmDisposable() {
            return mDisposable;
        }

        public void setmDisposable(Disposable mDisposable) {
            this.mDisposable = mDisposable;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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

        private String comment;

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        private String author;
        private String contentId;

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


        public String getContentId() {
            return contentId;
        }

        public void setContentId(String contentId) {
            this.contentId = contentId;
        }
    }
}
