package com.example.android.nitkart;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StoreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ListView listView;

    Context context;
    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList;
    ArrayList<String> images;
    ArrayList<String> product_name;
    ArrayList<String> product_price;
    ArrayList<String> product_id;
    ArrayList<String> seller_name;
    ArrayList<String> seller_phone;
    ArrayList<String> seller_email;
    ArrayList<String> seller_block;
    ArrayList<String> seller_room;
    ArrayList<String> time_period;

    SwipeRefreshLayout swipeRefreshLayout;

    public StoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreFragment newInstance(String param1, String param2) {
        StoreFragment fragment = new StoreFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getContext();

        View view = inflater.inflate(R.layout.fragment_store, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        albumList = new ArrayList<>();
        images = new ArrayList<>();
        product_name = new ArrayList<>();
        product_price = new ArrayList<>();
        product_id = new ArrayList<>();
        seller_name = new ArrayList<>();
        seller_phone = new ArrayList<>();
        seller_email = new ArrayList<>();
        seller_block = new ArrayList<>();
        seller_room = new ArrayList<>();
        time_period = new ArrayList<>();
        adapter = new AlbumsAdapter(getContext(), albumList);

//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                MainActivity.domain + "/user/getProducts/",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject product = response.getJSONObject(i);
                                String url = product.getString("image");
                                String name = product.getString("product_name");
                                String price = product.getString("product_price");
                                String id = product.getString("id");
                                String s_name = product.getString("seller_name");
                                String phone = product.getString("seller_phone");
                                String email = product.getString("seller_email");
                                String block = product.getString("seller_block");
                                String room = product.getString("seller_room");
                                String time = product.getString("time_period");
                                images.add(url);
                                product_name.add(name);
                                product_price.add(price);
                                product_id.add(id);
                                seller_name.add(s_name);
                                seller_phone.add(phone);
                                seller_email.add(email);
                                seller_block.add(block);
                                seller_room.add(room);
                                time_period.add(time);
                            }
                            prepareAlbums();
//                            Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(getContext(), NoInternetActivity.class);
//                        startActivity(intent);
                    }
                }
        );
        SingletonRequestQueue.getInstance(context).addToRequestQueue(jsonArrayRequest);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFragment();
            }
        });
        return view;
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

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        for (int i = 0; i < images.size(); i++) {
            Album a = new Album(product_name.get(i), product_price.get(i), images.get(i), product_id.get(i), seller_name.get(i), seller_phone.get(i), seller_email.get(i), seller_block.get(i), seller_room.get(i), time_period.get(i));
            albumList.add(a);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void refreshFragment() {


        albumList = new ArrayList<>();
        images = new ArrayList<>();
        product_name = new ArrayList<>();
        product_price = new ArrayList<>();
        product_id = new ArrayList<>();
        seller_name = new ArrayList<>();
        seller_phone = new ArrayList<>();
        seller_email = new ArrayList<>();
        seller_block = new ArrayList<>();
        seller_room = new ArrayList<>();
        time_period = new ArrayList<>();
        adapter = new AlbumsAdapter(getContext(), albumList);

//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                MainActivity.domain + "/user/getProducts/",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject product = response.getJSONObject(i);
                                String url = product.getString("image");
                                String name = product.getString("product_name");
                                String price = product.getString("product_price");
                                String id = product.getString("id");
                                String s_name = product.getString("seller_name");
                                String phone = product.getString("seller_phone");
                                String email = product.getString("seller_email");
                                String block = product.getString("seller_block");
                                String room = product.getString("seller_room");
                                String time = product.getString("time_period");
                                images.add(url);
                                product_name.add(name);
                                product_price.add(price);
                                product_id.add(id);
                                seller_name.add(s_name);
                                seller_phone.add(phone);
                                seller_email.add(email);
                                seller_block.add(block);
                                seller_room.add(room);
                                time_period.add(time);
                            }
                            prepareAlbums();
//                            Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                        swipeRefreshLayout.setRefreshing(false);
//                        Intent intent = new Intent(getContext(), NoInternetActivity.class);
//                        startActivity(intent);
                    }
                }
        );
        SingletonRequestQueue.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }


}
