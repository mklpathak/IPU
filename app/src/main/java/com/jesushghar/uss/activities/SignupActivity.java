package com.jesushghar.uss.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jesushghar.uss.R;
import com.jesushghar.uss.helper.SQLiteHandler;
import com.jesushghar.uss.helper.SessionManager;
import com.jesushghar.uss.utils.AppConfig;
import com.jesushghar.uss.utils.AppController;
import com.jesushghar.uss.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = SignupActivity.class.getSimpleName();


    private EditText inputName;
    private EditText inputEmail;
    private EditText inputPassword;
    private Spinner roleSpinner;
    private Spinner schoolSpinner;

    private Button btnRegister;
    private TextView btnLogin;

    private ProgressDialog progressDialog;
    private SessionManager session;
    private SQLiteHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        inputName = (EditText) findViewById(R.id.input_name);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        roleSpinner = (Spinner) findViewById(R.id.role);
        schoolSpinner = (Spinner) findViewById(R.id.school);

        btnRegister = (Button) findViewById(R.id.btn_signup);
        btnLogin = (TextView) findViewById(R.id.link_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());

        db = new SQLiteHandler(getApplicationContext());

        if (session.isLoggedIn()) {
            Intent intent = new Intent(SignupActivity.this,
                    HomeActivity.class);
            startActivity(intent);
            finish();
        }


        //Validate Email.
        inputEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!Utility.emailValidator(inputEmail.getText().toString())) {
                        //Toast.makeText(getApplicationContext(),"Enter valid email!", Toast.LENGTH_SHORT).show();
                        AlertDialog alertDialog = new AlertDialog.Builder(SignupActivity.this).create();
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


        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.role, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(R.layout.my_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);

        ArrayAdapter<CharSequence> schoolAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.university_schools, android.R.layout.simple_spinner_item);
        schoolAdapter.setDropDownViewResource(R.layout.my_spinner_dropdown_item);
        schoolSpinner.setAdapter(schoolAdapter);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String role = roleSpinner.getSelectedItem().toString();
                String school = schoolSpinner.getSelectedItem().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(name, email, password, role, school);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }


    private void registerUser(final String name, final String email, final String password, final String role, final String school) {
        String tag_string_req = "req_register";

        progressDialog.setMessage("Registering...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Signup Response: " + response.toString());
                        hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            if (!error) {
                                // User successfully stored in MySQL
                                // Now store the user in sqlite
                                String uid = jObj.getString("uid");

                                JSONObject user = jObj.getJSONObject("user");
                                String name = user.getString("name");
                                String email = user.getString("email");
                                String role = user.getString("role");
                                String school = user.getString("school");
                                String created_at = user.getString("created_at");

                                // Inserting row in users table
                                db.addUser(name, email, role, school, uid, created_at);

                                Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                                // Launch login activity
                                Intent intent = new Intent(
                                        SignupActivity.this,
                                        LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {

                                // Error occurred in registration. Get the error
                                // message
                                String errorMsg = jObj.getString("error_msg");
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Registration Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("role", role);
                params.put("school", school);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
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

}
