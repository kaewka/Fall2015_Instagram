package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.instagram.R;
import com.codepath.instagram.helpers.InstagramPostsAdapter;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.networking.InstagramClient;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "IGClient";
    private List<InstagramPost> posts;
    private InstagramPostsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //posts = fetchPopularPosts();

        //Get recyclerView reference
        RecyclerView rvPhotos = (RecyclerView) findViewById(R.id.rvPhotos);

        posts = new ArrayList<InstagramPost>();

        //Create Adapter
        adapter = new InstagramPostsAdapter(posts);

        //Set adapter
        rvPhotos.setAdapter(adapter);

        //Set layout
        rvPhotos.setLayoutManager(new LinearLayoutManager(this));

        InstagramClient.getPopularFeed(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject data = null;
                    if (response != null) {
                        Log.d("TAG", response.toString());
                        posts = Utils.decodePostsFromJsonResponse(response);
                    }
                } catch (Exception e) {
                    // Invalid JSON format, show appropriate error.
                    e.printStackTrace();
                }
                adapter.updateList(posts);
            }
        });

    }

    private List<InstagramPost> fetchPopularPosts() {
        JSONObject postsJson = null;
        try {
            postsJson = Utils.loadJsonFromAsset(getApplicationContext(), "popular.json");
            //Log.d(TAG, postsJson.toString());

        } catch (Exception e){

        }
        return Utils.decodePostsFromJsonResponse(postsJson);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
