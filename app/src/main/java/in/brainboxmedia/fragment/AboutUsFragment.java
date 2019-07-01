package in.brainboxmedia.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;

import in.brainboxmedia.R;
import in.brainboxmedia.data.AboutUs;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */

public class AboutUsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AboutUsFragment";
    private ImageView cover, facebookButton, googleButton, twitterButton;
    private TextView email, phone, website, aboutUs;
    private AboutUs data;
    private String fbLink, googleLink, twitterLink;
    private ScrollView scrollView;
    private ProgressBar progressBar;


    public AboutUsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        //progress dialog
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_cyclic);
        progressBar.setVisibility(View.INVISIBLE);
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view_about);
        //initializing the views
        aboutUs = view.findViewById(R.id.about_us);
        cover = view.findViewById(R.id.header_cover_image);
        facebookButton = view.findViewById(R.id.fb_link);
        googleButton = view.findViewById(R.id.google_link);
        twitterButton = view.findViewById(R.id.twitter_link);
        email = view.findViewById(R.id.email_brain_box);
        phone = view.findViewById(R.id.phone_brain_box);
        website = view.findViewById(R.id.website_brain_box);

        //setting on click listeners
        facebookButton.setOnClickListener(this);
        googleButton.setOnClickListener(this);
        twitterButton.setOnClickListener(this);
        email.setOnClickListener(this);
        phone.setOnClickListener(this);
        website.setOnClickListener(this);
        MyTask2 m = new MyTask2();
        m.execute();
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fb_link:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(fbLink)));

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no browser clients installed.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.google_link:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(googleLink)));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no browser clients installed.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.twitter_link:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(twitterLink)));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no browser clients installed.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.email_brain_box:
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { email.getText().toString() });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Enquiry about BrainBox Media events");
                try {
                    startActivity(Intent.createChooser(intent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.phone_brain_box:
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:0123456789"));
                startActivity(i);
                break;
            case R.id.website_brain_box:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(website.getText().toString())));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no browser clients installed.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public class MyTask2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            scrollView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        @Keep
        protected Void doInBackground(Void... params) {
            Query about = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("about");
            about.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    data = dataSnapshot.getValue(AboutUs.class);

                    aboutUs.setText(data.getAbout_us());
                    Glide.with(getContext()).load(data.getCover())
                            .thumbnail(0.6f).into(cover);

                    email.setText(data.getEmail());
                    phone.setText(data.getPhone());
                    website.setText(data.getWebsite_link());
                    fbLink = data.getFacebook();
                    googleLink = data.getGoogle();
                    twitterLink = data.getTwitter();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getActivity(), "Sry not able to connect with the database.", Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void merchantHash) {
            super.onPostExecute(merchantHash);
            scrollView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
