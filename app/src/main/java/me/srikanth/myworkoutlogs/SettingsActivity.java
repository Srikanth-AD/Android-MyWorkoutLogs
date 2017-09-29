package me.srikanth.myworkoutlogs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends BaseActivity {

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState, R.layout.activity_settings);
        final Button signOutButton = findViewById(R.id.sign_out_button);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            setTitle(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this , new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            Log.i("onConnectionFailed", connectionResult.toString());
                        }
                    } /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();


            signOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    signOutButton.setVisibility(View.GONE);
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);

                                }
                            });
                }
            });
        } else {
            Log.w("Settings", "Unable to get current user");
            setTitle("Settings");
            signOutButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        Log.i("settings", "back press");
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
