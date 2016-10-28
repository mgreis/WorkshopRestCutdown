package org.martins.workshoplist;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;

public class HomeActivity extends AppCompatActivity implements MenuFragment.OnFragmentInteractionListener , ProductFragment.OnFragmentInteractionListener{
    private static final String URI = "http://10.17.0.47:80/products/";
    private String user_id;
    private String user_email;
    private Intent intent;
    private MenuFragment menuFragment;
    private Bundle menuBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        intent = getIntent();
        user_email = intent.getStringExtra(LoginActivity.USER_EMAIL);
        user_id = intent.getStringExtra(LoginActivity.USER_ID);

        menuFragment = new MenuFragment();
        menuBundle = new Bundle();


        menuBundle.putString("user_email", user_email);
        menuBundle.putString("user_id", user_id);
        menuBundle.putString("uri", URI);


        menuFragment.setArguments(menuBundle);


        getSupportFragmentManager().beginTransaction().replace(R.id.activity_home, menuFragment).commit();


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
