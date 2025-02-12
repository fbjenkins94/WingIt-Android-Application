package com.example.projectwingit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.projectwingit.io.LambdaResponse;
import com.google.android.material.card.MaterialCardView;

import static com.example.projectwingit.io.LambdaRequests.login;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MaterialCardView category1;
    private MaterialCardView category2;
    private MaterialCardView category3;

    public HomeFragment() {
        // Required empty public constructor
    }

    // button for the first card that sends you to the recipe page
   /* private View.OnClickListener cardListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getFragmentManager().beginTransaction().replace(R.id.container, new RecipePageFragment()).commit();
        }
    };*/

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // needed to log the user in from the start if user is logged in
        // LambdaResponse login = login();

        // set the button to switch fragments
        // TODO replace button code using recyclerView which can make a button for all cards
        category1 = v.findViewById(R.id.category1);
        category1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                RecipeList spicyList = new RecipeList();
                Toast.makeText(getActivity().getApplicationContext(), "Searching... Please wait", Toast.LENGTH_LONG).show();
                spicyList.typeResults("", null, null,
                        4, null, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);

                getFragmentManager().beginTransaction().replace(R.id.container, spicyList).addToBackStack(null).commit();
            }
        });

        //TODO: Implement Vegetarian User Characteristic and then put it into the method below
        category2 = v.findViewById(R.id.category2);
        category2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                RecipeList vegetarianList = new RecipeList();
                Toast.makeText(getActivity().getApplicationContext(), "Searching... Please wait", Toast.LENGTH_LONG).show();
                vegetarianList.typeResults("Vegan Vegetarian", null, null,
                        -1, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);

                getFragmentManager().beginTransaction().replace(R.id.container, vegetarianList).addToBackStack(null).commit();
            }
        });


        category3 = v.findViewById(R.id.category3);
        category3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                RecipeList ourFavs = new RecipeList();
                Toast.makeText(getActivity().getApplicationContext(), "Searching... Please wait", Toast.LENGTH_LONG).show();
                ourFavs.typeResults("Sweet", null, null,
                        -1, null, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
                getFragmentManager().beginTransaction().replace(R.id.container, ourFavs).addToBackStack(null).commit();

            }
        });

        // Inflate the layout for this fragment
        return v;
    }
}