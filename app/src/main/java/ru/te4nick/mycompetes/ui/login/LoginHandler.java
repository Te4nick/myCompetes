package ru.te4nick.mycompetes.ui.login;

import android.content.Intent;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import ru.te4nick.mycompetes.ui.nav_header.NavHeaderFragment;

public class LoginHandler {
    public final static int ERROR = 0;
    public final static int SUCCESS = 1;
    public final static int DATABASE_ERROR = 2;
    public final static int NULL_USER_ERROR = 3;
    public final static int EMPTY_EMAIL = 10;
    public final static int INVALID_EMAIL = 11;
    public final static int UNCONFIRMED_EMAIL = 12;
    public final static int EMPTY_PASSWORD = 20;
    public final static int SHORT_PASSWORD = 21;

    private User currentUser;
    private String email;
    private String password;
    private String username;
    private int authStateCode = ERROR;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    public LoginHandler() {}

    public int loginOrRegisterWithEmailAndPassword(String email, String password, String name) {
        if ((authStateCode = loginWithEmailAndPassword(email, password)) == DATABASE_ERROR) {
            if ((authStateCode = registerWithEmailAndPassword(email, password, name)) == DATABASE_ERROR) {
                return DATABASE_ERROR;
            }
        }
        return authStateCode;
    }


    public int registerWithEmailAndPassword(String email, String password, String name) {
        this.email = email;
        this.password = password;
        if ((authStateCode = checkCredentials()) == SUCCESS) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User user = new User(email, name);
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    mAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(name).build());
                                                    mAuth.getCurrentUser().sendEmailVerification(); // sends email verification
                                                    authStateCode = UNCONFIRMED_EMAIL;
                                                } else {
                                                    authStateCode = DATABASE_ERROR;
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }
        return authStateCode;
    }


    public int loginWithEmailAndPassword(String email, String password) {
        this.email = email;
        this.password = password;
        if ((authStateCode = checkCredentials()) == SUCCESS) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (!user.isEmailVerified()) {
                            user.sendEmailVerification();
                            authStateCode = UNCONFIRMED_EMAIL;
                        }
                    } else {
                        authStateCode = DATABASE_ERROR;
                    }
                }
            });
        }
        if (authStateCode == SUCCESS) { // we get current User
            mDatabase.getReference("Users").child(mAuth.getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User userProfile = snapshot.getValue(User.class);
                            if (userProfile != null) {
                                currentUser = userProfile;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        return authStateCode;
    }

    private int checkCredentials() {
        if (this.email.isEmpty()) { return EMPTY_EMAIL; }
        if (this.password.isEmpty()) { return EMPTY_PASSWORD; }
        if (!Patterns.EMAIL_ADDRESS.matcher(this.email).matches()) { return INVALID_EMAIL; }
        if (this.password.length() < 6) { return SHORT_PASSWORD; }
        return SUCCESS;
    }

    public void signOut() {
        mAuth.signOut();
    }

//    public User getCurrentUser() {
//        FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid())
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        User userProfile = snapshot.getValue(User.class);
//                        if (userProfile != null) {
//                            currentUser = userProfile;
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//        return currentUser;
//    }

    public String setValues() {
        mDatabase.getReference("Users").child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    currentUser = userProfile;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return username;
    }

    public String getName() {return mAuth.getCurrentUser().getDisplayName();}
}