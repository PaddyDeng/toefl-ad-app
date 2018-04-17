package io.dcloud.H58E83894.data.circle;

import java.util.List;

/**
 * Created by fire on 2017/7/14  14:35.
 */

public class CommunityData {


    private String id;
    private String title;
    private String content;
    private String cnContent;
    private String uid;
    private String createTime;
    private String dateTime;
    private String hot;
    private String catId;
    private String viewCount;
    private String radioTitle;
    private String username;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

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

    public String getCnContent() {
        return cnContent;
    }

    public void setCnContent(String cnContent) {
        this.cnContent = cnContent;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getRadioTitle() {
        return radioTitle;
    }

    public void setRadioTitle(String radioTitle) {
        this.radioTitle = radioTitle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public int getIsReply() {
        return isReply;
    }

    public void setIsReply(int isReply) {
        this.isReply = isReply;
    }

    public List<String> getDatum() {
        return datum;
    }

    public void setDatum(List<String> datum) {
        this.datum = datum;
    }

    public List<String> getDatumTitle() {
        return datumTitle;
    }

    public void setDatumTitle(List<String> datumTitle) {
        this.datumTitle = datumTitle;
    }

    public List<ReplyData> getReply() {
        return Reply;
    }

    public void setReply(List<ReplyData> reply) {
        Reply = reply;
    }

    private String nickname;
    private Object image;
    private String article;
    private int isReply;//1可以去下载
    private List<String> datum;
    private List<String> datumTitle;
    private List<ReplyData> Reply;


}
