package be.cosci.ibm.ucllwatson.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import be.cosci.ibm.ucllwatson.R;
import be.cosci.ibm.ucllwatson.db.PhotosRepository;

/**
 * Created by Petr on 28-Mar-18.
 */
public class IngredientsDetail extends AppCompatActivity {

    public static final String INGREDIENT_INTENT = "be.cosci.ibm.ucllwatson.adapter.IMAGE_ID";

    private long id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredience_detail);

        initViews();
        setToolbar();
    }

    /**
     * Set toolbar and its title
     */
    private void setToolbar() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(new PhotosRepository(this).getItem(id).getIngredientName());
    }

    /**
     * Initialize views
     */
    private void initViews() {
        ImageView imageView = findViewById(R.id.ingredient_image);
        id = getIntent().getExtras().getLong(INGREDIENT_INTENT);
        Picasso.get().load(new File(new PhotosRepository(this).getItem(id).getPath())).rotate(90f).into(imageView);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}