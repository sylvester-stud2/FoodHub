//package com.example.foodhub;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import androidx.recyclerview.widget.RecyclerView;
//import java.util.List;
//
//public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
//
//    private List<Post> postList;
//
//    public PostAdapter(List<Post> postList) {
//        this.postList = postList;
//    }
//
//    @Override
//    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
//        return new PostViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(PostViewHolder holder, int position) {
//        Post post = postList.get(position);
//        holder.title.setText(post.getTitle());
//        holder.instructions.setText(post.getInstructions());
//        holder.ingredients.setText(post.getIngredients());
//        holder.imageView.setImageBitmap(post.getDecodedImage());
//    }
//
//    @Override
//    public int getItemCount() {
//        return postList.size();
//    }
//
//    public class PostViewHolder extends RecyclerView.ViewHolder {
//        public TextView title;
//        public TextView instructions;
//        public TextView ingredients;
//        public ImageView imageView;
//
//        public PostViewHolder(View itemView) {
//            super(itemView);
//            title = itemView.findViewById(R.id.recipe_title);
//            instructions = itemView.findViewById(R.id.instructions);
//            ingredients = itemView.findViewById(R.id.ingredients);
//            imageView = itemView.findViewById(R.id.postpic);
//        }
//    }
//}
