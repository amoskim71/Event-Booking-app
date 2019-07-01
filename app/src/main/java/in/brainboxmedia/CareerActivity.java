package in.brainboxmedia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */

public class CareerActivity extends AppCompatActivity {

    private TextView website;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career);
        Toolbar toolbar = findViewById(R.id.toolbar_career);
        setSupportActionBar(toolbar);
        toolbar.setTitle(" ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_black));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CareerActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        website = findViewById(R.id.website_brain_box_career);
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(website.getText().toString())));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(CareerActivity.this, "There are no browser clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
