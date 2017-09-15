package io.dcloud.H58E83894.data.make;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by fire on 2017/8/7  15:43.
 */

public class ListenTpoContentData {

    private String id;
    private String image;
    private String catId;
    private String name;
    private String title;
    private String catName;
    private String file;
    private String text;
    public Disposable mDisposable;
    private List<ListenChildData> child;
    private List<ListenSecRecordData> record;

    public List<ListenSecRecordData> getRecord() {
        return record;
    }

    public void setRecord(List<ListenSecRecordData> record) {
        this.record = record;
    }

    public List<ListenChildData> getChild() {
        return child;
    }

    public void setChild(List<ListenChildData> child) {
        this.child = child;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
