package com.example.bankingsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bankingsystem.Model.Profile;
import com.example.bankingsystem.Model.db.ApplicationDB;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {

    private Bundle bundle;
    private String username;
    private String password;

    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private Button btnFindID;
    private Button btnFindPassword;
    private CheckBox chkRememberCred;
    private Button btnCreateAccount;

    private Profile lastProfileUsed;
    private Gson gson;
    private String json;
    private SharedPreferences userPreferences;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (view.getId() == btnLogin.getId()) {
                validateAccount();
            } else if (view.getId() == btnCreateAccount.getId()) {
                createAccount();
            }
        }
    };

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = this.getArguments();
        if (bundle != null) {
            username = bundle.getString("UserId", "");
            password = bundle.getString("Password", "");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        edtUsername = rootView.findViewById(R.id.edt_userId);
        edtPassword = rootView.findViewById(R.id.edt_password);
        btnLogin = rootView.findViewById(R.id.btn_login);
        btnFindID = rootView.findViewById(R.id.btn_find_id);
        btnFindPassword = rootView.findViewById(R.id.btn_find_password);
        chkRememberCred = rootView.findViewById(R.id.chk_remember);
        btnCreateAccount = rootView.findViewById(R.id.btn_create_account);

        getActivity().setTitle(getResources().getString(R.string.app_name));
        ((LaunchActivity) getActivity()).removeUpButton();

        setupViews();

        if (bundle != null) {
            edtUsername.setText(username);
            edtPassword.setText(password);
            chkRememberCred.setChecked(true);
        }

        return rootView;
    }

    /**
     * method used to setup the values for the views and fields
     */
    private void setupViews() {

        btnLogin.setOnClickListener(clickListener);
//        btnFindID.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((FindActivity)getActivity()).replaceFragment()
//            }
//        });
        btnFindPassword.setOnClickListener(clickListener);
        btnCreateAccount.setOnClickListener(clickListener);

        userPreferences = getActivity().getSharedPreferences("LastProfileUsed", MODE_PRIVATE);

        chkRememberCred.setChecked(userPreferences.getBoolean("rememberMe", false));
        if (chkRememberCred.isChecked()) {

            gson = new Gson();
            json = userPreferences.getString("LastProfileUsed", "");
            lastProfileUsed = gson.fromJson(json, Profile.class);

            edtUsername.setText(lastProfileUsed.getUserId());
            edtPassword.setText(lastProfileUsed.getPassword());

            //Automatic login for user
            //login();
            //finish();
        }

    }

    @Override
    public void onStop() {
        if (lastProfileUsed != null) {
            if (edtUsername.getText().toString().equals(lastProfileUsed.getUserId()) && edtPassword.getText().toString().equals(lastProfileUsed.getPassword())) {
                userPreferences.edit().putBoolean("rememberMe", chkRememberCred.isChecked()).apply();
            } else {
                userPreferences.edit().putBoolean("rememberMe", false).apply();
            }
        }

        super.onStop();
    }

    private void validateAccount() {
        ApplicationDB applicationDB = new ApplicationDB(getActivity().getApplicationContext());
        ArrayList<Profile> profiles = applicationDB.getAllProfiles();

        boolean match = false;

        if (profiles.size() > 0) {
            for (int i = 0; i < profiles.size(); i++) {
                if (edtUsername.getText().toString().equals(profiles.get(i).getUserId()) && edtPassword.getText().toString().equals(profiles.get(i).getPassword())) {

                    match = true;

                    userPreferences.edit().putBoolean("rememberMe", chkRememberCred.isChecked()).apply();

                    lastProfileUsed = profiles.get(i);

                    SharedPreferences.Editor prefsEditor = userPreferences.edit();
                    gson = new Gson();
                    json = gson.toJson(lastProfileUsed);
                    prefsEditor.putString("LastProfileUsed", json).apply();

                    ((LaunchActivity) getActivity()).login();
                }
            }
            if (!match) {
                Toast.makeText(getActivity(), R.string.incorrect_login, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), R.string.incorrect_login, Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * method that creates an account
     */
    private void createAccount() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_frm_content, new CreateProfileFragment())
                .addToBackStack(null)
                .commit();
    }

}
