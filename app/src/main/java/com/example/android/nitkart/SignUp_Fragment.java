package com.example.android.nitkart;

/**
 * Created by dhp on 26/2/18.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp_Fragment extends Fragment implements OnClickListener {
    private static View view;
    private static EditText fullName, userName, emailId, mobileNumber, location,
            password, confirmPassword;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    String domain = MainActivity.domain;
    String loginUrl = domain + "/user/login/";
    String registerUrl = domain + "/user/register/";
    String testUrl = domain + "/user";
    String googleEmailUrl = domain + "/user/email_id/";
    String myPreferences = "myPreferences";
    String passWord = "password";
    String phoneNumber = "phone_number";
    SharedPreferences sharedPreferences;


    public SignUp_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = this.getActivity().getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        view = inflater.inflate(R.layout.activity_sign_up, container, false);
        initViews();
        setListeners();
        return view;
    }

    // Initialize all views
    private void initViews() {
        fullName =  view.findViewById(R.id.fullName);
        userName =  view.findViewById(R.id.userName);
        emailId = view.findViewById(R.id.userEmailId);
        mobileNumber =  view.findViewById(R.id.mobileNumber);
        location =  view.findViewById(R.id.location);
        password =  view.findViewById(R.id.password);
        confirmPassword =  view.findViewById(R.id.confirmPassword);
        signUpButton =  view.findViewById(R.id.signUpBtn);
        login =  view.findViewById(R.id.already_user);
        terms_conditions =  view.findViewById(R.id.terms_conditions);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    // Set Listeners
    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:
                // Call checkValidation method
                checkValidation();
                break;

            case R.id.already_user:
                // Replace login fragment
                new MainActivity().replaceLoginFragment();
                break;
        }

    }

    // Check Validation Method
    private void checkValidation() {

        // Get all edit text texts
        String getFullName = fullName.getText().toString();
        final String getEmailId = emailId.getText().toString();
        final String getUserName = userName.getText().toString();
        final String getMobileNumber = mobileNumber.getText().toString();
        String getLocation = location.getText().toString();
        final String getPassword = password.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getLocation.equals("") || getLocation.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0
                || getUserName.length() == 0)

            new CustomToast().Show_Toast(getActivity(), view,
                    "All fields are required.");

            // Check if email id valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");

            // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword))
            new CustomToast().Show_Toast(getActivity(), view,
                    "Both password doesn't match.");

            // Make sure user should check Terms and Conditions checkbox
        else if (!terms_conditions.isChecked())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Please select Terms and Conditions.");


            // Else do signup or do your stuff
        else {
            Toast.makeText(getActivity(), "Do SignUp.", Toast.LENGTH_SHORT)
                    .show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, registerUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                            Log.d("Response", response);
                            if (response.charAt(2) == 'i' || response.charAt(2) == 'S') {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", getUserName);
                                editor.putString("password", getPassword);
                                editor.putString("email_id", getEmailId);
                                editor.putString("phone_number", getMobileNumber);
                                editor.commit();
                                Intent intent = new Intent(getActivity(), BottomNavigation.class);
                                startActivity(intent);
                                getActivity().finish();
                                Toast.makeText(getActivity(), "inside", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("Error.Response", error.toString());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", getUserName);
                    params.put("password", getPassword);
                    params.put("email_id", getEmailId);
                    params.put("phone_number", getMobileNumber);
                    return params;
                }
            };
            SingletonRequestQueue.getInstance(getActivity()).addToRequestQueue(stringRequest);

        }

    }
}
