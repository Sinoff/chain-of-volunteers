package chainofvolunteers.chainofvolunteers;

/*Android imports*/
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/*Facebook imports*/
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.Callable;

import chainofvolunteers.chainofvolunteers.AppHelpers.Route;


public class LoginActivity extends AppCompatActivity {

    /******************/
    /*      Vars       /
    /******************/
    /*General*/
    private final String        TAG = "COV: Login";
    private String              ownerName;
    private String              ownerEmail;

    /*Facebook*/
    private CallbackManager     facebookCallbackManager;
    private LoginButton         facebookLoginButton;
    private Button              ngoButton;

    /******************/
    /*    Methods      /
    /******************/

    /*****Method: onCreate*****/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //NGO button
        ngoButton = (Button)findViewById(R.id.ngo_button);
        ngoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //move to NGO page.
                moveToNgo();
            }
        });

        /*facebook login*/
        facebookCallbackManager = CallbackManager.Factory.create();
        facebookLoginButton = (LoginButton)findViewById(R.id.facebook_sign_in_button);
        facebookLoginButton.setReadPermissions(Arrays.asList("public_profile","email"));
        facebookLoginButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(null != loginResult.getAccessToken()) {
                    Log.i(TAG, loginResult.getAccessToken().getUserId());
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    try {
                                        ownerName = object.getString("name");
                                        ownerEmail = object.getString("email");
                                        handleSignInResult(new Callable<Void>() {
                                            @Override
                                            public Void call() throws Exception {
                                                LoginManager.getInstance().logOut();
                                                return null;
                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "name,email");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
            }
            @Override
            public void onCancel()
            {
                Log.d(TAG, "facebook:canceled");
                handleSignInResult(null);
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "facebook:error");
                handleSignInResult(null);
            }
        });

        }

    /*****Method: onActivityResult*****/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /*****Method: handleSignInResult*****/
    private void handleSignInResult(Callable<Void> logout) {
        if(logout == null) {
            /* Login error */
            Toast.makeText(getApplicationContext(), R.string.login_error, Toast.LENGTH_SHORT).show();
        } else {
            /* Login success */


//            Intent homePageActivity = new Intent(this, HomePageActivity.class);
//            startActivity(homePageActivity);
//            finish();
        }
    }

    private void moveToNgo()
    {
        Intent submitMissionActivity = new Intent(this, SubmitMissionActivity.class);
        startActivity(submitMissionActivity);
        finish();
    }
}
