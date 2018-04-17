package io.dcloud.H58E83894.data.instestlisten;

import java.util.List;

/**
 * Created by fire on 2018/1/11  14:28.
 */

public class AllInstListenData {

    /**
     * data : [{"id":"20485","pid":"0","catId":"412","name":"AaronKoblin_2011[巧妙地可视化人类活动]","title":"AaronKoblin_2011[巧妙地可视化人类活动]","image":"/files/attach/images/20180314/1521021905514649.jpg","createTime":"2018-03-14 18:03:14","sort":"20485","userId":"1","viewCount":"0","show":"1","catName":"趣味听力练习","url":"/files/attach/file/20180314/1521021752539304.mp3","article":"123213123亲吻请问群翁群去而且"}]
     * code : 1
     * page : {"count":1,"page":"1","pageCount":0}
     */

    private int code;
    private PageBean page;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class PageBean {
        /**
         * count : 1
         * page : 1
         * pageCount : 0
         */

        private int count;
        private String page;
        private int pageCount;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }
    }

    public static class DataBean {
        /**
         * id : 20485
         * pid : 0
         * catId : 412
         * name : AaronKoblin_2011[巧妙地可视化人类活动]
         * title : AaronKoblin_2011[巧妙地可视化人类活动]
         * image : /files/attach/images/20180314/1521021905514649.jpg
         * createTime : 2018-03-14 18:03:14
         * sort : 20485
         * userId : 1
         * viewCount : 0
         * show : 1
         * catName : 趣味听力练习
         * url : /files/attach/file/20180314/1521021752539304.mp3
         * article : 123213123亲吻请问群翁群去而且
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
        private String url;
        private String article;

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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getArticle() {
            return article;
        }

        public void setArticle(String article) {
            this.article = article;
        }
    }
}
