package io.dcloud.H58E83894.data.make;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by fire on 2017/8/14  09:30.
 */

public class OnlyMineData {


    /**
     * data : [{"id":"20483","pid":"0","catId":"420","name":"口语批改","title":null,"image":"","createTime":"2018-03-14 16:03:14","sort":"20483","userId":"19250","viewCount":"0","show":"1","catName":"口语点评","A":"","description":"","answer":"","alternatives":"","article":""}]
     * code : 1
     */

    private int code;
    private List<DataBean> data;

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

    public static class DataBean {
        /**
         * id : 20483
         * pid : 0
         * catId : 420
         * name : 口语批改
         * title : null
         * image :
         * createTime : 2018-03-14 16:03:14
         * sort : 20483
         * userId : 19250
         * viewCount : 0
         * show : 1
         * catName : 口语点评
         * A :
         * description :
         * answer :
         * alternatives :
         * article :
         */

        private String id;
        private String pid;
        private String catId;
        private String name;
        private String title;
        private String image;
        private String createTime;
        private String sort;
        private String userId;
        private String viewCount;
        private String show;
        private String catName;
        private String A;
        private String description;
        private String answer;
        private String alternatives;
        private String article;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        private String question;

        public Disposable getmDisposable() {
            return mDisposable;
        }

        public void setmDisposable(Disposable mDisposable) {
            this.mDisposable = mDisposable;
        }

        public Disposable mDisposable;

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

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
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

        public String getA() {
            return A;
        }

        public void setA(String A) {
            this.A = A;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getAlternatives() {
            return alternatives;
        }

        public void setAlternatives(String alternatives) {
            this.alternatives = alternatives;
        }

        public String getArticle() {
            return article;
        }

        public void setArticle(String article) {
            this.article = article;
        }
    }
}
