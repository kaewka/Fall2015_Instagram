package com.codepath.instagram.helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.CommentsActivity;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

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
    public void onBindViewHolder(final PostViewHolder holder, int position) {
        final InstagramPost post = posts.get(position);

        Uri profileImageUri = Uri.parse(post.user.profilePictureUrl);
        final Uri postImageUri = Uri.parse(post.image.imageUrl);
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

        holder.tvCommentsCount.setText("View all " + post.commentsCount + " comments");
        holder.tvCommentsCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, CommentsActivity.class);
                i.putExtra("mediaId", post.mediaId);
                context.startActivity(i);

            }
        });

        holder.ibShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBitmapAndShare(postImageUri, context);
            }
        });

        holder.llComments.removeAllViews();
        ForegroundColorSpan blueForegroundColorSpan = new ForegroundColorSpan(
                context.getResources().getColor(R.color.blue_text));
        TypefaceSpan serifMediumTypeFaceSpan = new TypefaceSpan("sans-serif-medium");
        SpannableStringBuilder ssb;
        if (post.commentsCount == 1) {
            ssb = new SpannableStringBuilder(post.comments.get(0).user.userName);
            ssb.setSpan(blueForegroundColorSpan, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(serifMediumTypeFaceSpan, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(" ");
            ssb.append(post.comments.get(0).text);
            View view = LayoutInflater.from(context).inflate(R.layout.item_text_comment, holder.llComments, false);
            TextView tvComment = (TextView) view.findViewById(R.id.tvComment);
            tvComment.setText(ssb, TextView.BufferType.NORMAL);
            holder.llComments.addView(view);
        } else if (post.commentsCount >= 2) {
            for (int i = post.comments.size() - 1; i >= post.comments.size() - 2; i--) {
                ssb = new SpannableStringBuilder(post.comments.get(0).user.userName);
                ssb.setSpan(blueForegroundColorSpan, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(serifMediumTypeFaceSpan, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.append(" ");
                ssb.append(post.comments.get(i).text);
                View view = LayoutInflater.from(context).inflate(R.layout.item_text_comment, holder.llComments, false);
                TextView tvComment = (TextView) view.findViewById(R.id.tvComment);
                tvComment.setText(ssb, TextView.BufferType.NORMAL);
                holder.llComments.addView(view);
            }
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // This method is used to update data for adapter and notify adapter that data has changed
    public void updateList(List<InstagramPost> data) {
        posts = data;
        notifyDataSetChanged();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView sdvProfileImage;
        public SimpleDraweeView sdvPhoto;
        public TextView tvUsername;
        public TextView tvCaption;
        public TextView tvPostDate;
        public TextView tvLikes;
        public TextView tvCommentsCount;
        public LinearLayout llComments;
        public ImageButton ibShare;

        public PostViewHolder (View layoutView){
            super(layoutView);
            sdvProfileImage = (SimpleDraweeView) layoutView.findViewById(R.id.sdvProfileImage);
            sdvPhoto = (SimpleDraweeView) layoutView.findViewById(R.id.sdvPhoto);
            tvUsername = (TextView) layoutView.findViewById(R.id.tvUsername);
            tvCaption  = (TextView) layoutView.findViewById(R.id.tvCaption);
            tvPostDate = (TextView) layoutView.findViewById(R.id.tvPostDate);
            tvLikes = (TextView) layoutView.findViewById(R.id.tvLikes);
            tvCommentsCount = (TextView) itemView.findViewById(R.id.tvCommentsCount);
            llComments = (LinearLayout) itemView.findViewById(R.id.llComments);
            ibShare = (ImageButton) itemView.findViewById(R.id.ibShare);
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


    public static void getBitmapAndShare(Uri imageUri, final Context context) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();


        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(imageUri)
                .setRequestPriority(Priority.HIGH)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .build();

        DataSource<CloseableReference<CloseableImage>> dataSource =
                imagePipeline.fetchDecodedImage(imageRequest, context);

        try {
            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                @Override
                public void onNewResultImpl(@Nullable Bitmap bitmap) {
                    if (bitmap == null) {
                        return;
                    }
                    shareBitmap(bitmap, context);
                }

                @Override
                public void onFailureImpl(DataSource dataSource) {

                }
            }, CallerThreadExecutor.getInstance());
        } finally {
            if (dataSource != null) {
                dataSource.close();
            }
        }
    }

    private static void shareBitmap(Bitmap bitmap, Context context) {
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                bitmap, "Image Description", null);

        Uri bmpUri = Uri.parse(path);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");

        context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }
}
