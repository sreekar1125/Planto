package com.dev.planto.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.planto.R;
import com.dev.planto.model.VerifList;

import java.util.ArrayList;

public class VerifListAdapter extends RecyclerView.Adapter<VerifListAdapter.myViewHolder> {

    private ArrayList<VerifList> verifList;
    private Context context;

    public VerifListAdapter() {
    }

    public VerifListAdapter(ArrayList<VerifList> verifList, Context context) {
        this.verifList = verifList;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.verfication_list_item,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.date.setText(verifList.get(position).getDate());
        holder.status.setText(verifList.get(position).getStatus());

        if (TextUtils.equals(holder.status.getText(),"Failed") || TextUtils.equals(holder.status.getText(),"failed")){
            holder.status.setTextColor(ContextCompat.getColor(context,R.color.colorRed));
        }
    }


    @Override
    public int getItemCount() {
        return verifList.size();
    }


    class myViewHolder extends RecyclerView.ViewHolder{
        private TextView date , status;

         myViewHolder(View itemView) {
            super(itemView);

            date =  itemView.findViewById(R.id.txt_date);
            status =  itemView.findViewById(R.id.txt_verified_status);
        }
    }


}