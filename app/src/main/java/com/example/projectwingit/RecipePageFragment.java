package com.example.projectwingit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.Dialog;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectwingit.io.LambdaResponse;
import com.example.projectwingit.utils.LoginInfo;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.projectwingit.io.LambdaRequests.deleteRecipe;
import static com.example.projectwingit.io.LambdaRequests.favoriteRecipe;
import static com.example.projectwingit.io.LambdaRequests.getRecipe;
import static com.example.projectwingit.utils.WingitLambdaConstants.FAVORITED_RECIPES_STR;
import static com.example.projectwingit.utils.WingitLambdaConstants.GLUTEN_FREE_STR;
import static com.example.projectwingit.utils.WingitLambdaConstants.NUT_ALLERGY_STR;
import static com.example.projectwingit.utils.WingitLambdaConstants.RECIPE_DESCRIPTION_STR;
import static com.example.projectwingit.utils.WingitLambdaConstants.RECIPE_INGREDIENTS_STR;
import static com.example.projectwingit.utils.WingitLambdaConstants.RECIPE_PICTURE_STR;
import static com.example.projectwingit.utils.WingitLambdaConstants.RECIPE_TITLE_STR;
import static com.example.projectwingit.utils.WingitLambdaConstants.RECIPE_TUTORIAL_STR;
import static com.example.projectwingit.utils.WingitLambdaConstants.USERNAME_STR;

// TODO what does this do?
//A simple {@link Fragment} subclass.
//Use the {@link Settings#newInstance} factory method to
//create an instance of this fragment.

public class RecipePageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button rateButton;
    Dialog rateDialog;
    private int recipeID;
    /**
     * TODO: We will fill the following TextView widgets with recipe fields
     *
     * TODO: private ImageView recipe_Image_View;          <- recipe_id.API_LAMBDA_GET_RECIPE_IMG()
     * TODO: private TextView recipe_Title_View;           <- recipe_id.API_LAMBDA_GET_RECIPE_TITLE()
     * TODO: private TextView recipe_Description_View;     <- recipe_id.API_LAMBDA_GET_RECIPE_DESCRIPTION()
     * TODO: private TextView recipe_Ingredients_View;     <- recipe_id.API_LAMBDA_GET_RECIPE_INGREDIENTS()
     * TODO: private TextView recipe_Instructions_View;    <- recipe_id.API_LAMBDA_GET_RECIPE_INSTRUCTIONS()
     * TODO: private TextView recipe_Spiciness_View;       <- recipe_id.API_LAMBDA_GET_RECIPE_SPICINESS()
     * TODO: private TextView recipe_Nut_Allergy_View;     <- recipe_id.API_LAMBDA_GET_RECIPE_NUT_ALLERGY()
     * TODO: private TextView recipe_Gluten_Free_View;     <- recipe_id.API_LAMBDA_GET_RECIPE_GLUTEN_FREE()
     */

    public RecipePageFragment() {
        // Required empty public constructor
    }

    // recipe page fragment that passes in recipe info
    // TODO obviously have more than just the title being passed in
    public RecipePageFragment(int recipeID) {
        this.recipeID = recipeID;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Settings.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipePageFragment newInstance(String param1, String param2) {
        RecipePageFragment fragment = new RecipePageFragment();
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
        View v = inflater.inflate(R.layout.fragment_recipe_page, container, false);
        System.out.println(LoginInfo.CURRENT_LOGIN.username);
        LambdaResponse recipeLambdaResponse = getRecipe(recipeID);
        while (recipeLambdaResponse.isRunning()) {}

        JSONObject recipeObject = recipeLambdaResponse.getResponseJSON();
        while (recipeLambdaResponse.isRunning()) {}

        ImageView recipeImage = v.findViewById(R.id.Recipe_ImageView);
        TextView titleText = v.findViewById(R.id.titleText);
        TextView descriptionText = v.findViewById(R.id.Description_TextView); //TODO: USE CORRECT ID
        TextView ingredientsText = v.findViewById(R.id.Ingredients_TextView);
        TextView instructionsText = v.findViewById(R.id.textViewInstructions);
        TextView nutAllergyText = v.findViewById(R.id.textViewAllergy);

        try {

            titleText.setText(recipeObject.getString(RECIPE_TITLE_STR));
            descriptionText.setText(recipeObject.getString(RECIPE_DESCRIPTION_STR));
            Glide.with(this).load(recipeObject.getString(RECIPE_PICTURE_STR)).into(recipeImage);
            ingredientsText.setText(recipeObject.getString(RECIPE_INGREDIENTS_STR));
            instructionsText.setText(recipeObject.getString(RECIPE_TUTORIAL_STR));

            String allergyString = "";
            if(recipeObject.getBoolean(NUT_ALLERGY_STR)) allergyString += "This recipe contains nuts. ";
            else allergyString += "This recipe does not contain nuts. ";
            if(recipeObject.getBoolean(GLUTEN_FREE_STR)) allergyString += "This recipe is gluten-free.";
            else allergyString += "This recipe contains gluten.";
            nutAllergyText.setText(allergyString);
        } catch (JSONException e) {
            e.printStackTrace();
        }








        /**
         * TODO: 1. Implement a loading graphic for 1 second
         * TODO: 2. Fill widgets with recipe fields
         * TODO      a. while( index(ingredient_arrayList) != last ) ->  recipe_Ingredients_View.setText(ingredient_arrayList[i] + "\n");
         * TODO:     b. while( index(instruction_arrayList) != last ) -> dynamically (recipe_Instructions_View.setText(instructions_arrayList[i] + " ");)
         */

        // rating dialog characteristics TODO maybe make the rating dialog its own method
        rateDialog = new Dialog(getActivity());
        rateDialog.setContentView(R.layout.rating_dialog);
        rateDialog.setCancelable(false);

        Button cancel = rateDialog.findViewById(R.id.cancel_dialog_button);
        Button okay = rateDialog.findViewById(R.id.ok_dialog_button);
        Button cookingTutorialButton = v.findViewById(R.id.Tutorial_Button);
        MaterialButton favoriteButton = v.findViewById(R.id.recipe_page_fav_button);

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                rateDialog.dismiss();
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                LambdaResponse lr = getRecipe(recipeID);

                JSONObject joe = lr.getResponseJSON();
                while (lr.isRunning()) {}

                JSONArray favs = new JSONArray();
                try {
                    favs = joe.getJSONArray(FAVORITED_RECIPES_STR);
                } catch (JSONException e){
                    e.printStackTrace();
                }

                try {
                    boolean favVal = false;
                    for (int i = 0; i < favs.length(); i++) {
                        if (favs.getString(i).equals("" + recipeID)) favVal = true;
                    }


                    LambdaResponse favreq = favoriteRecipe("" + recipeID);
                    favoriteButton.setIconResource(R.drawable.ic_recipe_in_favorites);
                    favoriteButton.setText("In Favorites");
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

        okay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                rateDialog.dismiss();
            }
        });

        // rating button characteristics
        rateButton = (Button) v.findViewById(R.id.ratebutton);
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateDialog.show();
            }
        });

        cookingTutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment h = new HomeFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, h).addToBackStack(null).commit();
            }
        });





        // Inflate the layout for this fragment
        return v;
    }
}