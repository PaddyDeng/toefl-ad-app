package io.dcloud.H58E83894.data.user;

import com.google.gson.annotations.SerializedName;

public class KnowListData{


    /**
     * data : {"0":{"id":"19725","pid":"0","catId":"385","name":"托福写作中，怎样花式举例子？","title":"托福写作中，怎样花式举例子？","image":"","createTime":"2018-01-10 15:25:12","sort":"19725","userId":"1","viewCount":"1","show":"1","catName":"知识库","article":""},"1":{"id":"19725","pid":"0","catId":"385","name":"托福写作中，怎样花式举例子？","title":"托福写作中，怎样花式举例子？","image":"","createTime":"2018-01-1 15:25:12","sort":"19725","userId":"1","viewCount":"1","show":"1","catName":"知识库","article":""},"2":{"id":"19725","pid":"0","catId":"385","name":"托福写作中，怎样花式举例子？","title":"托福写作中，怎样花式举例子？","image":"","createTime":"2018-01-10 15:25:12","sort":"19725","userId":"1","viewCount":"1","show":"1","catName":"知识库","article":""},"pageStr":"<li class=\"iPage on\">1<\/li>"}
     * code : 1
     */

    private DataBean data;
    private int code;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class DataBean {
        /**
         * 0 : {"id":"19725","pid":"0","catId":"385","name":"托福写作中，怎样花式举例子？","title":"托福写作中，怎样花式举例子？","image":"","createTime":"2018-01-10 15:25:12","sort":"19725","userId":"1","viewCount":"1","show":"1","catName":"知识库","article":""}
         * 1 : {"id":"19725","pid":"0","catId":"385","name":"托福写作中，怎样花式举例子？","title":"托福写作中，怎样花式举例子？","image":"","createTime":"2018-01-1 15:25:12","sort":"19725","userId":"1","viewCount":"1","show":"1","catName":"知识库","article":""}
         * 2 : {"id":"19725","pid":"0","catId":"385","name":"托福写作中，怎样花式举例子？","title":"托福写作中，怎样花式举例子？","image":"","createTime":"2018-01-10 15:25:12","sort":"19725","userId":"1","viewCount":"1","show":"1","catName":"知识库","article":""}
         * pageStr : <li class="iPage on">1</li>
         */

        @SerializedName("0")
        private _$0Bean _$0;
        @SerializedName("1")
        private _$1Bean _$1;
        @SerializedName("2")
        private _$2Bean _$2;
        private String pageStr;

        public _$0Bean get_$0() {
            return _$0;
        }

        public void set_$0(_$0Bean _$0) {
            this._$0 = _$0;
        }

        public _$1Bean get_$1() {
            return _$1;
        }

        public void set_$1(_$1Bean _$1) {
            this._$1 = _$1;
        }

        public _$2Bean get_$2() {
            return _$2;
        }

        public void set_$2(_$2Bean _$2) {
            this._$2 = _$2;
        }

        public String getPageStr() {
            return pageStr;
        }

        public void setPageStr(String pageStr) {
            this.pageStr = pageStr;
        }

        public static class _$0Bean {
            /**
             * id : 19725
             * pid : 0
             * catId : 385
             * name : 托福写作中，怎样花式举例子？
             * title : 托福写作中，怎样花式举例子？
             * image :
             * createTime : 2018-01-10 15:25:12
             * sort : 19725
             * userId : 1
             * viewCount : 1
             * show : 1
             * catName : 知识库
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

            public String getArticle() {
                return article;
            }

            public void setArticle(String article) {
                this.article = article;
            }
        }

        public static class _$1Bean {
            /**
             * id : 19725
             * pid : 0
             * catId : 385
             * name : 托福写作中，怎样花式举例子？
             * title : 托福写作中，怎样花式举例子？
             * image :
             * createTime : 2018-01-1 15:25:12
             * sort : 19725
             * userId : 1
             * viewCount : 1
             * show : 1
             * catName : 知识库
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

            public String getArticle() {
                return article;
            }

            public void setArticle(String article) {
                this.article = article;
            }
        }

        public static class _$2Bean {
            /**
             * id : 19725
             * pid : 0
             * catId : 385
             * name : 托福写作中，怎样花式举例子？
             * title : 托福写作中，怎样花式举例子？
             * image :
             * createTime : 2018-01-10 15:25:12
             * sort : 19725
             * userId : 1
             * viewCount : 1
             * show : 1
             * catName : 知识库
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

            public String getArticle() {
                return article;
            }

            public void setArticle(String article) {
                this.article = article;
            }
        }
    }
}