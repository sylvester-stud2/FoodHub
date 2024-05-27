package com.example.foodhub;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Rate {

    private Context context;
    private View rootView; // Root view containing the TextViews

    public Rate() {
        this.context = context;
        this.rootView = rootView;
    }

    public void submitRating(int userId, int postId, String ratingType) {
        String url = "http://your_server/ratings.php";
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response
                        Toast.makeText(context, "Rating submitted", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(context, "Error submitting rating", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userId));
                params.put("post_id", String.valueOf(postId));
                params.put("rating_type", ratingType); // 'thumbs_up' or 'thumbs_down'
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void fetchRatings(int postId) {
        String url = "http://your_server/ratings.php?post_id=" + postId;
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int thumbsUpCount = response.getInt("thumbs_up_count");
                            int thumbsDownCount = response.getInt("thumbs_down_count");

                            // Assuming you have TextViews with ids 'thumbs_up_view' and 'thumbs_down_view'
                            TextView thumbsUpView = rootView.findViewById(R.id.thumbs_up_view);
                            TextView thumbsDownView = rootView.findViewById(R.id.thumbs_down_view);

                            thumbsUpView.setText("Thumbs Up: " + thumbsUpCount);
                            thumbsDownView.setText("Thumbs Down: " + thumbsDownCount);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        queue.add(jsonObjectRequest);
    }
}
