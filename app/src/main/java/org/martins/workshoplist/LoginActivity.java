//http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/
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

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;

    private String uri = "http://10.17.0.47:80/users";
    public static String USER_ID = "cm1617.workshopclient.HomeActivity.USER_ID";
    public static String USER_EMAIL = "cm1617.workshopclient.HomeActivity.USER_EMAIL";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


    }


    public void login(View view){

        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        // Check for empty data in the form
        if (!email.isEmpty() && !password.isEmpty()) {
            if(Utility.validate(email)){

                RequestParams params = new RequestParams();

                // Put Http parameter username with value of Email Edit View control
                params.put("user_email", email);
                // Put Http parameter password with value of Password Edit Value control
                params.put("user_password", password);
                // Invoke RESTful Web Service with Http parameters
                System.out.println(email+password);
                invokeWS(params);
            }
            // When Email is invalid
            else{
                Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
            }
        }
        else {
            // Prompt user to enter credentials
            Toast.makeText(getApplicationContext(),"Please enter the credentials!", Toast.LENGTH_LONG).show();
        }

    }

    public void toRegister(View view){
        pDialog.cancel();
        Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(i);
        finish();
    }


    public void invokeWS(RequestParams params){
        // Show Progress Dialog
        pDialog.show();

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(uri,params ,new AsyncHttpResponseHandler() {

            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                String response = new String(bytes);
                // Hide Progress Dialog
                pDialog.hide();

                System.out.println (response);
                try {
                    // JSON Object
                    JSONObject obj = (new JSONArray(response)).getJSONObject(0);
                    Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                    navigatetoHomeActivity(obj);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }

            // When the response returned by REST has Http response code other than '200'

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {

                // Hide Progress Dialog
                pDialog.hide();

                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Wrong email or password", Toast.LENGTH_LONG).show();
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
        });
    }

    /**
     * Method which navigates from Login Activity to Home Activity
     */
    public void navigatetoHomeActivity(JSONObject obj){
        Intent homeIntent = new Intent(getApplicationContext(),HomeActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
            homeIntent.putExtra(USER_ID,obj.getString("user_id"));
            homeIntent.putExtra(USER_EMAIL, obj.getString("user_email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(homeIntent);
    }

}
