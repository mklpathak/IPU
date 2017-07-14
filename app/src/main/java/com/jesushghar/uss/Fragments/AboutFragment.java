package com.jesushghar.uss.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jesushghar.uss.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;


public class AboutFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    CarouselView carouselView;
    int[] sampleImages = {R.drawable.image1, R.drawable.image2, R.drawable.image3,  R.drawable.image5, R.drawable.image7};

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        carouselView = (CarouselView) rootView.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
        carouselView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL = "http://www.ipu.ac.in/";
                Intent link_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                startActivity(link_intent);
            }
        });

        return  rootView;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
