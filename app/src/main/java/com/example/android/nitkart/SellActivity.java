package com.example.android.nitkart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SellActivity extends AppCompatActivity implements Imageutils.ImageAttachmentListener {
    ImageView iv_attachment;

    //For Image Attachment

    private Bitmap bitmap;
    private String file_name;
    Button submitBtn;
    String postAdUrl = MainActivity.domain + "/user/postAd/";
    Imageutils imageutils;
    public ActionBar toolbar;
    ProgressDialog progressBar;
    private static LinearLayout infoFill;
    private static Animation shakeAnimation;
    SharedPreferences sharedPreferences;
    String myPreferences = "myPreferences";
    String emailId = "email_id";


    public EditText sellerName, sellerEmail, sellerPhone, sellerBlock, sellerRoom, timePeriod, productPrice, productName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        toolbar = getSupportActionBar();
        toolbar.setTitle("Sell");
        infoFill = findViewById(R.id.infoFill);

        imageutils = new Imageutils(this);

        productName = findViewById(R.id.product_name);
        sellerName = findViewById(R.id.seller_name);
        sellerEmail = findViewById(R.id.seller_email);
        sellerPhone = findViewById(R.id.seller_phone);
        sellerBlock = findViewById(R.id.seller_block);
        sellerRoom = findViewById(R.id.seller_room);
        timePeriod = findViewById(R.id.time_period);
        productPrice = findViewById(R.id.price);
        sharedPreferences = getApplicationContext().getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        sellerEmail.setText(sharedPreferences.getString("email_id", null));
//        sellerEmail.setFocusable(false);
        sellerName.setText(sharedPreferences.getString("username", null));
        sellerPhone.setText(sharedPreferences.getString("phone_number", null));



        progressBar = new ProgressDialog(this);
        shakeAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.shake);


        iv_attachment = findViewById(R.id.imageViewAttach);
        submitBtn = findViewById(R.id.submitBtn);
        iv_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageutils.imagepicker(1);
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidationAndReq();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageutils.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        this.bitmap = file;
        this.file_name = filename;
        iv_attachment.setImageBitmap(file);

        String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        imageutils.createImage(file, filename, path, false);
    }

    public void onBackPressed() {
        Intent intent = new Intent(SellActivity.this, BottomNavigation.class);
        startActivity(intent);
        finish();
        toolbar = getSupportActionBar();
    }


    private void checkValidationAndReq() {

        // Get all edit text texts
        final String getSellerName, getSellerEmailId, getSellerPhone, getProductName, getProductPrice, getTimePeriod, getSellerBlock, getSellerRoom;
        getProductName = productName.getText().toString();
        getSellerName = sellerName.getText().toString();
        getSellerPhone = sellerPhone.getText().toString();
        getSellerEmailId = sellerEmail.getText().toString();
        getSellerBlock = (sellerBlock.getText().toString());
        getSellerRoom = (sellerRoom.getText().toString());
        getTimePeriod = (timePeriod.getText().toString());
        getProductPrice = productPrice.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getSellerEmailId);

        // Check if all strings are null or not
        if (getSellerName.equals("") || getSellerName.length() == 0
                || getSellerEmailId.equals("") || getSellerEmailId.length() == 0
                || getSellerPhone.equals("") || getSellerPhone.length() == 0
                || getSellerBlock.equals("") || getSellerBlock.length() == 0
                || getSellerRoom.equals("") || getSellerRoom.length() == 0
                || getProductPrice.equals("") || getProductPrice.length() == 0
                || getProductName.equals("") || getProductName.length() == 0
                || getTimePeriod.equals("") || getTimePeriod.length() == 0) {

            new CustomToast().Show_Toast(getApplicationContext(), new View(getApplicationContext()),
                    "All fields are required.");
            infoFill.startAnimation(shakeAnimation);
        }

        // Check if email id valid or not
        else if (!m.find()) {
            new CustomToast().Show_Toast(getApplicationContext(), new View(getApplicationContext()),
                    "Your Email Id is Invalid.");
            sellerEmail.startAnimation(shakeAnimation);
        } else {
            progressBar.setCancelable(false);//you can cancel it by pressing back button
            progressBar.setMessage("Posting Ad...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.show();

            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, postAdUrl, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    Toast.makeText(SellActivity.this, resultResponse, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), BottomNavigation.class);
                    startActivity(intent);
                    finish();
                    progressBar.cancel();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.cancel();
                    NetworkResponse networkResponse = error.networkResponse;
                    String errorMessage = "Unknown error";
                    if (networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorMessage = "Request timeout";
                        } else if (error.getClass().equals(NoConnectionError.class)) {
                            errorMessage = "Failed to connect server";
                        }
                    } else {
                        String result = new String(networkResponse.data);
                        try {
                            JSONObject response = new JSONObject(result);
                            String status = response.getString("status");
                            String message = response.getString("message");

                            Log.e("Error Status", status);
                            Log.e("Error Message", message);

                            if (networkResponse.statusCode == 404) {
                                errorMessage = "Resource not found";
                            } else if (networkResponse.statusCode == 401) {
                                errorMessage = message + " Please login again";
                            } else if (networkResponse.statusCode == 400) {
                                errorMessage = message + " Check your inputs";
                            } else if (networkResponse.statusCode == 500) {
                                errorMessage = message + " Something is getting wrong";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i("Error", errorMessage);
                    Toast.makeText(SellActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("product_name", getProductName);
                    params.put("seller_name", getSellerName);
                    params.put("seller_phone", getSellerPhone);
                    params.put("seller_email", getSellerEmailId);
                    params.put("seller_block", getSellerBlock);
                    params.put("seller_room", getSellerRoom);
                    params.put("time_period", getTimePeriod);
                    params.put("product_price", getProductPrice);
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("image", new DataPart(getProductName + ".jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), iv_attachment.getDrawable()), "image/jpeg"));
                    return params;
                }
            };
            SingletonRequestQueue.getInstance(SellActivity.this).addToRequestQueue(multipartRequest);
        }


    }
}
