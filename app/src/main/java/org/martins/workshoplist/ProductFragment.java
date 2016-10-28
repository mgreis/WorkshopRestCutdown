package org.martins.workshoplist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String user_id;
    private String product_id;
    private String product_text;
    private String product_name;
    private String product_price;
    private String product_qty;
    private boolean add_button;
    private String uri = "http://10.17.0.47:80/products/";

    private OnFragmentInteractionListener mListener;

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
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
        product_text = this.getArguments().getString("product_text","empty");
        product_id = this.getArguments().getString("product_id","empty");
        product_name = this.getArguments().getString("product_name","empty");
        product_price = this.getArguments().getString("product_price","empty");
        product_qty = this.getArguments().getString("product_qty","empty");
        uri = this.getArguments().getString("uri","empty");
        add_button=this.getArguments().getBoolean("add_button",false);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product, container, false);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        TextView productId = (TextView) getView().findViewById(R.id.product_id);
        productId.setText(product_text);
        EditText productName = (EditText) getView().findViewById(R.id.product_name);
        productName.setText(product_name);
        EditText productQty = (EditText) getView().findViewById(R.id.product_qty);
        productQty.setText(product_qty);
        EditText productPrice = (EditText) getView().findViewById(R.id.product_price);
        productPrice.setText(product_price);


        //TODO

        Button back = (Button) getView().findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        Button edit = (Button) getActivity().findViewById(R.id.edit_button);

        if (add_button==true)
        {
            System.out.println("Add");
            edit.setText("Add");
        }

        edit.setOnClickListener (new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Button b = (Button) getActivity().findViewById(R.id.edit_button);
                if (add_button == true){
                    postFragment();
                }
                else {
                    editFragment();
                }
            }

        });

        Button delete = (Button) getActivity().findViewById(R.id.delete_button);
        if (add_button==true) {
            System.out.println("Add");
            ViewGroup viewGroup = (ViewGroup) delete.getParent();
            viewGroup.removeView(delete);

        }
        else {
            delete.setEnabled(true);
        }
        delete.setOnClickListener (new View.OnClickListener(){
            @Override
            public void onClick(View view){
                deleteFragment();
            }

        });


    }


    public void deleteFragment(){
        RequestParams params = new RequestParams();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.delete(uri+user_id+"/"+product_id,params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                getActivity().getSupportFragmentManager().popBackStack();


            }
            // When the response returned by REST has Http response code other than '200'

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable error) {
                treatError(statusCode);
            }
        });
    }





    public void editFragment(){
        RequestParams params = new RequestParams();
        params.add("product_name",((EditText) getView().findViewById(R.id.product_name)).getText().toString());
        params.add("product_price",((EditText) getView().findViewById(R.id.product_price)).getText().toString());
        params.add("product_qty", ((EditText) getView().findViewById(R.id.product_qty)).getText().toString());
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.put(uri+user_id+"/"+product_id,params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                getActivity().getSupportFragmentManager().popBackStack();


            }
            // When the response returned by REST has Http response code other than '200'

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable error) {
                treatError(statusCode);
            }
        });

    }

    public void postFragment(){
        RequestParams params = new RequestParams();
        params.add("product_name",((EditText) getView().findViewById(R.id.product_name)).getText().toString());
        params.add("product_price",((EditText) getView().findViewById(R.id.product_price)).getText().toString());
        params.add("product_qty", ((EditText) getView().findViewById(R.id.product_qty)).getText().toString());
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(uri+user_id,params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                getActivity().getSupportFragmentManager().popBackStack();

            }
            // When the response returned by REST has Http response code other than '200'

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable error) {
                treatError(statusCode);

            }
        });
    }

    public void treatError(int statusCode){
        // When Http response code is '404'
        if(statusCode == 404){
            Toast.makeText(getActivity().getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
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
