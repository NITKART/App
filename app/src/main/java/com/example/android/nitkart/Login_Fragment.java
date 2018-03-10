package com.example.android.nitkart;

/**
 * Created by dhp on 26/2/18.
 */


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login_Fragment extends Fragment implements OnClickListener {
    private static View view;
    private static EditText emailid, password;
    private static Button loginButton;
    private static TextView forgotPassword, signUp;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;
    private static FragmentManager fragmentManager;
    String domain = MainActivity.domain;
    private ProgressDialog mProgress;
    String loginUrl = domain + "/user/login/";
    String registerUrl = domain + "/user/register/";
    String testUrl = domain + "/user";
    String googleEmailUrl = domain + "/user/email_id/";
    String myPreferences = "myPreferences";
    String emailId = "email_id";
    String passWord = "password";
    String isLogged = "isLogged";
    String phoneNumber = "phone_number";
    SharedPreferences sharedPreferences;


    //Google login related
    static GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;

    public Login_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = this.getActivity().getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        view = inflater.inflate(R.layout.activity_login, container, false);
        initViews();
        setListeners();
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        mProgress = new ProgressDialog(getActivity());
        mProgress.setTitle("Logging In...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        view.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        updateUI(account);

        try
        {
            if(sharedPreferences.getString(isLogged, null).equals("1"))
            {
                Intent intent = new Intent(getActivity(), BottomNavigation.class);
                startActivity(intent);
                getActivity().finish();
            }
        }
        catch(Exception e)
        {
            Log.v("Exception : ", e.toString());
        }

        return view;
    }


    // Initiate Views
    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();

        emailid = (EditText) view.findViewById(R.id.login_emailid);
        password = (EditText) view.findViewById(R.id.login_password);
        loginButton = (Button) view.findViewById(R.id.loginBtn);
        forgotPassword = (TextView) view.findViewById(R.id.forgot_password);
        signUp = (TextView) view.findViewById(R.id.createAccount);
        show_hide_password = (CheckBox) view
                .findViewById(R.id.show_hide_password);
        loginLayout = (LinearLayout) view.findViewById(R.id.login_layout);

        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            forgotPassword.setTextColor(csl);
            show_hide_password.setTextColor(csl);
            signUp.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    // Set Listeners
    private void setListeners() {
        loginButton.setOnClickListener(this);
        signUp.setOnClickListener(this);

        // Set check listener over checkbox for showing and hiding password
        show_hide_password
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                checkValidation();
                break;
            case R.id.createAccount:

                // Replace signup frgament with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer, new SignUp_Fragment(),
                                Utils.SignUp_Fragment).commit();
                break;
        }

    }

    // Check Validation before login
    private void checkValidation() {
        // Get email id and password
        final String getEmailId = emailid.getText().toString();
        final String getPassword = password.getText().toString();

        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(getEmailId);

        // Check for both field is empty or not
        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {
            loginLayout.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getActivity(), view,
                    "Enter both credentials.");

        }
//        // Check if email id is valid or not
//        else if (!m.find())
//            new CustomToast().Show_Toast(getActivity(), view,
//                    "Your Email Id is Invalid.");
//            // Else do login and do your stuff
        else {            //implement custom login here
            Toast.makeText(getActivity(), "Doing  Login.", Toast.LENGTH_SHORT)
                    .show();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, loginUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                            Log.d("Response", response);
                            if (response.charAt(2) == 'S') {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(emailId, getEmailId);
                                editor.putString(passWord, getPassword);
                                editor.putString(isLogged, "1");
                                editor.commit();
                                Intent intent = new Intent(getActivity(), BottomNavigation.class);
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                loginLayout.startAnimation(shakeAnimation);
                                new CustomToast().Show_Toast(getActivity(), view, "Invalid User");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), error.toString() + "lol", Toast.LENGTH_SHORT).show();
                            Log.d("Error.Response", error.toString());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", getEmailId);
                    params.put("password", getPassword);
                    return params;
                }
            };
            SingletonRequestQueue.getInstance(getActivity()).addToRequestQueue(stringRequest);


        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Coudldn't sign in", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getActivity(), "Error Signing In", Toast.LENGTH_SHORT).show();
            updateUI(null);
        }
    }

    private void updateUI(final GoogleSignInAccount account) {
        if (account != null) {
            Intent intent = new Intent(getActivity(), BottomNavigation.class);
            startActivity(intent);
            Log.d("llllllllllllll", "inside if");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, googleEmailUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                            Log.d("Response", response);
                            if (response.charAt(2) == 'S') {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", account.getDisplayName());
                                editor.putString("password", null);
                                editor.putString("email_id", account.getEmail());
                                editor.putString("phone_number", null);
                                editor.putString(isLogged, "1");
                                String url;
                                if (account.getPhotoUrl() != null) {
                                    url = account.getPhotoUrl().toString();
                                    editor.putString("photo_url", url);
                                }else{
                                    editor.putString("photo_url", "https://scontent-bom1-1.xx.fbcdn.net/v/t1.0-9/16196015_10154888128487744_6901111466535510271_n.png?oh=07b0d5bb946821893d0b6424746bc4da&oe=5B41BEE9");
                                }
                                editor.commit();
//                                Toast.makeText(getActivity(), "inside", Toast.LENGTH_SHORT)
//                                        .show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //this is causing an error
//                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("Error.Response", error.toString());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", account.getDisplayName());
                    params.put("password", "hsdhgsfjdfkjdskljf34@a#");
                    params.put("email_id", account.getEmail());
                    params.put("phone_number", "00000000");
                    return params;
                }
            };
            SingletonRequestQueue.getInstance(getActivity()).addToRequestQueue(stringRequest);
            getActivity().finish();

        }
    }

}
