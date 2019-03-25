package be.cosci.ibm.ucllwatson.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.cosci.ibm.ucllwatson.R;
import be.cosci.ibm.ucllwatson.WatsonImplementation;
import be.cosci.ibm.ucllwatson.adapter.IngredientRecycleViewAdapter;
import be.cosci.ibm.ucllwatson.adapter.RecipeRecycleViewAdapter;
import be.cosci.ibm.ucllwatson.db.PhotosRepository;
import be.cosci.ibm.ucllwatson.db.item.PhotoItem;
import be.cosci.ibm.ucllwatson.utils.ConnectionUtil;
import be.cosci.ibm.ucllwatson.utils.JSONUtils;
import be.cosci.ibm.ucllwatson.utils.item.ReceiptItem;

/**
 *
 */
public class SearchActivity extends AppCompatActivity {

    private final String RECEIPTS_URL = "https://openwhisk.eu-gb.bluemix.net/api/v1/web/vincent.ravoet%40student.pxl.be_dev/default/recipe-finder-action.json";

    private RecyclerView ingredients;
    private RecyclerView recipes;

    private String data;
    private IngredientRecycleViewAdapter ingredientRecycleViewAdapter;
    private RecipeRecycleViewAdapter recipeRecycleViewAdapter;
    private List<ReceiptItem> receiptItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
        setToolbar();
    }

    public void refreshRecipes() {
        loadRecipes();
    }

    /**
     * Download JSON from recipes from server
     */
    public void loadRecipes() {
        if (ConnectionUtil.showNetworkInfoSnackbar(this, R.id.search_content)) {
            return;
        }
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message.what == RESULT_OK) {
                    receiptItems = JSONUtils.parseJSONtoReceiptItems(data);
                    recipeRecycleViewAdapter.setData(receiptItems);
                } else {
                    recipeRecycleViewAdapter.setData(new ArrayList<ReceiptItem>());
                }
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (new PhotosRepository(SearchActivity.this).getAllItems().size() == 0) {
                        handler.sendEmptyMessage(RESULT_CANCELED);
                    } else {
                        List<PhotoItem> photoItems = new PhotosRepository(SearchActivity.this).getAllItems();
                        String[] parameters = new String[photoItems.size()];
                        for (int i = 0; i < photoItems.size(); i++) {
                            parameters[i] = photoItems.get(i).getIngredientName();
                        }
                        data = JSONUtils.downloadJSON(RECEIPTS_URL, parameters);
                        handler.sendEmptyMessage(RESULT_OK);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Initialize views
     */
    private void initViews() {
        ingredients = findViewById(R.id.list_ingredients);
        setUpIngredients(ingredients);
        recipes = findViewById(R.id.list_recipes);
        setUpRecipes(recipes);
    }

    /**
     * @param ingredients
     */
    private void setUpIngredients(RecyclerView ingredients) {
        List<PhotoItem> ingredientItemList = new PhotosRepository(this).getAllItems();
        ingredientRecycleViewAdapter = new IngredientRecycleViewAdapter(this, ingredientItemList);
        int counter = 0;
        for (PhotoItem photoItem : ingredientItemList) {
            try {
                if (photoItem.getIngredientName().isEmpty()) {
                    WatsonImplementation.find(photoItem, ingredientRecycleViewAdapter, this);
                } else counter++;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (counter == ingredientItemList.size()) loadRecipes();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ingredients.setLayoutManager(linearLayoutManager);
        ingredients.setAdapter(ingredientRecycleViewAdapter);
    }

    /**
     * @param recipes
     */
    private void setUpRecipes(RecyclerView recipes) {
        recipeRecycleViewAdapter = new RecipeRecycleViewAdapter(this, receiptItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recipes.setLayoutManager(linearLayoutManager);
        recipes.setAdapter(recipeRecycleViewAdapter);
    }

    /**
     * Set toolbar
     */
    private void setToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_settings:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}