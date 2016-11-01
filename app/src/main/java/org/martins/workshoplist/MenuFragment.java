package org.martins.workshoplist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Bundle bundle;
    private String uri = "http://10.17.0.47:80/products/";
    private String user_id;
    private String user_email;
    private JSONArray productArray;
    private String wSResponse;
    public ArrayList<Button> productButtonsList = new ArrayList<>();
    private ProductFragment productFragment;


    private OnFragmentInteractionListener mListener;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        user_id = this.getArguments().getString("user_id","empty");
        System.out.println("USER_ID: "+user_id);
        user_email = this.getArguments().getString("product_id","empty");
        uri = this.getArguments().getString("uri","empty");


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getProductsFromWS();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    public void getProductsFromWS(){
        /**
         * Create the RequestParams object;
         * Create the AsyncHttpClient object;
         * call the get() method with the uri+user_id, params and the AsynchResponseHandler interface as parameters
         */
    }


    private void launchButtons() {
        System.out.println("FragTime!");


        if(getProductArray()==null){return;}


        for (int i = 0; i < getProductArray().length(); i++) {
            productButtonsList.add(new Button(getActivity()));
            try {
                productButtonsList.get(i).setText(getProductArray().getJSONObject(i).getString("product_name"));
                productButtonsList.get(i).setTag(getProductArray().getJSONObject(i));
                productButtonsList.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            System.out.println (((JSONObject)v.getTag()).get("user_id"));
                            productFragment = new ProductFragment();
                            bundle = new Bundle();

                            bundle.putString("product_text",    "Edit Product:");
                            bundle.putString("product_id",      ((JSONObject)v.getTag()).get("product_id").toString());
                            bundle.putString("product_name",    ((JSONObject)v.getTag()).get("product_name").toString());
                            bundle.putString("product_price",   ((JSONObject)v.getTag()).get("product_price").toString());
                            bundle.putString("product_qty",     ((JSONObject)v.getTag()).get("product_qty").toString());
                            bundle.putString("user_id",         ((JSONObject)v.getTag()).get("user_id").toString());
                            System.out.println("USER_ID HOME: "+((JSONObject)v.getTag()).get("user_id").toString());
                            bundle.putBoolean("add_button",false);
                            bundle.putString("uri",uri);


                            productFragment.setArguments(bundle);


                            FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.activity_home, productFragment);
                            ft.addToBackStack("MAIN");
                            LinearLayout layout = (LinearLayout) getView().findViewById(R.id.linearLayoutFrag);
                            layout.removeAllViews();
                            ft.commit();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                LinearLayout layout = (LinearLayout) getView().findViewById(R.id.linearLayoutFrag);

                layout.addView(productButtonsList.get(i));



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }



    private void configureAddButton(){
        Button addButton = (Button) getView().findViewById(R.id.addButton);
        addButton.setTag(user_id);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                productFragment = new ProductFragment();
                bundle = new Bundle();

                bundle.putString("product_text", "Add new Product:");
                bundle.putString("product_id", "Add new Product:");
                bundle.putString("product_name", "");
                bundle.putString("product_price","");
                bundle.putString("product_qty", "");
                bundle.putString("user_id", user_id);
                bundle.putBoolean("add_button",true);
                bundle.putString("uri",uri);


                productFragment.setArguments(bundle);

                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.activity_home, productFragment);
                ft.addToBackStack("MAIN");
                LinearLayout layout = (LinearLayout) getView().findViewById(R.id.linearLayoutFrag);
                layout.removeAllViews();
                ft.commit();

            }
        });
    }



    public JSONArray getProductArray() {
        return productArray;
    }

    public void setProductArray(JSONArray productArray) {
        this.productArray = productArray;
    }

    public String getwSResponse() {
        return wSResponse;
    }

    public void setwSResponse(String wSResponse) {
        this.wSResponse = wSResponse;
    }

    public void success(byte[] bytes){
        setwSResponse(new String(bytes));
        // Hide Progress Dialog
        try {
            // JSON Object
            System.out.println(getwSResponse());
            setProductArray(new JSONArray(getwSResponse()));
            //view.setText (wSResponse);
            launchButtons();
            configureAddButton();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getActivity().getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }

    }

    public void failure(int statusCode){
        // When Http response code is '404'
        if(statusCode == 404){
            configureAddButton();
        }
        // When Http response code is '500'
        else if(statusCode == 500){
            Toast.makeText(getActivity().getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
        }
        // When Http response code other than 404, 500
        else{
            Toast.makeText(getActivity().getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
        }

    }

}
