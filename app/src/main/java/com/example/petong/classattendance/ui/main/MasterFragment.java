package com.example.petong.classattendance.ui.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.petong.classattendance.R;
import com.example.petong.classattendance.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class MasterFragment extends Fragment {
    private static final String USER_KEY = "user_key";

    public static MasterFragment newInstance(User userData) {
        MasterFragment fragment = new MasterFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(USER_KEY, userData);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_master, container, false);

        User mUserData = getArguments().getParcelable(USER_KEY);
        if (mUserData != null){
            Log.d("CallApi","FragmentMaster: "+ mUserData.getStatus());
        }
        TextView mTextStuCode = view.findViewById(R.id.fieldStuCode);
        mTextStuCode.setText(mUserData.getData().getStudentCode());
        TextView mTextPreName = view.findViewById(R.id.fieldPreName);
        mTextPreName.setText(mUserData.getData().getPreName());
        TextView mTextfULLName = view.findViewById(R.id.fieldFullName);
        mTextfULLName.setText(mUserData.getData().getFullName());

        return view;
    }

}
