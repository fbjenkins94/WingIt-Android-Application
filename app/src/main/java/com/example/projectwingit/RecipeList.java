package com.example.projectwingit;

import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.projectwingit.io.LambdaRequests;
import com.example.projectwingit.io.LambdaResponse;
import com.example.projectwingit.io.UserInfo;
import com.example.projectwingit.utils.WingitUtils;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.projectwingit.io.LambdaRequests.getRecipe;
import static com.example.projectwingit.io.LambdaRequests.login;
import static com.example.projectwingit.io.LambdaRequests.searchRecipes;
import static com.example.projectwingit.utils.WingitLambdaConstants.FAVORITED_RECIPES_STR;
import static com.example.projectwingit.utils.WingitLambdaConstants.QUERY_RESULTS_STR;
import static com.example.projectwingit.utils.WingitLambdaConstants.RECIPE_DESCRIPTION_STR;
import static com.example.projectwingit.utils.WingitLambdaConstants.RECIPE_ID_STR;
import static com.example.projectwingit.utils.WingitLambdaConstants.RECIPE_PICTURE_STR;
import static com.example.projectwingit.utils.WingitLambdaConstants.RECIPE_RATING_STR;
import static com.example.projectwingit.utils.WingitLambdaConstants.RECIPE_TITLE_STR;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeList extends Fragment implements RecipeListRecyclerViewAdapter.OnRecipeListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * TODO: private ArrayList<recipe_id> recipe_arrayList = new ArrayList<>();
     */
    private ArrayList<String> mRecipeImageUrls = new ArrayList<>();
    private ArrayList<String> mRecipeTitles = new ArrayList<>();
    private ArrayList<String> mRecipeCategories = new ArrayList<>();
    private ArrayList<String> mRecipeDescriptions = new ArrayList<>();
    private ArrayList<Integer> mRecipeID = new ArrayList<>();
    private ArrayList<Boolean> mIsFavorites = new ArrayList<>();

    private String loginUsername = "JustWingit";
    private String loginEmail = "cse110wingit@gmail.com";

    private String recipeSearchText;
    private int spiciness;
    private Boolean nutAllergy;
    private Boolean glutenFree;
    private Boolean vegetarian;
    private Boolean isFavoritesPage;
    private Boolean isOurFavoritesPage;


    private Boolean initializedCards = Boolean.FALSE;
    private JSONObject recipeCardInfo;

    public RecipeList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeList.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeList newInstance(String param1, String param2) {
        RecipeList fragment = new RecipeList();
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
        View v = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        if (isFavoritesPage) {
            initFavorites(v);
        }
        else if (isOurFavoritesPage) {
            initOurFavorites(v);
        }
        else {
            initImageBitmaps(v);
        }

        // Inflate the layout for this fragment
        return v;
    }


    private void initImageBitmaps(View v) {
        LambdaResponse login = login();

        if(initializedCards) {
            initRecyclerView(v);
        }
        else {
            initializedCards = Boolean.TRUE;
            LambdaResponse lr = searchRecipes(recipeSearchText, nutAllergy, glutenFree, vegetarian, spiciness);

            JSONObject joe = lr.getResponseJSON();

            JSONArray ja = new JSONArray();
            try {
                ja = joe.getJSONArray(QUERY_RESULTS_STR);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject recipeID = new JSONObject();
            LambdaResponse recipeObject;
            String recipeIDString = "";
            JSONObject recipeJSONObject;

            try {
                for (int i = 0; i < ja.length(); i++) {
                    recipeIDString = ja.getString(i);
                    int id = Integer.parseInt(recipeIDString);

                    recipeObject = getRecipe(id);

                    recipeJSONObject = recipeObject.getResponseJSON();

                    String testRecipeName = recipeJSONObject.getString(RECIPE_TITLE_STR);
                    String recipeRating = "";
                    if(!(testRecipeName.contains("recipe1"))) {
                        mRecipeImageUrls.add(recipeJSONObject.getString(RECIPE_PICTURE_STR));
                        Log.d("Picture URL: ", recipeJSONObject.getString(RECIPE_PICTURE_STR));
                        Log.d("Recipe Title: ", recipeJSONObject.getString(RECIPE_TITLE_STR));

                        mRecipeTitles.add(recipeJSONObject.getString(RECIPE_TITLE_STR));
                        mRecipeDescriptions.add(recipeJSONObject.getString(RECIPE_DESCRIPTION_STR));

                        if(recipeJSONObject.getString(RECIPE_RATING_STR).equalsIgnoreCase("null")){
                            recipeRating += "Not Rated";
                        }
                        else{
                            recipeRating += "Rating: " + recipeJSONObject.getString(RECIPE_RATING_STR) + " Stars";
                        }
                        mRecipeCategories.add(recipeRating);

                        String[] recipeIDList;
                        recipeIDList = UserInfo.CURRENT_USER.getFavoritedRecipes();
                        if (recipeIDList != null) {
                            boolean favVal = false;

                            for (int z = 0; z < recipeIDList.length; z++) {
                                if (recipeIDList[z] != null) {
                                    if (recipeIDList[z].equals(recipeIDString)) favVal = true;
                                }
                            }

                            mIsFavorites.add(favVal);
                            mRecipeID.add(id);
                        }
                        else {
                            mIsFavorites.add(false);
                            mRecipeID.add(id);
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            initRecyclerView(v);
        }

    }

    private void initRecyclerView(View v) {
        RecyclerView recyclerView = (RecyclerView)  v.findViewById(R.id.recycler_view_container);
        RecipeListRecyclerViewAdapter adapter = new RecipeListRecyclerViewAdapter(mRecipeImageUrls, mRecipeTitles, mRecipeCategories,mRecipeDescriptions, getContext(), this, mIsFavorites);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onRecipeClick(int position) {
        /**
         * TODO: 1. Pass recipe_id from (int position) <- which is the recipe card, i presume
         * TODO: 2. Debug recipe list duplication upon pressing the back button
         * TODO:      a. we could delete all current recipe cards (int positions) upon entering this method
         */

        // TODO eventually we will pass in an entire recipe object here but for now it is just the title
        Fragment recipeFragment = new RecipePageFragment(mRecipeID.get(position));
        getFragmentManager().beginTransaction().replace(R.id.container, recipeFragment).addToBackStack(null).commit();
    }

    /**
     * @param recipeSearchText
     *      1. This is the String that the user enters in SearchFragment
     *      2. This String represents the recipe that the user would like to search in the WingIt Database
     *      3. We will make a request to the Lambda API using this string in the onCreateView method above.
     */
    public void typeResults(String recipeSearchText, Boolean nutAllergy, Boolean glutenFree, int spiciness,
                            Boolean vegetarian, Boolean isFavoritesPage, Boolean isOurFavoritesPage) {
        this.recipeSearchText = recipeSearchText;
        this.spiciness = spiciness;
        this.nutAllergy = nutAllergy;
        this.glutenFree = glutenFree;
        this.vegetarian = vegetarian;
        this.isFavoritesPage = isFavoritesPage;
        this.isOurFavoritesPage = isOurFavoritesPage;
    }

    private void initFavorites(View v) {
        if(initializedCards) initRecyclerView(v);
        else {
            initializedCards = Boolean.TRUE;

            String[] recipeIDList;
            recipeIDList = UserInfo.CURRENT_USER.getFavoritedRecipes();

            LambdaResponse recipeObject;
            String recipeIDString = "";
            JSONObject recipeJSONObject;

            try {
                for (int i = 0; i < recipeIDList.length; i++) {
                    if (recipeIDList[i] != null) {
                        recipeIDString = recipeIDList[i];
                        int id = Integer.parseInt(recipeIDString);
                        recipeObject = getRecipe(id);

                        recipeJSONObject = recipeObject.getResponseJSON();

                        mRecipeImageUrls.add(recipeJSONObject.getString(RECIPE_PICTURE_STR));
                        mRecipeTitles.add(recipeJSONObject.getString(RECIPE_TITLE_STR));
                        mRecipeCategories.add("Category " + i);
                        mRecipeDescriptions.add(recipeJSONObject.getString(RECIPE_DESCRIPTION_STR));
                        mRecipeID.add(id);
                        mIsFavorites.add(Boolean.TRUE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            initRecyclerView(v);
        }

    }

    private void initOurFavorites(View v) {
        if(initializedCards) initRecyclerView(v);
        else {
            initializedCards = Boolean.TRUE;

            String[] recipeIDList = {"25","19","35","9","29"};

            LambdaResponse recipeObject;
            String recipeIDString = "";
            JSONObject recipeJSONObject;

            try {
                for (int i = 0; i < recipeIDList.length; i++) {
                    if (recipeIDList[i] != null) {
                        recipeIDString = recipeIDList[i];
                        int id = Integer.parseInt(recipeIDString);
                        recipeObject = getRecipe(id);

                        recipeJSONObject = recipeObject.getResponseJSON();

                        mRecipeImageUrls.add(recipeJSONObject.getString(RECIPE_PICTURE_STR));
                        mRecipeTitles.add(recipeJSONObject.getString(RECIPE_TITLE_STR));
                        mRecipeCategories.add("Category " + i);
                        mRecipeDescriptions.add(recipeJSONObject.getString(RECIPE_DESCRIPTION_STR));
                        mRecipeID.add(id);

                        String[] userFavs;
                        userFavs = UserInfo.CURRENT_USER.getFavoritedRecipes();
                        if (userFavs != null) {
                            boolean favVal = false;

                            for (int z = 0; z < userFavs.length; z++) {
                                if (userFavs[z] != null) {
                                    if (userFavs[z].equals(recipeIDString)) favVal = true;
                                }
                            }

                            mIsFavorites.add(favVal);
                            mRecipeID.add(id);
                        }
                        else {
                            mIsFavorites.add(false);
                            mRecipeID.add(id);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            initRecyclerView(v);
        }

    }


}
