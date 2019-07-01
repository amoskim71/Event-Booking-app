package in.brainboxmedia;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;

import in.brainboxmedia.data.Attendees;
import in.brainboxmedia.data.UserProfile;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */
public class TicketGeneratorActivity extends AppCompatActivity {

    private String txnId, userId, name, eventNameString, dateString, timeString, venueString, detailsString, merchantData, eventId, amount;
    private ImageView qrCodeImage, back;
    private TextView eventName, nameOnTicket, phoneOnTicket, venue, time, date, details;
    private ProgressDialog prog;
    private DatabaseReference mFirebaseDatabase;
    private NestedScrollView n;
    private Attendees attendees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        //progress dialog
        prog = new ProgressDialog(this);
        n = (NestedScrollView) findViewById(R.id.scroll);
        prog.setMessage("fetching the data mate :) ");
        prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prog.setIndeterminate(true);

        qrCodeImage = findViewById(R.id.qr_code);
        eventName = findViewById(R.id.event_name_ticket);
        nameOnTicket = findViewById(R.id.name_ticket);
        phoneOnTicket = findViewById(R.id.phone_number_ticket);
        venue = findViewById(R.id.venue_ticket);
        time = findViewById(R.id.time_ticket);
        details = findViewById(R.id.ticket_details_ticket);
        date = findViewById(R.id.date_ticket);
        back = findViewById(R.id.back_button_ticket);
        Intent intent = getIntent();
        HashMap<String, String> hashMap = (HashMap<String, String>) intent.getSerializableExtra("data");
        setTheData(hashMap);
        attendees = new Attendees(amount, eventId, detailsString, merchantData, name, eventNameString, dateString, timeString, venueString);
        try {
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog();
            }
        });


        getQRCode();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UploadDataTo upload = new UploadDataTo();
        upload.execute();

    }

    private void alertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TicketGeneratorActivity.this);
        alertDialog.setMessage("Did you take the screenshot of the ticket?");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // Write your code here to invoke YES event
                Toast.makeText(getApplicationContext(), "Cool see you at the event", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(TicketGeneratorActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "please take a screenshot", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void setTheData(HashMap<String, String> data) {
        prog.show();

        merchantData = data.get("merchantData");
        eventId = data.get("eventId");
        amount = data.get("amount");

        txnId = data.get("txnId");
        name = data.get("name");
        eventNameString = data.get("nameEvent");
        dateString = data.get("date");
        timeString = data.get("time");
        venueString = data.get("venue");
        detailsString = data.get("details");


        nameOnTicket.setText(name);
        eventName.setText(eventNameString);
        date.setText(dateString);
        time.setText(timeString);
        venue.setText(venueString);
        details.setText(detailsString);
        prog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void getQRCode() {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(txnId, BarcodeFormat.QR_CODE, 150, 150);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private class UploadDataTo extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            userId = user.getUid();
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("UserProfile")
                    .child(userId);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserProfile data = dataSnapshot.getValue(UserProfile.class);
                    nameOnTicket.setText(data.getFname() + data.getLname());
                    phoneOnTicket.setText(data.getPhone());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("detail event", "Failed to read app title value.", databaseError.toException());
                }
            });
            mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("attendees");
            if (txnId != null) {
                mFirebaseDatabase.child(userId).child(txnId).setValue(attendees);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void merchantHash) {
            super.onPostExecute(merchantHash);
        }
    }

}
