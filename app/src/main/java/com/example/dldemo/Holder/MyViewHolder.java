package com.example.dldemo.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dldemo.R;
import com.example.dldemo.model.LangModel;

class MyViewHolder extends RecyclerView.ViewHolder {
    TextView langName;
    ImageView langImage;

    public MyViewHolder(View view) {
        super(view);
        langName = view.findViewById(R.id.langName);
        langImage = view.findViewById(R.id.langImage);

    }

    public void bind(LangModel langModel) {
        langName.setText(langModel.getName());
        Glide.with(itemView)
                .load(langModel.getImage())
                .into(langImage);
    }
}
