package com.jesushghar.uss.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jesushghar.uss.R;
import com.jesushghar.uss.activities.HomeActivity;
import com.jesushghar.uss.activities.LoginActivity;
import com.jesushghar.uss.helper.SQLiteHandler;
import com.jesushghar.uss.helper.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private SQLiteHandler db;
    private SessionManager session;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        db = new SQLiteHandler(getActivity());

        session = new SessionManager(getActivity());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        String email = user.get("email");
        String role = user.get("role");
        String school = user.get("school");

        TextView  nameTextView = (TextView) rootView.findViewById(R.id.textView_name);
        TextView  emailTextView = (TextView) rootView.findViewById(R.id.textView_email);
        TextView  roleTextView = (TextView) rootView.findViewById(R.id.textView_role);
        TextView  schoolTextView = (TextView) rootView.findViewById(R.id.textView_school);

        nameTextView.setText(name);
        emailTextView.setText(email);
        roleTextView.setText(role);
        schoolTextView.setText(school);


        ImageView profileImage = (ImageView) rootView.findViewById(R.id.profile_image);
        //Picasso.with(getActivity())
          //      .load(R.drawable.image1)
            //    .resize(Resources.getSystem().getDisplayMetrics().widthPixels,500)
              //  .centerCrop()
                //.into(profileImage);

        final Button logout = (Button) rootView.findViewById(R.id.button_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
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

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}
