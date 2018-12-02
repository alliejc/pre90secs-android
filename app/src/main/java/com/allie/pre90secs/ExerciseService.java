package com.allie.pre90secs;

import com.allie.pre90secs.model.ExerciseItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExerciseService {

    private static ExerciseService exerciseService;
    private final ExerciseInterface exerciseInterface;

    public ExerciseService(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        exerciseInterface = retrofit.create(ExerciseInterface.class);
    }

    public Call<List<ExerciseItem>> getExerciseItemList(){
        return exerciseInterface.getExerciseItemList();
    }

    public Call<String> getExerciseImage(String id) {
        return exerciseInterface.getExerciseImage(id);
    }
}
