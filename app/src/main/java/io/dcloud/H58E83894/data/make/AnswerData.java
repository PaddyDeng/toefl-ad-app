package io.dcloud.H58E83894.data.make;

/**
 * Created by fire on 2017/7/18  11:46.
 */

public class AnswerData {
    private String option;
    private String content;
    private String type;//选项类型  单选或多选
    private int chooseSer;//选择序号
    private boolean selected;
    private int control;//1正确 2错误  0默认，不管
    private String formOption;//表格类别用逗号分隔开的
    private String singleChooseAnswer;//用来记录表格选择的的选项
    private String correctOption;//记录表格选择的选项
    private String errorOption;//记录表格选择的选项
    private String readMultAnswer;//阅读type 7 下的答案
    private String readMultCorrectOption;//记录阅读type 7 正确答案选项

    public String getReadMultCorrectOption() {
        return readMultCorrectOption;
    }

    public void setReadMultCorrectOption(String readMultCorrectOption) {
        this.readMultCorrectOption = readMultCorrectOption;
    }

    public String getReadMultAnswer() {
        return readMultAnswer;
    }

    public void setReadMultAnswer(String readMultAnswer) {
        this.readMultAnswer = readMultAnswer;
    }

    public String getErrorOption() {
        return errorOption;
    }

    public void setErrorOption(String errorOption) {
        this.errorOption = errorOption;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public String getSingleChooseAnswer() {
        return singleChooseAnswer;
    }

    public void setSingleChooseAnswer(String singleChooseAnswer) {
        this.singleChooseAnswer = singleChooseAnswer;
    }

    public String getFormOption() {
        return formOption;
    }

    public void setFormOption(String formOption) {
        this.formOption = formOption;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getChooseSer() {
        return chooseSer;
    }

    public void setChooseSer(int chooseSer) {
        this.chooseSer = chooseSer;
    }

    public int getControl() {
        return control;
    }

    public void setControl(int control) {
        this.control = control;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
