package in.brainboxmedia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import in.brainboxmedia.data.UserProfile;

import static in.brainboxmedia.paymentgateway.PaymentMainActivity.isValidEmail;
import static in.brainboxmedia.paymentgateway.PaymentMainActivity.isValidPhone;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */
public class ProfileCompeletion extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText inputFName, inputEmail, inputLName, inputPhone;
    private Button nextAndSave, skip;
    private DatabaseReference mFirebaseDatabase;
    private RadioGroup radioSexGroup;
    private TextInputLayout name_til, email_til, mobile_til, lname_til;
    private RadioButton radioSexButton;
    private String userId;
    private ProgressDialog progress;
    private RelativeLayout r;

    public static void setErrorInputLayout(EditText editText, String msg, TextInputLayout textInputLayout) {
        textInputLayout.setError(msg);
        editText.requestFocus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_compeletion);
        //progress dialog
        progress = new ProgressDialog(this);
        progress.setMessage("thank you your profile is safe with us :) ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        r = findViewById(R.id.rl);
        skip = findViewById(R.id.btn_skip_profile);
        name_til = findViewById(R.id.first_name_profile_til);
        email_til = findViewById(R.id.email_profile_til);
        mobile_til = findViewById(R.id.mobile_profile_til);
        lname_til = findViewById(R.id.last_name_profile_til);
        inputFName = findViewById(R.id.first_name_profile_et);
        inputEmail = findViewById(R.id.email_profile_et);
        inputLName = findViewById(R.id.last_name_profile_et);
        inputPhone = findViewById(R.id.mobile_profile_et);
        radioSexGroup = findViewById(R.id.gender);

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("UserProfile");

        nextAndSave = findViewById(R.id.btn_next_profile);

        nextAndSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputFName.getText().toString();
                String email = inputEmail.getText().toString();
                String lname = inputLName.getText().toString();
                String phone = inputPhone.getText().toString();
                if (validateDetails(email, phone, name, lname, radioSexGroup.getCheckedRadioButtonId())) {
                    int selectedId = radioSexGroup.getCheckedRadioButtonId();
                    radioSexButton = findViewById(selectedId);
                    String gender = radioSexButton.getText().toString();

                    if (TextUtils.isEmpty(userId)) {
                        createUser(name, lname, email, phone, gender);
                        nextLayout();
                    } else {
                        updateUser(name, lname, email, phone, gender);
                        nextLayout();
                    }
                } else {
                    Snackbar.make(r, "please fill all the details ", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextLayout();
            }
        });
    }

    public boolean validateDetails(String email, String mobile, String name, String lName, int radioButton) {
        email = email.trim();
        mobile = mobile.trim();

        if (TextUtils.isEmpty(mobile)) {
            setErrorInputLayout(inputPhone, getString(R.string.err_phone_empty), mobile_til);
            return false;
        } else if (!isValidPhone(mobile)) {
            setErrorInputLayout(inputPhone, getString(R.string.err_phone_not_valid), mobile_til);
            return false;
        } else if (TextUtils.isEmpty(email)) {
            setErrorInputLayout(inputEmail, getString(R.string.err_email_empty), email_til);
            return false;
        } else if (!isValidEmail(email)) {
            setErrorInputLayout(inputEmail, getString(R.string.email_not_valid), email_til);
            return false;
        } else if (TextUtils.isEmpty(name)) {
            setErrorInputLayout(inputFName, "First name is empty", name_til);
            return false;
        } else if (TextUtils.isEmpty(lName)) {
            setErrorInputLayout(inputLName, "Last name is empty", lname_til);
            return false;
        } else return radioButton != -1;
    }

    public void nextLayout() {
        startActivity(new Intent(ProfileCompeletion.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    }

    private void createUser(String fname, String lname, String email, String phone, String gender) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfile userProfile = new UserProfile(fname, lname, email, phone, gender);
        if (user != null) {
            userId = user.getUid();
        } else {
            userId = userProfile.getEmail();
        }
        mFirebaseDatabase.child(userId).setValue(userProfile);
    }

    private void updateUser(String fname, String lname, String email, String phone, String gender) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(fname))
            mFirebaseDatabase.child(userId).child("fname").setValue(fname);

        if (!TextUtils.isEmpty(email))
            mFirebaseDatabase.child(userId).child("email").setValue(email);
    }

}