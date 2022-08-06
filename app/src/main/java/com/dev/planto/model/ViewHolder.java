package com.dev.planto.model;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.dev.planto.R;
import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {
    private View mView;

    public ViewHolder(View itemView) {
        super(itemView);
        mView=itemView;
    }
    public void setDetails(Context ctx, String image){
        ImageView mImageIv=mView.findViewById(R.id.rImageView);
        Picasso.get().load(image).into(mImageIv);
    }
}
