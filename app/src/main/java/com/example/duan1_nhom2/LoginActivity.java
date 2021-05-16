package com.example.duan1_nhom2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1_nhom2.DAOClass.DAO_Admin;
import com.example.duan1_nhom2.DAOClass.DAO_NguoiDung;
import com.example.duan1_nhom2.Model.NguoiDung;
import com.example.duan1_nhom2.AdditionalFunctions.AdditionalFunctions;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {
    EditText txtUserEmail, txtPassword;
    TextView txtRegisterNow, txtForgotPassword;
    CheckBox chkRemember;
    Button btnLogin, btnFacebookLogin, btnGoogleLogin;
    CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;
    Bundle bundle = new Bundle();
    private boolean ADMIN_STATUS = false;
    boolean isLoggingIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
        registerCallbackForFacebookLogin();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLoggingIn){
                    isLoggingIn = true;
                    checkAccountVerification();
                }
            }
        });
        btnFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAsFacebook();
            }
        });
        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAsGoogle();
            }
        });
        txtRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, DangKyActivity.class));
            }
        });
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, QuenMatKhauActivity.class));
            }
        });
        getUserAccount();
    }
    private void loginAsGoogle(){
        logOut();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getBaseContext(), gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1);
    }
    private void loginAsFacebook(){
        logOut();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
    }
    private void findView(){
        txtPassword = findViewById(R.id.txtPassword);
        txtUserEmail = findViewById(R.id.txtUserEmail);
        chkRemember = findViewById(R.id.chkRemember);
        btnFacebookLogin = findViewById(R.id.btnFacebookLogin);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegisterNow = findViewById(R.id.txtRegisterNow);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
    }
    private void registerCallbackForFacebookLogin(){
        callbackManager = CallbackManager.Factory.create();
        // If using in a fragment
//        loginButton.setFragment(this);
        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookSignInResult(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("LoginError", error.getMessage());
            }
        });
    }
    private void handleFacebookSignInResult(AccessToken token){
        FirebaseAuth myFirebaseAuth = FirebaseAuth.getInstance();
        GraphRequest graphRequest = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String id = object.getString("id");
                    String email = object.getString("email");
                    String name = object.getString("name");
                    String photoURL = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    bundle.putString("PhotoURL", photoURL);
                    bundle.putString("DisplayName", name);
                    bundle.putString("UserID", id);
                    bundle.putString("Email", email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, email, name, picture.width(800).height(800)");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        myFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    sendFacebookUserProfile();
                }else{
                    Toast.makeText(getBaseContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void sendFacebookUserProfile(){
        String id = bundle.getString("UserID");
        String displayName = bundle.getString("DisplayName");
        String photoURL = bundle.getString("PhotoURL");
        String email = bundle.getString("Email");
        DAO_NguoiDung.loginAsFacebook(id, displayName, email, photoURL, LoginActivity.this);
    }
    private void saveUserAccount(String email, String password){
        //LÆ°u tÃªn tÃ i khoáº£n vÃ  máº­t kháº©u ngÆ°á»i dÃ¹ng
        if (!email.isEmpty()&&!password.isEmpty()){
            SharedPreferences sp = getSharedPreferences("myAccount", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            boolean chk = chkRemember.isChecked();
            if(!chk){
                editor.clear();
            }else{
                editor.putString("email",email);
                editor.putString("password",password);
                editor.putBoolean("savestatus",chk);
            }
            editor.apply();
        }else{
            //ThÃ´ng bÃ¡o khi nháº­p sai
        }

    }
    private void getUserAccount(){
        SharedPreferences pref = getSharedPreferences("myAccount",MODE_PRIVATE);
        boolean chk = pref.getBoolean("savestatus",false);
        if (chk){
            final String username = pref.getString("email","");
            final String password = pref.getString("password","");
            txtUserEmail.setText(username);
            txtPassword.setText(password);
        }
        chkRemember.setChecked(chk);
    }
    private void authenticateGoogleAccount(String idToken, final String displayName){
        final FirebaseAuth myFirebaseAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        myFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = myFirebaseAuth.getCurrentUser();
                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(displayName).build();
                    user.updateProfile(request);
                    sendGoogleUserProfile(user);
                }else{
                    Toast.makeText(getBaseContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void sendGoogleUserProfile(FirebaseUser user){
        if (user!=null){
            DAO_NguoiDung.loginAsGoogle(user, LoginActivity.this);
        }
    }
    private void checkAccountVerification(){
        final String email = txtUserEmail.getText().toString();
        final String password = txtPassword.getText().toString();
        if (email.equals("AdminStatusTrue")){
            Toast.makeText(this, "Chế độ login admin", Toast.LENGTH_SHORT).show();
            ADMIN_STATUS = true;
            return;
        }else if (email.equals("AdminStatusFalse")){
            Toast.makeText(this, "Chế độ login bình thường", Toast.LENGTH_SHORT).show();
            ADMIN_STATUS = false;
            return;
        }
        if (
                AdditionalFunctions.isStringEmpty(getBaseContext(),email, password) ||
                !AdditionalFunctions.isEmailValid(getBaseContext(),email) ||
                !AdditionalFunctions.isPasswordValid(getBaseContext(),password)
            )
        {
            isLoggingIn = false;
            return;
        }
        FirebaseAuth myFirebaseAuth = FirebaseAuth.getInstance();
        myFirebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = authResult.getUser();
                if (user.isEmailVerified()){
                    if (ADMIN_STATUS){
                        DAO_Admin.loginAsAdmin(user, LoginActivity.this, password);
                    }else {
                        DAO_NguoiDung.loginAsUser(user, LoginActivity.this, password);
                    }
                    saveUserAccount(email, password);
                }else{
                    Toast.makeText(getBaseContext(), "Tài khoải chưa được xác thực!", Toast.LENGTH_SHORT).show();
                }
                isLoggingIn = false;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "Sai email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                isLoggingIn = false;
            }
        });
    }
    private void logOut(){
        if (FirebaseAuth.getInstance().getCurrentUser()!= null){
            FirebaseAuth.getInstance().signOut();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                authenticateGoogleAccount(account.getIdToken(), account.getDisplayName());
            }catch (ApiException e){
                e.printStackTrace();
            }
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
