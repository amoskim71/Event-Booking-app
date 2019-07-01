package in.brainboxmedia;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import in.brainboxmedia.adpaters.EventsPartiesAdapter;
import in.brainboxmedia.data.EventsParties;
import in.brainboxmedia.helper.GridSpacingItemDecoration;
import in.brainboxmedia.interfaces.ClickListener;
import in.brainboxmedia.listners.RecyclerTouchListener;


/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */
public class EventsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "events database query";
    private EventsPartiesAdapter adapter;
    private List<EventsParties> eventPartiesList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ShimmerFrameLayout mShimmerViewContainer;

    private List<String> keys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initCollapsingToolbar();
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        keys = new ArrayList<>();
        eventPartiesList = new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_black));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventsActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }
        });

        adapter = new EventsPartiesAdapter(this, eventPartiesList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        // SwipeRefreshLayout
        mSwipeRefreshLayout = findViewById(R.id.refresh_event_list);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                // Fetching data from cloud
                loadRecyclerViewData();

            }
        });


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.v("key : ", keys.get(position));

                Intent intent = new Intent(EventsActivity.this, EventsDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("eventId", keys.get(position));
                intent.putExtras(extras);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
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

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
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
                    collapsingToolbar.setTitle(getString(R.string.event));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few EventsParties for testing
     */
    void GetDataFirebase() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("events")
                .orderByChild("date");
        query.keepSynced(true);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("Database query", "onChildAdded:" + dataSnapshot.getKey());
                keys.add(dataSnapshot.getKey());
                EventsParties data = dataSnapshot.getValue(EventsParties.class);
                eventPartiesList.add(data);
                adapter.notifyDataSetChanged();
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
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

    @Override
    public void onRefresh() {
        clear();
        loadRecyclerViewData();
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void clear() {
        eventPartiesList.clear();
    }

    // Add a list of items -- change to type used
    public void addAll(List<EventsParties> list) {
        eventPartiesList.addAll(list);
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }
}