package be.cosci.ibm.ucllwatson.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import be.cosci.ibm.ucllwatson.R;
import be.cosci.ibm.ucllwatson.adapter.AbouUsAdapter;

/**
 * Created by Petr on 28-Mar-18.
 */
public class AboutActivity extends AppCompatActivity {

    private ImageView schoolLogo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();
        setToolbar();
        setUpRecycleView();
    }

    /**
     *
     */
    private void initViews() {
        this.schoolLogo = findViewById(R.id.school_logo);
        setPulseAnim(schoolLogo);
        setOnSchoolLogoClickListener(schoolLogo);
    }

    /**
     * @param imageView
     */
    private void setPulseAnim(ImageView imageView) {
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                imageView,
                PropertyValuesHolder.ofFloat("scaleX", 1.13f),
                PropertyValuesHolder.ofFloat("scaleY", 1.13f));
        scaleDown.setDuration(700);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();
    }

    /**
     * @param imageView
     */
    private void setOnSchoolLogoClickListener(ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ucll.be/"));
                startActivity(browserIntent);
            }
        });
    }

    /**
     * Set toolbar
     */
    private void setToolbar() {
        getSupportActionBar().setTitle(R.string.about_title_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
    }

    /**
     * Set up Recycler'sView adapter
     */
    private void setUpRecycleView() {
        List<String> names = new ArrayList<>();
        names.add("De Maere Lars");
        names.add("Ramanauskas Matas");
        names.add("Ravoet Vincent");
        names.add("Tykal Petr");
        names.add("Vanisterbecq Brent");
        names.add("Vermeire Henok");
        RecyclerView recyclerView = findViewById(R.id.about_us_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        AbouUsAdapter aboutUsAdapter = new AbouUsAdapter(this, names);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(aboutUsAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}