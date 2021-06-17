package com.example.memes;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class MainActivity extends AppCompatActivity {

    String currenturl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load();
    }

  public void load(){
      ImageView img = (ImageView) findViewById(R.id.image);
      ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
      bar.setVisibility(View.VISIBLE);
      // Instantiate the RequestQueue.
      RequestQueue queue = Volley.newRequestQueue(this);
      String url ="https://meme-api.herokuapp.com/gimme";

// Request a string response from the provided URL.
      JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null,
              new Response.Listener<JSONObject>() {
                  @Override
                  public void onResponse(JSONObject response) {
                      // Display the first 500 characters of the response string.
                      try {
                          currenturl = response.getString("url");
                          Glide.with(MainActivity.this).load(currenturl).listener(new RequestListener<Drawable>() {
                              @Override
                              public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                  bar.setVisibility(View.GONE);
                                  return false;
                              }

                              @Override
                              public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                  bar.setVisibility(View.GONE);
                                  return false;
                              }
                          }).into(img);
                      } catch (JSONException e) {
                          e.printStackTrace();
                      }

                  }
              }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
          }
      });

      // Add the request to the RequestQueue.
      queue.add(stringRequest);
  }

    public void share(View view){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,currenturl);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share this Meme using");
        startActivity(shareIntent);
    }

    public void next(View view){
        load();
    }
}