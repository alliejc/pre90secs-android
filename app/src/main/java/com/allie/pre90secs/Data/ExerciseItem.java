package com.allie.pre90secs.Data;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExerciseItem {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("instructions")
    @Expose
    private List<String> instructions;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }
}
