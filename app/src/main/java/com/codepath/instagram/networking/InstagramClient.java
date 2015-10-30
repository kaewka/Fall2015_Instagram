package com.codepath.instagram.networking;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Created by thanawat on 10/28/15.
 */
public class InstagramClient {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void getPopularFeed(JsonHttpResponseHandler responseHandler){
        String url = "https://api.instagram.com/v1/media/popular?client_id=6d168ce25ec9473f925fa6f5158a3135";
        client.get(url, responseHandler);
    }

    public static void getComments(String mediaId, JsonHttpResponseHandler responseHandler){
        String url = "https://api.instagram.com/v1/media/" + mediaId + "/comments?client_id=6d168ce25ec9473f925fa6f5158a3135";
        client.get(url, responseHandler);
    }


}