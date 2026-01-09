package org.farm2.tools.web.dto;

public class QuotingKnowDto {
    private String text;
    private String knowid;
    private String fileid;
    private String title;
    private String model;
    private String questionId;
    private String id;

    public QuotingKnowDto(String id, String knowid, String fileid, String questionId, String model, String title, String text) {
        this.text = text;
        this.id = id;
        this.knowid = knowid;
        this.fileid = fileid;
        this.title = title;
        this.model = model;
        this.questionId = questionId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getKnowid() {
        return knowid;
    }

    public void setKnowid(String knowid) {
        this.knowid = knowid;
    }

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
