package in.brainboxmedia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import in.brainboxmedia.adpaters.HistoryAdapter;
import in.brainboxmedia.data.Attendees;
import in.brainboxmedia.data.UserProfile;
import in.brainboxmedia.fragment.TicketHistoryFragment;
import in.brainboxmedia.interfaces.ClickListener;
import in.brainboxmedia.listners.RecyclerTouchListener;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */
public class ProfileActivity extends AppCompatActivity {

    DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("UserProfile");
    private ImageView editPhone, editEmail, donePhone, doneEmail;
    private TextView name, profileImage, clickHere;
    private EditText phone, email;
    private HistoryAdapter adapter;
    private ArrayList<Attendees> historyList;
    private List<String> keys;
    private String userId;
    private ProgressDialog progress;
    private KeyListener listener, listener1;
    private ConstraintLayout displayLayout;
    private Button setProfile;
    private RecyclerView recyclerView;
    private ImageView back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        progress = new ProgressDialog(this);
        progress.setMessage("fetching the data mate :) ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        keys = new ArrayList<String>();
        setProfile = (Button) findViewById(R.id.complete);
        displayLayout = (ConstraintLayout) findViewById(R.id.display_layout);
        recyclerView = findViewById(R.id.history_booking);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        Log.v("id", userId);
        back = findViewById(R.id.back_button_ticket);

        profileImage = findViewById(R.id.user_profile_photo);
        editPhone = findViewById(R.id.editp);
        editEmail = findViewById(R.id.edit);
        donePhone = findViewById(R.id.done_phone);
        doneEmail = findViewById(R.id.done_email);
        clickHere = (TextView) findViewById(R.id.click_here);
        name = findViewById(R.id.user_profile_name);
        phone = findViewById(R.id.mobile_profile_et);
        email = findViewById(R.id.email_profile_et);

        historyList = new ArrayList<Attendees>();

        listener = phone.getKeyListener();
        listener1 = email.getKeyListener();

        email.setKeyListener(null);
        phone.setKeyListener(null);
        GetUserDataFirebase();

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneEmail.setVisibility(View.VISIBLE);
                editEmail.setVisibility(View.GONE);
                email.setEnabled(true);
                email.setKeyListener(listener1);
            }
        });
        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donePhone.setVisibility(View.VISIBLE);
                editPhone.setVisibility(View.GONE);
                phone.setEnabled(true);
                phone.setKeyListener(listener);
            }
        });
        doneEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                mFirebaseDatabase.child(userId).child("email").setValue(email.getText().toString());
                progress.dismiss();
                doneEmail.setVisibility(View.GONE);
                editEmail.setVisibility(View.VISIBLE);
                email.setKeyListener(null);
            }
        });
        donePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                mFirebaseDatabase.child(userId).child("phone").setValue(phone.getText().toString());
                progress.dismiss();
                donePhone.setVisibility(View.GONE);
                editPhone.setVisibility(View.VISIBLE);
                phone.setKeyListener(null);
            }
        });

        getTicketdata();
        adapter = new HistoryAdapter(ProfileActivity.this, historyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        clickHere.setVisibility(View.GONE);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.v("key : ", keys.get(position));

                Bundle bundle = new Bundle();
                bundle.putSerializable("images", historyList.get(position));
                bundle.putString("key", keys.get(position));
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                TicketHistoryFragment newFragment = TicketHistoryFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EventsActivity.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        setProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ProfileCompeletion.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
    }

    void GetUserDataFirebase() {
        if (userId.equals("")) {
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            finish();
        } else {
            if (userId != null) {
                Query query = FirebaseDatabase.getInstance()
                        .getReference()
                        .child("UserProfile")
                        .child(userId);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        progress.show();
                        UserProfile user = dataSnapshot.getValue(UserProfile.class);
                        if (user != null) {
                            setupProfile(user);
                            setProfile.setVisibility(View.GONE);
                            setProfile.setEnabled(false);
                        } else {
                            setProfile.setVisibility(View.VISIBLE);
                            setProfile.setEnabled(true);
                            displayLayout.setVisibility(View.GONE);
                        }
                        progress.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("detail event", "Failed to read app title value.", databaseError.toException());
                    }
                });
            }
        }
    }

    public void setupProfile(UserProfile user) {
        List<Integer> color = new ArrayList<Integer>();
        color.add(R.color.bg_screen1);
        color.add(R.color.bg_screen2);
        color.add(R.color.bg_screen3);
        color.add(R.color.bg_screen4);

        Random r = new Random();
        int i = r.nextInt(4);
        phone.setText(user.getPhone());
        String name1 = String.format("%s %s", user.getFname(), user.getLname());
        name.setText(name1);
        email.setText(user.getEmail());
        char firstLetter = (name1.toUpperCase()).charAt(0);
        profileImage.setText(firstLetter + "");
        profileImage.setBackgroundColor(getResources().getColor(color.get(i)));

    }

    public void getTicketdata() {
        if (userId != null) {
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("attendees")
                    .child(userId);
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    progress.show();
                    keys.add(dataSnapshot.getKey());
                    Attendees data = dataSnapshot.getValue(Attendees.class);
                    historyList.add(data);
                    adapter.notifyDataSetChanged();
                    progress.dismiss();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    getTicketdata();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    getTicketdata();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    getTicketdata();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("datatickets", "Failed to read app title value.", databaseError.toException());

                }
            });
        }
    }

}
