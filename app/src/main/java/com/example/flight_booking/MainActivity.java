package com.example.flight_booking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.flight_booking.Models.Users;
import com.example.flight_booking.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
  private DatabaseReference mDatabase;
    private EditText etenterEmail;
    private ImageView imageView;
    private EditText mEtPassword,mEtPincode,mEtConfPassword,mEtEmail,mEtMobileNo;
    private EditText username;
    private int counter = 5;
    private Button buttonS1,buttonL2;
    private Button google;
   private FirebaseAuth mAuth;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar1;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    DatabaseReference databaseReference;
    String currentUserId;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    GoogleSignInClient mgoogleSignInClient;
    ImageView ivCp;
    ProgressBar progressBar;
    Uri imageUri;
    UploadTask uploadTask;
    StorageReference storageReference;
    private static final int PICK_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ivCp=findViewById(R.id.iv_cp);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        databaseReference = database.getReference("User");
        storageReference = FirebaseStorage.getInstance().getReference("Profile images");

//    documentReference = db.collection("user").document(currentUserId);
//


        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("we're create your Account");
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login to your account");

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mgoogleSignInClient = GoogleSignIn.getClient(this, gso);


        binding.Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(binding.editTextTextEmailAddress.getText().toString(), binding.password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if(task.getResult().getAdditionalUserInfo().isNewUser()){

                                        String email =user.getEmail();
                                        String uid =user.getUid();

                                        HashMap<Object,String> hashMap =new HashMap<>();

                                        hashMap.put("email",email);
                                        hashMap.put("uid",uid);

                                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                                        DatabaseReference reference =database.getReference("Users_profile");
                                        reference.child(uid).setValue(hashMap);

                                    }
                                    Intent intent = new Intent(MainActivity.this, BookAFlight.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }



                            }
                        });


            }
        });


        binding.google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(binding.editTextTextEmailAddress.getText().toString(),
                        binding.password.getText().toString()).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();

                                if (task.isSuccessful()) {
                                    Users users = new Users(binding.username.getText().toString(), binding.editTextTextEmailAddress.getText().toString(),binding.etmobile.getText().toString(), binding.password.getText().toString());

                                    String id = task.getResult().getUser().getUid();
                                    database.getReference().child("User").child(id).setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("success", "success");

                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i("error", e.getMessage());
                                        }
                                    });


                                    Toast.makeText(MainActivity.this, "UserCreated Successfully", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });


//        database = FirebaseDatabase.getInstance();
//        toolbar1 =  (Toolbar) findViewById(R.id.toolbar);
//      setSupportActionBar(toolbar1);
        nav = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

//
//
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar1, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mEtPassword = findViewById(R.id.password);
        etenterEmail = findViewById(R.id.editTextTextEmailAddress);
        imageView = findViewById(R.id.iv_view2);
        buttonS1 = findViewById(R.id.Signin);
        google = findViewById(R.id.google);
        username = findViewById(R.id.username);
        mEtConfPassword=findViewById(R.id.confirm_password);
        mEtMobileNo=findViewById(R.id.etmobile);
        mEtPincode=findViewById(R.id.etPincode);


//
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_profile:

                        Intent intent =new Intent(MainActivity.this,UserProfile.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Home panel is open", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_edit:
                        Toast.makeText(getApplicationContext(), "Edit panel is open", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }
                return true;
            }
        });


        mEtPassword = findViewById(R.id.password);
        mEtConfPassword= findViewById(R.id.confirm_password);
        etenterEmail = findViewById(R.id.editTextTextEmailAddress);
        Button btnlogin = findViewById(R.id.Signin);


    }


    int RC_SIGN_IN = 65;

    private void signIn() {
        Intent signInIntent = mgoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG, ", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG, ", "Google sign in failed", e);
                // ...
            }
        }
    }
//
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG, ", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Users user1 = new Users();
                            user1.setUserId(user.getUid());
                            user1.setUsername(user.getDisplayName());
                            database.getReference().child("User").child(user.getUid()).setValue(user1);


                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "Sign in with Google", Toast.LENGTH_LONG).show();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG, ", "signInWithCredential:failure", task.getException());
//                            Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });

    }

}






