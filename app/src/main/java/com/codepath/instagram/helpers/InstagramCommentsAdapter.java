package com.codepath.instagram.helpers;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramComment;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class InstagramCommentsAdapter extends RecyclerView.Adapter<InstagramCommentsAdapter.InstagramCommentsViewHolder>{
    private ArrayList<InstagramComment> comments;
    Context context;

    public InstagramCommentsAdapter(ArrayList<InstagramComment> comments) {
        this.comments = comments;
    }

    @Override
    public InstagramCommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_comment, parent, false);

        InstagramCommentsViewHolder viewHolder = new InstagramCommentsViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(InstagramCommentsViewHolder holder, int position) {
        InstagramComment comment = comments.get(position);

        ForegroundColorSpan blueForegroundColorSpan = new ForegroundColorSpan(
                context.getResources().getColor(R.color.blue_text));
        TypefaceSpan serifMediumTypeFaceSpan = new TypefaceSpan("sans-serif-medium");
        SpannableStringBuilder ssb = new SpannableStringBuilder(comment.user.userName);
        ssb.setSpan(blueForegroundColorSpan, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(serifMediumTypeFaceSpan, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(" ");

        ssb.append(comment.text);
        ForegroundColorSpan grayForeGroundColorSpan = new ForegroundColorSpan(
                context.getResources().getColor(R.color.gray_text));
        TypefaceSpan serifTypeFaceSpan = new TypefaceSpan("sans-serif");
        ssb.setSpan(grayForeGroundColorSpan, ssb.length() - comment.text.length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(serifTypeFaceSpan, ssb.length() - comment.text.length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.tvUserName.setText(ssb, TextView.BufferType.NORMAL);
        holder.ivAvatar.setImageURI(Uri.parse(comment.user.profilePictureUrl));
        holder.tvCreatedTime.setText(DateUtils.getRelativeTimeSpanString(comment.createdTime * 1000));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class InstagramCommentsViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUserName;
        public SimpleDraweeView ivAvatar;
        public TextView tvCreatedTime;

        public InstagramCommentsViewHolder(View itemView) {
            super(itemView);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            ivAvatar = (SimpleDraweeView) itemView.findViewById(R.id.ivAvatar);
            tvCreatedTime = (TextView) itemView.findViewById(R.id.tvCreatedTime);
        }
    }
}