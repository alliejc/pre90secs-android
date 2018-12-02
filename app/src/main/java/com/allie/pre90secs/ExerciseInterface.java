package com.allie.pre90secs;

import com.allie.pre90secs.model.ExerciseItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ExerciseInterface {

    @GET("ExerciseObject.json")
    Call<List<ExerciseItem>> getExerciseItemList();

    @GET("img/{img_file}")
    Call<String> getExerciseImage(@Path("img_file") String img_file);

}
