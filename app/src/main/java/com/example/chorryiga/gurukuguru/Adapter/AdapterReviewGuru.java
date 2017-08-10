package com.example.chorryiga.gurukuguru.Adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chorryiga.gurukuguru.GlobalUse.Server;
import com.example.chorryiga.gurukuguru.Model.ModelRating;
import com.example.chorryiga.gurukuguru.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Choy on 7/9/2017.
 */

public class AdapterReviewGuru extends RecyclerView.Adapter<AdapterReviewGuru.ViewHolder> {
    private ArrayList<ModelRating> reviews;
    private Context context;

    public AdapterReviewGuru(Context context, ArrayList<ModelRating> reviews){
        this.context = context;
        this.reviews = reviews;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView review_guru;
        public TextView name_review;
        public CircleImageView foto_ortu;

        public ViewHolder(View itemView){
            super(itemView);
            review_guru = (TextView) itemView.findViewById(R.id.reviewortu);
            name_review = (TextView) itemView.findViewById(R.id.name_review);
            foto_ortu = (CircleImageView) itemView.findViewById(R.id.foto_ortu);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int i){
        View view = LayoutInflater.from(context).inflate(R.layout.list_rewiewguru, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterReviewGuru.ViewHolder holder, int position){
        holder.review_guru.setText(reviews.get(position).getReview());
        holder.name_review.setText(reviews.get(position).getNama());
        Picasso.with(context).invalidate(Server.URLpath+"upload/"+reviews.get(position).getFoto());
        Picasso.with(context).load(Server.URLpath+"upload/"+reviews.get(position).getFoto()).into(holder.foto_ortu);
    }

    public int getItemCount(){
        return reviews.size();
    }
}
