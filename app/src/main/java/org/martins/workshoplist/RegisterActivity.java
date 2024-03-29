package org.martins.workshoplist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private String uri = "http://10.17.0.47:80/users";
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

    }

    public void toLogin(View view){
        pDialog.cancel();
        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void register(View view){

        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()) {
            if (Utility.validate(email)) {
                /**
                 * create RequestParams object, populate it with "user_email" and "user_password"
                 * call invokeWS() with the requestParams Object
                 */
                // When Email is invalid
            } else {
                Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWS(RequestParams params){
        // Show Progress Dialog
        pDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        /**
         * Create the AsyncHttpClient object;
         * call the post() method with the uri, params and the AsynchResponseHandler interface as parameters
         */
    }

    public void setDefaultValues(){
        inputEmail.setText("");
        inputPassword.setText("");
    }

    public void toLoginActivity(){
        Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    public void success (byte[] bytes){
        String response = new String(bytes);

        System.out.println("STRING: "+response);
        // Hide Progress Dialog
        pDialog.hide();

        // Set Default Values for Edit View controls
        setDefaultValues();
        // Display successfully registered message using Toast
        Toast.makeText(getApplicationContext(), "You are successfully registered!", Toast.LENGTH_LONG).show();
        toLoginActivity();
    }

    public void failure(int statusCode){
        // Hide Progress Dialog
        pDialog.hide();
        // When Http response code is '404'
        if(statusCode == 403){
            Toast.makeText(getApplicationContext(), "Email already registered", Toast.LENGTH_LONG).show();
        }

        else if(statusCode == 404){
            Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
        }
        // When Http response code is '500'
        else if(statusCode == 500){
            Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
        }
        // When Http response code other than 404, 500
        else{
            Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
        }

    }

}
