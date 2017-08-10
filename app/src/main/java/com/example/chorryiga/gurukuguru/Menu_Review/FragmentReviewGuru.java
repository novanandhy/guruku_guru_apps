package com.example.chorryiga.gurukuguru.Menu_Review;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.chorryiga.gurukuguru.Adapter.AdapterReviewGuru;
import com.example.chorryiga.gurukuguru.R;
import com.example.chorryiga.gurukuguru.SplashActivity;

public class FragmentReviewGuru extends Fragment {
    private RatingBar ratingBar;
    private TextView ratingValue;

    public static FragmentReviewGuru newInstance(){
        FragmentReviewGuru fragment = new FragmentReviewGuru();
        return fragment;
    }

    public static FragmentReviewGuru newInstance(String strArg){
        FragmentReviewGuru fragment = new FragmentReviewGuru();
        Bundle args = new Bundle();
        args.putString("strArg1", strArg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_review_guru,null,false);

        ratingBar = (RatingBar) view.findViewById(R.id.rating_guru);
        ratingValue = (TextView) view.findViewById(R.id.nilai_rating_guru);

        ratingBar.setRating((float) SplashActivity.mRating.get(0).getRating());
        ratingValue.setText(""+SplashActivity.mRating.get(0).getRating());
        tampilReview(view);
        return view;
    }

    private void tampilReview(View view){
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.rv_reviewortu);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm  = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        AdapterReviewGuru adapter = new AdapterReviewGuru(getActivity(), SplashActivity.mRatingReview);
        recyclerView.setAdapter(adapter);
    }
}
