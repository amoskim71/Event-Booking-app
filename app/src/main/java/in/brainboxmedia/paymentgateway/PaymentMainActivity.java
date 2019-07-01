package in.brainboxmedia.paymentgateway;


/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.brainboxmedia.MainActivity;
import in.brainboxmedia.ProfileCompeletion;
import in.brainboxmedia.R;
import in.brainboxmedia.TicketGeneratorActivity;
import in.brainboxmedia.data.EventsParties;
import in.brainboxmedia.data.UserProfile;


public class PaymentMainActivity extends BaseActivity {

    public static final String TAG = "payment activity : ";
    public BaseApplication baseApplication;
    private CheckBox checkbox;
    private ScrollView scrollView;
    private SharedPreferences.Editor editor;
    private SharedPreferences settings;
    private String userMobile, userEmail, userName;
    private EditText email_et, mobile_et, name_et;
    private TextInputLayout email_til, mobile_til, name_til;
    private TextView details, priceText, venue, time, date, eventName, terms, profileStatus;
    private AppCompatRadioButton radio_btn_default;
    private AppPreference mAppPreference;
    private ImageView eventPoster;
    private String userId, key, urlTermsAndc, detail, txnid;
    private Button payNowButton;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    private ProgressDialog progress;
    private Double d1;
    private DatabaseReference mFirebaseDatabase;
    private FrameLayout prog;

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte[] messageDigest = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }

    public static void setErrorInputLayout(EditText editText, String msg, TextInputLayout textInputLayout) {
        textInputLayout.setError(msg);
        editText.requestFocus();
    }

    public static boolean isValidEmail(String strEmail) {
        return strEmail != null && android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches();
    }

    public static boolean isValidPhone(String phone) {
        Pattern pattern = Pattern.compile(AppPreference.PHONE_PATTERN);

        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppPreference = new AppPreference();

        baseApplication = new BaseApplication();
        progress = new ProgressDialog(this);
        progress.setMessage("fetching the data mate :) ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        prog = findViewById(R.id.progressBar3);
        prog.setVisibility(View.GONE);
        getTermsAndServices();
        profileStatus = (TextView) findViewById(R.id.profile_status);
        profileStatus.setText("Please enter the details to be on your exclusive ticket");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        scrollView = findViewById(R.id.scrollView);
        checkbox = findViewById(R.id.checkBox);
        this.settings = getSharedPreferences("settings", 0);
        terms = findViewById(R.id.terms_and_conditions);
        details = findViewById(R.id.ticket_details_payment);
        payNowButton = findViewById(R.id.pay_now_button);
        priceText = findViewById(R.id.total_price);
        priceText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7, 2)});
        eventName = findViewById(R.id.event_name_payment);
        venue = findViewById(R.id.venue);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        eventPoster = findViewById(R.id.event_poster_payment);
        email_et = findViewById(R.id.email_et);
        mobile_et = findViewById(R.id.mobile_et);
        mobile_et.setKeyListener(null);
        name_et = findViewById(R.id.name_et);
        Bundle extras = getIntent().getExtras();
        key = extras.getString("key");
        detail = extras.getString("numberHeaderandprice");
        details.setText(detail);
        priceText.setText(extras.getString("price"));

        GetAndSetInformationFirebase();
        GetEventDetailsFirebase();

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(urlTermsAndc)));

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(PaymentMainActivity.this, "There are no browser clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prog.setVisibility(View.VISIBLE);
                String price = priceText.getText().toString().replaceAll("Rs ", "").trim();
                if (price.equals("free")) {
                    String t = System.currentTimeMillis() + "";
                    Intent intent = new Intent(PaymentMainActivity.this, TicketGeneratorActivity.class);
                    HashMap<String, String> data = new HashMap<String, String>();
                    data.put("txnId", t);
                    data.put("name", name_et.getText().toString());
                    data.put("nameEvent", eventName.getText().toString());
                    data.put("date", date.getText().toString());
                    data.put("time", time.getText().toString());
                    data.put("venue", venue.getText().toString());
                    data.put("details", details.getText().toString());
                    data.put("merchantData", "free tickets");
                    intent.putExtra("data", data);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                } else {
                    if (checkbox.isChecked()) {
                        userEmail = email_et.getText().toString().trim();
                        userName = name_et.getText().toString().trim();
                        userMobile = mobile_et.getText().toString().trim();
                        if (validateDetails(userEmail, userMobile)) {

                            payNowButton.setEnabled(false);
                            launchPayUMoneyFlow();
                        }
                    } else {
                        Snackbar.make(scrollView, "Please accept the terms and condition", Snackbar.LENGTH_SHORT).show();
                    }
                    prog.setVisibility(View.GONE);
                }
            }
        });


    }

    private void launchPayUMoneyFlow() {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
        payUmoneyConfig.setDoneButtonText("Get your Ticket");

        //Use this to set your custom title for the activity
        payUmoneyConfig.setPayUmoneyActivityTitle("Secure Payment Portal");

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        baseApplication.setAppEnvironment(AppEnvironment.PRODUCTION);
        double amount = 0.0D;
        try {
            //this.priceText.getText().toString().replaceAll("Rs ", "").trim()
            amount = Double.parseDouble(this.priceText.getText().toString().replaceAll("Rs ", "").trim());
            this.d1 = amount;
            AppEnvironment appEnvironment = baseApplication.getAppEnvironment();
            this.txnid = System.currentTimeMillis() + "";
            builder.setAmount(amount)
                    .setTxnId(txnid)
                    .setPhone(this.userMobile.trim())
                    .setProductName(this.key)
                    .setFirstName(this.userName.trim())
                    .setEmail(this.userEmail.trim())
                    .setsUrl(appEnvironment.surl())
                    .setfUrl(appEnvironment.furl())
                    .setUdf1("")
                    .setUdf2("")
                    .setUdf3("")
                    .setUdf4("")
                    .setUdf5("")
                    .setIsDebug(appEnvironment.debug())
                    .setKey(appEnvironment.merchant_Key())
                    .setMerchantId(appEnvironment.merchant_ID());
            mPaymentParams = builder.build();
            generateHashFromServer(mPaymentParams);
        } catch (Exception e) {
            for (; ; ) {
                try {
                    String str1;
                    String str2;
                    String str3;
                    String str4;
                    String str5;
                    AppEnvironment localAppEnvironment;
                    this.mPaymentParams = builder.build();
                    generateHashFromServer(this.mPaymentParams);
                    return;
                } catch (Exception localException2) {
                    Toast.makeText(this, localException2.getMessage(), Toast.LENGTH_LONG).show();
                    this.payNowButton.setEnabled(true);

                }
                e.printStackTrace();
            }
        }

    }

    public boolean validateDetails(String email, String mobile) {
        email = email.trim();
        mobile = mobile.trim();

        if (TextUtils.isEmpty(mobile)) {
            setErrorInputLayout(mobile_et, getString(R.string.err_phone_empty), mobile_til);
            return false;
        } else if (!isValidPhone(mobile)) {
            setErrorInputLayout(mobile_et, getString(R.string.err_phone_not_valid), mobile_til);
            return false;
        } else if (TextUtils.isEmpty(email)) {
            setErrorInputLayout(email_et, getString(R.string.err_email_empty), email_til);
            return false;
        } else if (!isValidEmail(email)) {
            setErrorInputLayout(email_et, getString(R.string.email_not_valid), email_til);
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_payment;
    }

    void GetEventDetailsFirebase() {
        if (key != null) {
            progress.show();
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("events")
                    .child(key);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    EventsParties data = dataSnapshot.getValue(EventsParties.class);
                    progress.dismiss();
                    Glide.with(PaymentMainActivity.this).load(data.getParty_poster())
                            .thumbnail(0.6f).into(eventPoster);
                    date.setText(data.getDate());
                    eventName.setText(data.getName());
                    venue.setText(data.getPlace());
                    time.setText(data.getTime());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("detail event", "Failed to read app title value.", databaseError.toException());
                }
            });
        }
    }

    void GetAndSetInformationFirebase() {
        if (userId.equals("")) {
            startActivity(new Intent(PaymentMainActivity.this, MainActivity.class));
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

                        UserProfile user = dataSnapshot.getValue(UserProfile.class);
                        if (user == null) {
                            profileStatus.setText("click me to setup your profile");
                            email_et.setVisibility(View.GONE);
                            name_et.setVisibility(View.GONE);
                            mobile_et.setVisibility(View.GONE);
                            payNowButton.setEnabled(false);
                            profileStatus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(PaymentMainActivity.this, ProfileCompeletion.class));
                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                }
                            });
                        } else {
                            setupprofile(user);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("detail event", "Failed to read app title value.", databaseError.toException());
                    }
                });
            }
        }
    }

    public void setupprofile(UserProfile user) {
        if (user == null) {
            email_et.setText(" ");
            mobile_et.setText(" ");
            name_et.setText(" ");
        } else {
            email_et.setText(user.getEmail());
            mobile_et.setText(user.getPhone());
            name_et.setText(String.format("%s %s", user.getFname(), user.getLname()));
        }

    }

    void getTermsAndServices() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("documents")
                .child("terms_and_condition");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                urlTermsAndc = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("detail event", "Failed to read app title value.", databaseError.toException());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        payNowButton.setEnabled(true);
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        Log.d("MainActivity", "request code " + paramInt1 + " resultcode " + paramInt2);
        ResultModel localResultModel;
        if ((paramInt1 == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT) && (paramInt2 == -1) && (paramIntent != null)) {
            TransactionResponse localTransactionResponse = paramIntent.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);
            localResultModel = paramIntent.getParcelableExtra("result");
            if ((localTransactionResponse != null) && (localTransactionResponse.getPayuResponse() != null)) {
                if (localTransactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {

                    String payuResponse = localTransactionResponse.getPayuResponse();
                    String merchantResponse = localTransactionResponse.getTransactionDetails();

                    String txnId = this.txnid, amount = String.valueOf(this.d1), key = this.key;
                    Toast.makeText(PaymentMainActivity.this, "successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PaymentMainActivity.this, TicketGeneratorActivity.class);
                    HashMap<String, String> data = new HashMap<String, String>();
                    data.put("txnId", txnId);
                    data.put("name", name_et.getText().toString());
                    data.put("nameEvent", eventName.getText().toString());
                    data.put("date", date.getText().toString());
                    data.put("time", time.getText().toString());
                    data.put("venue", venue.getText().toString());
                    data.put("details", details.getText().toString());
                    data.put("merchantData", merchantResponse);
                    data.put("eventId", key);
                    data.put("amount", amount);
                    intent.putExtra("data", data);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();
                } else {
                    Toast.makeText(PaymentMainActivity.this, "unsuccessful transaction try again", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PaymentMainActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }

            }
        } else {
            return;
        }
        if ((localResultModel != null) && (localResultModel.getError() != null)) {
            Log.d("MainActivity : ", "Error response : " + localResultModel.getError().getTransactionResponse());
            return;
        }
        Log.d("MainActivity : ", "Both objects are null!");
    }

    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get("key") + "|");
        stringBuilder.append(params.get("tnxid") + "|");
        stringBuilder.append(params.get("amount") + "|");
        stringBuilder.append(params.get("productInfo") + "|");
        stringBuilder.append(params.get("firstName") + "|");
        stringBuilder.append(params.get("email") + "|");
        stringBuilder.append(params.get("udf1") + "|");
        stringBuilder.append(params.get("udf2") + "|");
        stringBuilder.append(params.get("udf3") + "|");
        stringBuilder.append(params.get("udf4") + "|");
        stringBuilder.append(params.get("udf5") + "|");
        stringBuilder.append(((BaseApplication) getApplication()).getAppEnvironment().salt());
        paymentParam.setMerchantHash(hashCal(stringBuilder.toString()));
        return paymentParam;
    }

    /**
     * This method generates hash from server.
     *
     * @param paymentParam payments params used for hash generation
     */
    public void generateHashFromServer(PayUmoneySdkInitializer.PaymentParam paymentParam) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        HashMap<String, String> params = paymentParam.getParams();

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams("key", params.get("key")));
        postParamsBuffer.append(concatParams("amount", params.get("amount")));
        postParamsBuffer.append(concatParams("txnid", params.get("txnid")));
        postParamsBuffer.append(concatParams("email", params.get("email")));
        postParamsBuffer.append(concatParams("productinfo", params.get("productInfo")));
        postParamsBuffer.append(concatParams("firstname", params.get("firstName")));
        postParamsBuffer.append(concatParams("udf1", params.get("udf1")));
        postParamsBuffer.append(concatParams("udf2", params.get("udf2")));
        postParamsBuffer.append(concatParams("udf3", params.get("udf3")));
        postParamsBuffer.append(concatParams("udf4", params.get("udf4")));
        postParamsBuffer.append(concatParams("udf5", params.get("udf5")));

        if (postParamsBuffer.charAt(-1 + postParamsBuffer.length()) == '&') {
        }
        for (String str = postParamsBuffer.substring(0, -1 + postParamsBuffer.length()).toString(); ; str = postParamsBuffer.toString()) {
            new GetHashesFromServerTask().execute(str);
            return;
        }
    }


    protected String concatParams(String paramString1, String paramString2) {
        return paramString1 + "=" + paramString2 + "&";
    }

    private class GetHashesFromServerTask extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PaymentMainActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... postParams) {

            String merchantHash = "";
            try {
                URL url = new URL("http://brainboxmedia.in/BrainBoxApp/gethashval.php");
                byte[] postParamsByte = postParams[0].getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        case "payment_hash":
                            merchantHash = response.getString(key);
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return merchantHash;
        }

        @Override
        protected void onPostExecute(String merchantHash) {
            super.onPostExecute(merchantHash);

            progressDialog.dismiss();
            payNowButton.setEnabled(true);

            if (merchantHash.isEmpty() || merchantHash.equals("")) {
                Toast.makeText(PaymentMainActivity.this, "Could not generate hash", Toast.LENGTH_SHORT).show();
            } else {
                mPaymentParams.setMerchantHash(merchantHash);

                if (AppPreference.selectedTheme != -1) {
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PaymentMainActivity.this, AppPreference.selectedTheme, mAppPreference.isOverrideResultScreen());
                } else {
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PaymentMainActivity.this, R.style.AppTheme_pink, mAppPreference.isOverrideResultScreen());
                }
            }
        }
    }

    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }

    }
}