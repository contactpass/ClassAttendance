package com.example.petong.classattendance.ui.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.petong.classattendance.R;
import com.example.petong.classattendance.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    private static final String USER_KEY = "user_key";

    public static DetailFragment newInstance(User userData) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(USER_KEY, userData);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        User mUserData = getArguments().getParcelable(USER_KEY);
        if (mUserData != null){
            Log.d("CallApi","FragmentDetail: "+ mUserData.getStatus());
        }

        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

}
