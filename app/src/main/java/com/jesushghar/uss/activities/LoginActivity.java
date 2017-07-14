package com.jesushghar.uss.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.jesushghar.uss.R;
import com.jesushghar.uss.helper.SQLiteHandler;
import com.jesushghar.uss.helper.SessionManager;
import com.jesushghar.uss.utils.AppConfig;
import com.jesushghar.uss.utils.AppController;
import com.jesushghar.uss.utils.BaseLoginActivity;
import com.jesushghar.uss.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = LoginActivity.class.getSimpleName();



    private SignInButton googleSignIn;

    private EditText inputEmail;
    private EditText inputPassword;
    private Button btnLogin;

    private ProgressDialog progressDialog;
    private SessionManager session;
    private SQLiteHandler db;


    private static final int RC_SIGN_IN = 0;

    private GoogleApiClient googleApiClient;

    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        btnLogin = (Button) findViewById(R.id.btn_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        db = new SQLiteHandler(getApplicationContext());

        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        //Validate Email.
        inputEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!Utility.emailValidator(inputEmail.getText().toString())) {
                        //Toast.makeText(getApplicationContext(),"Enter valid email!", Toast.LENGTH_SHORT).show();
                        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                        alertDialog.setTitle("Email");
                        alertDialog.setMessage("Email not valid!");
                        alertDialog.setButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        inputEmail.requestFocus();
                                    }
                                });
                        alertDialog.show();
                    }
                }
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()) {
                    checkLogin(email, password);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
                }
            }
        });


        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        googleSignIn = (SignInButton) findViewById(R.id.google_sign_in);
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGplus();
            }
        });


        TextView signup = (TextView) findViewById(R.id.link_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }



    private void checkLogin(final String email, final String password) {
        String tag_string_req = "req_login";

        progressDialog.setMessage("Logging in...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Login Response: " + response.toString());
                        progressDialog.hide();
                        try{
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");

                            if (!error) {
                                session.setLogin(true);
                                String uid = jObj.getString("uid");
                                JSONObject user = jObj.getJSONObject("user");
                                String name = user.getString("name");
                                String email = user.getString("email");
                                String created_at = user.getString("created_at");
                                String role = user.getString("role");
                                String school = user.getString("school");

                                db.addUser(name, email, role, school, uid, created_at);

                                Intent intent = new Intent(LoginActivity.this,
                                        HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                String errorMsg = jObj.getString("error_msg");
                                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Login Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                }) {
                @Override
                 protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog(){
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hideDialog(){
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;
            if (!googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        }
    }
    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        Toast.makeText(LoginActivity.this, "User is Connected! ", Toast.LENGTH_LONG).show();

        getProfileInformation();

        //updateUI(flase);
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();

        //updateUI(flase);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
            return;
        }

        if (!mIntentInProgress) {
            mConnectionResult = connectionResult;
            if (mSignInClicked) {
                //resolveSignInError();
            }
        }

    }


    private void signInWithGplus() {
        if (!googleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                googleApiClient.connect();
            }
        }
    }

    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(googleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(googleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personEmail = Plus.AccountApi.getAccountName(googleApiClient);

                Log.e(TAG, "getProfileInformation: Name: " + personName +", Email: " + personEmail);
                Toast.makeText(LoginActivity.this, "getProfileInformation: Name: " + personName +", Email: " + personEmail, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(LoginActivity.this, "Person information is Null!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
