package e_business_projekt.e_business_projekt;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import e_business_projekt.e_business_projekt.route_list.POIRouteProvider;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "EBP.LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "LOGIN: onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        POIRouteProvider.getInstance().resetInstance();


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //handling sign in button
        SignInButton signInButton = (SignInButton) findViewById(R.id.button_sign_in);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        //handling log out button
        Button logOutButton = (Button) findViewById(R.id.button_logout);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        //handling sign in button
        Button guestSignInButton = (Button) findViewById(R.id.button_guest_sign_in);
        guestSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guestSignIn();
            }
        });

    }

    private void signIn(){
        Log.i(TAG, "Sign In Button clicked");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void guestSignIn(){
        Log.i(TAG, "Guest Sign In Button clicked");
        String guestID = "Guest1234";
        String guestName = "Guest";

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userID", guestID);
        intent.putExtra("userName", guestName);

        //set user id to route manager
        POIRouteProvider.getInstance().setUserID(guestID);
        POIRouteProvider.getInstance().setUserName(guestName);
        Toast.makeText(getBaseContext(), "Signed in as Guest", Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.i(TAG, "Sign Out Successful");
                        Toast.makeText(getBaseContext(), "Sign out successful", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result){
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.d(TAG, "DisplayName:" + acct.getDisplayName());
            Log.d(TAG, "ID:" + acct.getId());
            Log.d(TAG, "Email:" + acct.getEmail());

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("userID", acct.getId());
            intent.putExtra("userName", acct.getDisplayName());

            //set user id to route manager
            POIRouteProvider.getInstance().setUserID(acct.getId());
            POIRouteProvider.getInstance().setUserName(acct.getDisplayName());
            startActivity(intent);

            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            Log.d(TAG, "FAIL:" + result.isSuccess());
            //updateUI(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
