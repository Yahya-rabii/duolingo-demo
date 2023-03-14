package com.example.dldemo.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dldemo.R;
import com.example.dldemo.model.LangModel;

import java.util.List;



public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private List<LangModel> langModelList;
    private final FinaListClickListener clickListener;

    public ListAdapter(List<LangModel> langModelList, FinaListClickListener clickListener) {
        this.langModelList = langModelList;
        this.clickListener = clickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<LangModel> langModelList) {
        this.langModelList = langModelList;
        notifyDataSetChanged();
    }
    public void setData(List<LangModel> data) {
        this.langModelList = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        return  new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.langName.setText(langModelList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(langModelList.get(position));
            }
        });
        Glide.with(holder.langImage)
                .load(langModelList.get(position).getImage())
                .into(holder.langImage);
    }

    @Override
    public int getItemCount() {
        return langModelList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView langName;
        ImageView langImage;

        public MyViewHolder(View view) {
            super(view);
            langName = view.findViewById(R.id.langName);
            langImage = view.findViewById(R.id.langImage);

        }
    }

    public interface FinaListClickListener {
        public void onItemClick(LangModel langModel);
    }
}
