package com.dev.planto.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.planto.R;
import com.dev.planto.admin.RequestedUser;
import com.dev.planto.model.Requestuser;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.myViewHolder> {
    private ArrayList<Requestuser> r;
    private ArrayList<String> uId;
    private Context context;


    public RequestAdapter(ArrayList<String> uId, ArrayList<Requestuser> r, Context context){
        this.uId = uId;
        this.r=r;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(context).inflate(R.layout.layout_request,parent,false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int Position) {
      holder.name.setText(r.get(Position).getName());

      holder.layout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              view.getContext().startActivity(new Intent(view.getContext(), RequestedUser.class).putExtra("userName",r.get(Position).getName()).putExtra("uId",uId.get(Position)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
          }
      });
    }

    @Override
    public int getItemCount() {
        return r.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        private CardView layout;
        private TextView name;
        myViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.req_username);
            layout = itemView.findViewById(R.id.req_user);
        }
    }
}
