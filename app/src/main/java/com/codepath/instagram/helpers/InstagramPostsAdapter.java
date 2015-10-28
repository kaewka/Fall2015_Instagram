package com.codepath.instagram.helpers;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by thanawat on 10/26/15.
 */
public class InstagramPostsAdapter extends RecyclerView.Adapter<InstagramPostsAdapter.PostViewHolder> {
    List<InstagramPost> posts;
    private Context context;

    public InstagramPostsAdapter(List<InstagramPost> posts){
        this.posts = posts;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_post, parent, false);

        PostViewHolder viewHolder = new PostViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int i) {
        InstagramPost post = posts.get(i);

        Uri profileImageUri = Uri.parse(post.user.profilePictureUrl);
        Uri postImageUri = Uri.parse(post.image.imageUrl);
        String formattedDate = (String) DateUtils.getRelativeTimeSpanString(post.createdTime * 1000);
        SpannableStringBuilder caption = getFormattedCaption(post.user.userName, post.caption);
        String likesCount = NumberFormat.getNumberInstance(Locale.US).format(post.likesCount) + " likes";

        holder.tvUsername.setText(post.user.userName);
        holder.sdvProfileImage.setImageURI(profileImageUri);
        holder.tvPostDate.setText(formattedDate);
        holder.sdvPhoto.setImageURI(postImageUri);
        holder.sdvPhoto.setAspectRatio(1.0f);
        holder.tvLikes.setText(likesCount);
        holder.tvCaption.setText(caption);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView sdvProfileImage;
        public SimpleDraweeView sdvPhoto;
        public TextView tvUsername;
        public TextView tvCaption;
        public TextView tvPostDate;
        public TextView tvLikes;

        public PostViewHolder (View layoutView){
            super(layoutView);
            sdvProfileImage = (SimpleDraweeView) layoutView.findViewById(R.id.sdvProfileImage);
            sdvPhoto = (SimpleDraweeView) layoutView.findViewById(R.id.sdvPhoto);
            tvUsername = (TextView) layoutView.findViewById(R.id.tvUsername);
            tvCaption  = (TextView) layoutView.findViewById(R.id.tvCaption);
            tvPostDate = (TextView) layoutView.findViewById(R.id.tvPostDate);
            tvLikes = (TextView) layoutView.findViewById(R.id.tvLikes);
        }
    }

    private SpannableStringBuilder getFormattedCaption(String userName, String caption) {
        ForegroundColorSpan blue = new ForegroundColorSpan(
                this.context.getResources().getColor(R.color.blue_text)
        );

        SpannableStringBuilder ssb = new SpannableStringBuilder(userName);

        ssb.setSpan(blue, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (caption != null) {
            ssb.append(" ");
            ForegroundColorSpan fgcsGray = new ForegroundColorSpan(
                    this.context.getResources().getColor(R.color.gray_text)
            );
            ssb.append(caption);
            ssb.setSpan(fgcsGray, ssb.length() - caption.length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return ssb;
    }
}
