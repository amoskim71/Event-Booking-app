package in.brainboxmedia.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import in.brainboxmedia.ProfileActivity;
import in.brainboxmedia.R;
import in.brainboxmedia.data.Attendees;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */

public class TicketHistoryFragment extends DialogFragment {
    private String TAG = TicketHistoryFragment.class.getSimpleName();
    private Attendees images;
    private String key, userId;
    private int selectedPosition = 0;
    private TextView eventName, nameOnTicket, phoneOnTicket, venue, time, date, details;
    private ImageView qrCodeImage, back;

    public static TicketHistoryFragment newInstance() {
        TicketHistoryFragment f = new TicketHistoryFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_ticket, container, false);
        qrCodeImage = v.findViewById(R.id.qr_code);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        eventName = v.findViewById(R.id.event_name_ticket);
        nameOnTicket = v.findViewById(R.id.name_ticket);
        phoneOnTicket = v.findViewById(R.id.phone_number_ticket);
        venue = v.findViewById(R.id.venue_ticket);
        time = v.findViewById(R.id.time_ticket);
        details = v.findViewById(R.id.ticket_details_ticket);
        date = v.findViewById(R.id.date_ticket);
        back = v.findViewById(R.id.back_button_ticket);

        images = (Attendees) getArguments().getSerializable("images");
        key = getArguments().getString("key");

        getQRCode();
        GetUserDataFirebase();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getBaseContext(), ProfileActivity.class));
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        return v;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    public void getQRCode() {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(key, BarcodeFormat.QR_CODE, 150, 150);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    void GetUserDataFirebase() {

        if (key != null) {
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("attendees")
                    .child(userId)
                    .child(key);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Attendees data = dataSnapshot.getValue(Attendees.class);
                    nameOnTicket.setText(data.getNames());
                    eventName.setText(data.getEventName());
                    date.setText(data.getDates());
                    time.setText(data.getTimes());
                    venue.setText(data.getVenues());
                    details.setText(data.getDetail());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("detail event", "Failed to read app title value.", databaseError.toException());
                }
            });
        }
    }
}
