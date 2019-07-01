package in.brainboxmedia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import in.brainboxmedia.data.Services;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */

public class ServicesActivity extends AppCompatActivity {

    private Services data;
    private ProgressDialog progress;
    private TextView services, corporate, list, manage, offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        Toolbar toolbar = findViewById(R.id.toolbar_services);
        setSupportActionBar(toolbar);
        toolbar.setTitle(" ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ServicesActivity.this, MainActivity.class));
            }
        });

        progress = new ProgressDialog(this);
        progress.setMessage("fetching the data mate :) ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
        services = findViewById(R.id.services_Text);
        corporate = findViewById(R.id.corporate);
        list = findViewById(R.id.list_text);
        manage = findViewById(R.id.event_manage_text);
        offer = findViewById(R.id.offers_text);
        GetDataFirebase();
    }

    private void GetDataFirebase() {
        Query about = FirebaseDatabase.getInstance()
                .getReference()
                .child("services");
        about.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data = dataSnapshot.getValue(Services.class);

                services.setText(data.getService());
                corporate.setText(data.getCorporate());
                list.setText(data.getlist_services());
                progress.dismiss();
                manage.setText(data.getManage());
                offer.setText(data.getOffers());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("servicesActivity", "Failed to read app title value.", databaseError.toException());
            }
        });
    }


}
