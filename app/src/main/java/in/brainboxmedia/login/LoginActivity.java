package in.brainboxmedia.login;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import in.brainboxmedia.IntroActivity;
import in.brainboxmedia.MainActivity;
import in.brainboxmedia.R;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener {
    private static final int RC_SIGN_IN = 9001;
    private CheckBox checkBox;
    private FirebaseAuth mAuth;
    private ConstraintLayout loginlayout;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progress = new ProgressDialog(this);
        progress.setMessage("signing you in mate :) ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        loginlayout = findViewById(R.id.loginlayout);
        Button google = findViewById(R.id.google_button);
        Button phone = findViewById(R.id.phone_button);
        google.setOnClickListener(this);
        phone.setOnClickListener(this);
        checkBox = findViewById(R.id.pandp);

        findViewById(R.id.linearLayout).setAnimation(
                AnimationUtils.loadAnimation(this, R.anim.down_to_up)
        );
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestScopes(new Scope(Scopes.PROFILE))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInGoogle() {
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updateUI(mAuth.getCurrentUser());
                        } else {
                            Snackbar.make(findViewById(R.id.linearLayout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                progress.show();
                updateUI(mAuth.getCurrentUser());
                try {
                    firebaseAuthWithGoogle(task.getResult(ApiException.class));
                } catch (ApiException e) {
                    updateUI(null);
                }
            } else {
                updateUI(null);
            }
        }

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            progress.dismiss();
            startActivity(new Intent(this, IntroActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        } else {
            Snackbar.make(loginlayout, "trying...........", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (checkBox.isChecked()) {
            switch (i) {
                case R.id.google_button:
                    signInGoogle();
                    break;
                case R.id.phone_button:
                    startActivity(new Intent(this, PhoneAuthActivity.class));
                    break;
            }
        } else {
            Snackbar.make(loginlayout, "please accept the privacy policy", Snackbar.LENGTH_SHORT).show();
        }
    }
}
