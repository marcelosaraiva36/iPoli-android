package com.curiousily.ipoli.app;

import com.curiousily.ipoli.quest.Quest;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;

/**
 * Created by Venelin Valkov <venelin@curiousily.com>
 * on 8/13/15.
 */
public interface APIClient {
    @POST("/quests")
    void createQuest(@Body Quest quest, Callback<Quest> cb);

    @GET("/quests")
    void getDailyQuests(Callback<List<Quest>> cb);


    @PUT("/quests")
    void rateQuest(@Body Quest quest, Callback<Quest> cb);
}