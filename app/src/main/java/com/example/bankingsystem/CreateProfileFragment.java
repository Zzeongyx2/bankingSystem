package com.example.bankingsystem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bankingsystem.Model.Profile;
import com.example.bankingsystem.Model.db.ApplicationDB;

import java.util.ArrayList;

public class CreateProfileFragment extends Fragment {

    private EditText edtName;
    private EditText edtPhoneNum;
    private EditText edtUserId;
    private EditText edtPassword;
    private EditText edtPasswordConfirm;

    public CreateProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Create Profile");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_profile, container, false);

        edtName = rootView.findViewById(R.id.edt_name);
        edtPhoneNum = rootView.findViewById(R.id.edt_phoneNum);
        edtUserId = rootView.findViewById(R.id.edt_userId);
        edtPassword = rootView.findViewById(R.id.edt_password);
        edtPasswordConfirm = rootView.findViewById(R.id.edt_password_confirm);
        Button btnCreateAccount = rootView.findViewById(R.id.btn_create_account);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProfile();
            }
        });

        ((LaunchActivity) getActivity()).showUpButton();

        return rootView;
    }

    /**
     * method used to create an account
     */
    private void createProfile() {

        ApplicationDB applicationDb = new ApplicationDB( getActivity().getApplicationContext());
        ArrayList<Profile> profiles = applicationDb.getAllProfiles();
        boolean usernameTaken = false;

        for (int iProfile = 0; iProfile < profiles.size(); iProfile++) {
            if (edtUserId.getText().toString().equals(profiles.get(iProfile).getUserId())) {
                usernameTaken = true;
            }
        }

        if (edtName.getText().toString().equals("") || edtPhoneNum.getText().toString().equals("") || edtUserId.getText().toString().equals("") || edtPassword.getText().toString().equals("") || edtPasswordConfirm.getText().toString().equals("")) {
            Toast.makeText(getActivity(), R.string.fields_blank, Toast.LENGTH_SHORT).show();
        }

        else if (!(edtPassword.getText().toString().equals(edtPasswordConfirm.getText().toString()))) {
            Toast.makeText(getActivity(), R.string.password_mismatch, Toast.LENGTH_SHORT).show();
        }
        else if (usernameTaken) {
            Toast.makeText(getActivity(), "A User has already taken that username", Toast.LENGTH_SHORT).show();
        }
        else {
            Profile userProfile = new Profile(edtName.getText().toString(), edtPhoneNum.getText().toString(), edtUserId.getText().toString(), edtPassword.getText().toString());

            applicationDb.saveNewProfile(userProfile);

            Bundle bundle = new Bundle();
            bundle.putString("UserId", userProfile.getUserId());
            bundle.putString("Password", userProfile.getPassword());

            ((LaunchActivity) getActivity()).profileCreated(bundle);

        }
    }
}
