package com.example.foodhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Comments {
    private Context context;
    private RequestQueue queue;
    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private ArrayList<CommentItem> commentList;

    public Comments(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.queue = Volley.newRequestQueue(context);
        this.commentList = new ArrayList<>();
        this.adapter = new CommentAdapter(commentList);
        this.recyclerView.setAdapter(adapter);
    }

    public void submitComment(int userId, int postId, String commentText) {
        String url = "http://your_server/Comment.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response
                        Toast.makeText(context, "Comment submitted", Toast.LENGTH_SHORT).show();
                        fetchComments(postId); // Refresh comments after submitting
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(context, "Error submitting comment", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userId));
                params.put("post_id", String.valueOf(postId));
                params.put("comment_text", commentText);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void fetchComments(int postId) {
        String url = "http://your_server/Comment.php?post_id=" + postId;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            commentList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject commentObject = response.getJSONObject(i);
                                int userId = commentObject.getInt("user_id");
                                String commentText = commentObject.getString("comment_text");
                                String username = commentObject.getString("username");

                                CommentItem comment = new CommentItem(userId, username, commentText);
                                commentList.add(comment);
                            }
                            adapter.notifyDataSetChanged();
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

        queue.add(jsonArrayRequest);
    }

    public static class CommentItem {
        private int userId;
        private String username;
        private String commentText;

        public CommentItem(int userId, String username, String commentText) {
            this.userId = userId;
            this.username = username;
            this.commentText = commentText;
        }

        public int getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public String getCommentText() {
            return commentText;
        }
    }

    public static class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
        private ArrayList<CommentItem> commentList;

        public CommentAdapter(ArrayList<CommentItem> commentList) {
            this.commentList = commentList;
        }

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            CommentItem currentItem = commentList.get(position);
            holder.usernameTextView.setText(currentItem.getUsername());
            holder.commentTextView.setText(currentItem.getCommentText());
        }

        @Override
        public int getItemCount() {
            return commentList.size();
        }

        public static class CommentViewHolder extends RecyclerView.ViewHolder {
            public TextView usernameTextView;
            public TextView commentTextView;

            public CommentViewHolder(@NonNull View itemView) {
                super(itemView);
                usernameTextView = itemView.findViewById(R.id.username_text_view);
                commentTextView = itemView.findViewById(R.id.comment_text_view);
            }
        }
    }
}
