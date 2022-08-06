package com.dev.planto.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.planto.model.Model;
import com.dev.planto.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class AwarenessAdapter extends RecyclerView.Adapter<AwarenessAdapter.myViewHolder> {

    private Context context;
    private ArrayList<Model> imgs;

    public AwarenessAdapter(Context context, ArrayList<Model> imgs) {
        this.context = context;
        this.imgs = imgs;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {
        Picasso.get().load(imgs.get(position).getImage()).into(holder.img);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onClick(View view) {
                Dialog popUpImg = new Dialog(context);
                Objects.requireNonNull(popUpImg.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
                popUpImg.setContentView(popUpImg.getLayoutInflater().inflate(R.layout.layout_popup_img,null));
                  ImageView imageView = popUpImg.findViewById(R.id.popup_img);
                  imageView.setImageDrawable(holder.img.getDrawable());
                  imageView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                  imageView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                popUpImg.show();
            }
        });
        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/*");
                intent.putExtra(Intent.EXTRA_TEXT,imgs.get(position).getImage());
                context.startActivity(Intent.createChooser(intent,"Share via"));
            }
        });

    }

    @Override
    public int getItemCount() {
        return imgs.size();
    }


    class myViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        ImageButton shareBtn;


        myViewHolder(@NonNull View itemView) {
            super(itemView);
           img = itemView.findViewById(R.id.rImageView);
           shareBtn = itemView.findViewById(R.id.sharebtn);

        }
    }
}