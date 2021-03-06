package com.allie.pre90secs.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExerciseItem implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("body_region")
    @Expose
    private List<String> bodyRegion = null;
    @SerializedName("difficulty")
    @Expose
    private List<String> difficulty = null;
    @SerializedName("instructions")
    @Expose
    private List<String> instructions = null;
    @SerializedName("limited_space")
    @Expose
    private Boolean limitedSpace;

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

    public List<String> getBodyRegion() {
        return bodyRegion;
    }

    public void setBodyRegion(List<String> bodyRegion) {
        this.bodyRegion = bodyRegion;
    }

    public List<String> getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(List<String> difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public Boolean getSpace() {
        return limitedSpace;
    }

    public void setSpace(Boolean limitedSpace) {
        this.limitedSpace = limitedSpace;
    }
}
