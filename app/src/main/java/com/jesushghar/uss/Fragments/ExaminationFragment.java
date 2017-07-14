package com.jesushghar.uss.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jesushghar.uss.R;

public class ExaminationFragment extends Fragment {

    String resultsURL;


    public ExaminationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        TextView notices, datesheet, results, regulationForms;
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_examination, container, false);

        notices = (TextView)rootView.findViewById(R.id.exam_notices);
        datesheet = (TextView)rootView.findViewById(R.id.exam_datesheet);
        results = (TextView)rootView.findViewById(R.id.exam_results);
        regulationForms = (TextView)rootView.findViewById(R.id.exam_regulation_forms);

        notices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultsURL = "http://ipu.ac.in/exam_notices.php";
                Intent link_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(resultsURL));
                startActivity(link_intent);
            }
        });

        datesheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultsURL = "http://ipu.ac.in/exam_datesheet.php";
                Intent link_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(resultsURL));
                startActivity(link_intent);
            }
        });

        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultsURL = "http://ipu.ac.in/exam_results.php";
                Intent link_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(resultsURL));
                startActivity(link_intent);
            }
        });

        regulationForms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultsURL = "http://ipu.ac.in/exam_regulation_form.php";
                Intent link_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(resultsURL));
                startActivity(link_intent);
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
