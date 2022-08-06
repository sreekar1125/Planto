package com.dev.planto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.planto.R;
import com.dev.planto.model.Transaction;

import java.util.ArrayList;

public class PassbookAdapter extends RecyclerView.Adapter<PassbookAdapter.myViewHolder> {

    private ArrayList<Transaction> transactions;
    private Context context;

    public PassbookAdapter(ArrayList<Transaction> transactions, Context context) {
        this.transactions = transactions;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(context).inflate(R.layout.layout_passbook,parent,false);

        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        holder.date.setText(transactions.get(position).getDate());
        holder.money.setText(transactions.get(position).getMoney());

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        private TextView date,money;

        myViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.txt_date);
            money = itemView.findViewById(R.id.trans_money);
        }
    }
}
