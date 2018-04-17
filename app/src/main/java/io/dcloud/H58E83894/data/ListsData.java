package io.dcloud.H58E83894.data;

import java.util.List;

/**
 * Created by fire on 2017/8/11  14:28.
 */

public class ListsData {


    /**
     * list : [[{"id":"20338","pid":"0","catId":"385","name":"托福口语词穷？不存在的！","title":"托福口语词穷？不存在的！","image":"/files/attach/images/20180203/1517649182125859.jpg","createTime":"2018-02-03 17:13:05","sort":"20338","userId":"1","viewCount":"785","show":"1","catName":"知识库","description":""}]]
     * count : 2
     * page : 1
     */

    private int count;
    private String page;
    private List<ListBean> list;

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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 20338
         * pid : 0
         * catId : 385
         * name : 托福口语词穷？不存在的！
         * title : 托福口语词穷？不存在的！
         * image : /files/attach/images/20180203/1517649182125859.jpg
         * createTime : 2018-02-03 17:13:05
         * sort : 20338
         * userId : 1
         * viewCount : 785
         * show : 1
         * catName : 知识库
         * description :
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
        private String description;

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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
