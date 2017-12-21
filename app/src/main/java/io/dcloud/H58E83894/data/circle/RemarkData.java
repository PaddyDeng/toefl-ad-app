package io.dcloud.H58E83894.data.circle;

import java.util.List;

/**
 * 评论数据？
 */

public class RemarkData {

    private String remarkNum;
    private String id;
    private String title;
    private String content;
    private String like;
    private String createTime;
    private String uid;
    private String type;
    private String icon;
    private String publisher;
    private String viewCount;
    private String belong;
    private boolean likeId;
    private String likeNum;
    private Object image;
    private List<ReplyBean> reply;
    private int recyclePosition;


    public String getRemarkNum() {
        return remarkNum;
    }

    public void setRemarkNum(String remarkNum) {
        this.remarkNum = remarkNum;
    }

    public int getRecyclePosition() {
        return recyclePosition;
    }

    public void setRecyclePosition(int recyclePosition) {
        this.recyclePosition = recyclePosition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public boolean isLikeId() {
        return likeId;
    }

    public void setLikeId(boolean likeId) {
        this.likeId = likeId;
    }

    public String getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public List<ReplyBean> getReply() {
        return reply;
    }

    public void setReply(List<ReplyBean> reply) {
        this.reply = reply;
    }

    public static class ReplyBean {

        private String id;
        private String content;
        private String replyUser;
        private String createTime;
        private String type;
        private String gossipId;
        private String uid;
        private String isLook;
        private String uName;
        private String replyUserName;
        private String userImage;

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

        public String getReplyUser() {
            return replyUser;
        }

        public void setReplyUser(String replyUser) {
            this.replyUser = replyUser;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getGossipId() {
            return gossipId;
        }

        public void setGossipId(String gossipId) {
            this.gossipId = gossipId;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getIsLook() {
            return isLook;
        }

        public void setIsLook(String isLook) {
            this.isLook = isLook;
        }

        public String getUName() {
            return uName;
        }

        public void setUName(String uName) {
            this.uName = uName;
        }

        public String getReplyUserName() {
            return replyUserName;
        }

        public void setReplyUserName(String replyUserName) {
            this.replyUserName = replyUserName;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

    }


}
