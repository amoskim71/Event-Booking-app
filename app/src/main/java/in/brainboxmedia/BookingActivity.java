package in.brainboxmedia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import in.brainboxmedia.adpaters.TicketAdapter;
import in.brainboxmedia.data.TypeTicket;
import in.brainboxmedia.paymentgateway.PaymentMainActivity;
/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */

public class BookingActivity extends AppCompatActivity {

    public static TextView sum;
    public static String details;
    public static TextView total_pricesss;
    private String key;
    private List<TypeTicket> ticketList;
    private TicketAdapter mAdapter;
    private int price = 0;
    private Button proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Bundle extras = getIntent().getExtras();
        key = extras.getString("eventId");

        ticketList = new ArrayList<>();
        proceed = findViewById(R.id.proceed);
        sum = findViewById(R.id.sum);
        sum.setText("Rs 0.0");
        total_pricesss = findViewById(R.id.total_pricesss);
        mAdapter = new TicketAdapter(this, ticketList);

        GetDataFirebase();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_ticket);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((sum.getText()).equals("Rs 0.0")) {
                    Toast.makeText(BookingActivity.this, "please select a ticket", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(BookingActivity.this, PaymentMainActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("price", String.valueOf(sum.getText()));
                    extras.putString("numberHeaderandprice", details);
                    extras.putString("key", key);
                    i.putExtras(extras);
                    startActivity(i);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            }
        });
    }

    void GetDataFirebase() {
        if (key != null) {
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("ticket_types")
                    .child(key)
                    .orderByChild("price");
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.d("Database query", "onChildAdded:" + dataSnapshot.getKey());
                    TypeTicket ticket = dataSnapshot.getValue(TypeTicket.class);
                    ticketList.add(ticket);
                    mAdapter.notifyDataSetChanged();
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
                    Log.e("Booking activity", "Failed to read app title value.", databaseError.toException());
                }
            });
        }
    }

    public void clear() {
        ticketList.clear();
    }

    private void loadRecyclerViewData() {
        GetDataFirebase();
    }

    public void onRefresh() {
        clear();
        loadRecyclerViewData();
    }
}
