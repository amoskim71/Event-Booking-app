package in.brainboxmedia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import in.brainboxmedia.adpaters.GalleryAdapter;
import in.brainboxmedia.data.Gallery;
import in.brainboxmedia.fragment.SlideshowDialogFragment;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */

public class GalleryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = "gallery activity";
    private ArrayList<Gallery> images;
    private GalleryAdapter mAdapter;
    private ProgressDialog progress;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        progress = new ProgressDialog(this);
        progress.setMessage("fetching the data mate :) ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
        Toolbar toolbar = findViewById(R.id.toolbar_gallery);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_black));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GalleryActivity.this, CategorizedGalleryActivity.class));
                finish();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.gallery_recycler_view);

        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(this, images);


        // SwipeRefreshLayout
        mSwipeRefreshLayout = findViewById(R.id.refresh_gallery);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);
                // Fetching data from cloud
                loadRecyclerViewData();

            }
        });

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void loadRecyclerViewData() {
        // Showing refresh animation before making realdatabase call
        mSwipeRefreshLayout.setRefreshing(true);
        GetDataFirebase();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        clear();
        loadRecyclerViewData();
    }

    void GetDataFirebase() {

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("gallery")
                .child(getIntent().getStringExtra("keyGallery"))
                .orderByChild("timestamp");
        query.keepSynced(true);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                progress.show();
                Gallery gallery = dataSnapshot.getValue(Gallery.class);
                Log.v("Database query", "onChildAdded:" + dataSnapshot.getKey());

                images.add(gallery);
                mAdapter.notifyDataSetChanged();
                progress.dismiss();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                onRefresh();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                onRefresh();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                onRefresh();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read app title value.", databaseError.toException());
            }

        });
    }

    public void clear() {
        images.clear();
    }
}