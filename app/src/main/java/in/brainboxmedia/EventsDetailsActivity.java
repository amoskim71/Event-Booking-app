package in.brainboxmedia;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import in.brainboxmedia.data.EventsParties;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */

public class EventsDetailsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnMapReadyCallback {

    private ImageView backDropEventImage;
    private TextView eventName, eventTiming, eventDate, eventPlace, eventDetails;
    private Button buttonBooking;
    private String key;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String name;
    private double latitude, longitude;
    private FrameLayout progress;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Toolbar toolbar = findViewById(R.id.event_detail_toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        initCollapsingToolbar();

        //progress dialog
        progress = (FrameLayout) findViewById(R.id.progressBar6);
        progress.setVisibility(View.VISIBLE);

        backDropEventImage = findViewById(R.id.backdrop_event_image);
        eventName = findViewById(R.id.event_name);
        eventTiming = findViewById(R.id.event_timing);
        eventDate = findViewById(R.id.event_date);
        eventPlace = findViewById(R.id.event_place);
        eventDetails = findViewById(R.id.event_details);
        buttonBooking = findViewById(R.id.button_book);

        Bundle extras = getIntent().getExtras();
        key = extras.getString("eventId");
        mSwipeRefreshLayout = findViewById(R.id.refresh_event_detail);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        //swipe refresh
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);
                // Fetching data from cloud
                loadRecyclerViewData();

            }
        });

        buttonBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EventsDetailsActivity.this, BookingActivity.class);
                Bundle extras = new Bundle();
                extras.putString("eventId", key);
                intent.putExtras(extras);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        //google maps


    }

    void GetEventDataFirebase() {
        if (key != null) {
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("events")
                    .child(key);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    EventsParties data = dataSnapshot.getValue(EventsParties.class);

                    Glide.with(getBaseContext()).load(data.getParty_poster())
                            .thumbnail(0.6f).into(backDropEventImage);
                    if (!data.getAvailable().equals("true")) {
                        buttonBooking.setVisibility(View.GONE);
                    } else {
                        buttonBooking.setVisibility(View.VISIBLE);
                    }
                    name = data.getName();
                    String lat = data.getLatitude();
                    String longe = data.getLongitude();
                    latitude = Double.parseDouble(data.getLatitude());
                    longitude = Double.parseDouble(data.getLongitude());
                    eventDate.setText(data.getDate());
                    eventName.setText(name);
                    eventDetails.setText(data.getDesscription());
                    eventPlace.setText(data.getPlace());
                    eventTiming.setText(data.getTime());
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(EventsDetailsActivity.this);
                    progress.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("detail event", "Failed to read app title value.", databaseError.toException());
                }
            });
        }
    }

    private void loadRecyclerViewData() {
        // Showing refresh animation before making realdatabase call
        mSwipeRefreshLayout.setRefreshing(true);
        GetEventDataFirebase();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                findViewById(R.id.event_detail_toolbar_layout);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.event_detail_appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(" ");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("location", latitude + longitude + " ");
        LatLng place = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 17.0f));
        mMap.addMarker(new MarkerOptions().position(place).title(eventName.getText().toString()));
    }

}
