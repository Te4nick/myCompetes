package ru.te4nick.mycompetes.ui.login;

import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LoginHandler {
    public final static int ERROR = 0;
    public final static int SUCCESS = 1;
    public final static int DATABASE_ERROR = 2;
    public final static int EMPTY_EMAIL = 10;
    public final static int INVALID_EMAIL = 11;
    public final static int UNCONFIRMED_EMAIL = 12;
    public final static int EMPTY_PASSWORD = 20;
    public final static int SHORT_PASSWORD = 21;

    private String email;
    private String password;
    private int authStateCode = ERROR;
    private FirebaseAuth mAuth;

    public LoginHandler() {}

    public int registerWithCredentials(String email, String password) {
        this.email = email;
        this.password = password;
        mAuth = FirebaseAuth.getInstance();
        if ((authStateCode = checkCredentials()) == SUCCESS) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User user = new User(email);
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            authStateCode = SUCCESS;
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

    public int loginWithCredentials(String email, String password) {
        this.email = email;
        this.password = password;
        mAuth = FirebaseAuth.getInstance();
        if ((authStateCode = checkCredentials()) == SUCCESS) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user.isEmailVerified()) {
                            authStateCode = SUCCESS;
                        } else {
                            authStateCode = UNCONFIRMED_EMAIL;
                        }
                    } else {
                        authStateCode = DATABASE_ERROR;
                    }
                }
            });



            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User user = new User(email);
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            authStateCode = SUCCESS;
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

    private int checkCredentials() {
        if (this.email.isEmpty()) {
            return EMPTY_EMAIL;
        }
        if (this.password.isEmpty()) {
            return EMPTY_PASSWORD;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(this.email).matches()) {
            return INVALID_EMAIL;
        }
        if (this.password.length() < 6) {
            return SHORT_PASSWORD;
        }
        return SUCCESS;
    }
}

//enum LoginState {
//    NONE (0),
//    EMPTY_EMAIL (10),
//    INVALID_EMAIL (11),
//    EMPTY_PASSWORD (20),
//    SHORT_PASSWORD (21),
//    CONNECTION_ERROR (1);
//    LoginState(int code) {}
//}

