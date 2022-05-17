package com.minhthieu.instagramofficial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class StartActivity extends AppCompatActivity {
    // basic
    private Button loginBt;
    private TextView notAUser;
    private LinearLayout forgotPassword;

    // text input edit text
    private TextInputLayout emailContainer;
    private TextInputLayout passwordContainer;

    private TextInputEditText emailEdit;
    private TextInputEditText passwordEdit;

    // FIREBASE
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;

    // ERROR TYPE
    private final String EMAIL_BLANK = "EMAIL_BLANK";
    private final String INVALID_EMAIL = "INVALID_EMAIL";

    private final String WRONG_PASSWORD = "WRONG_PASSWORD";
    private final String PASSWORD_BLANK = "PASSWORD_BLANK";

    // may cai xamcho
    static boolean isNewUser = false; // cai nay de check coi email co duoc tao chua

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Anh xa
        AnhXa();
        // OnFocusChange
        ContainOnFocusChange();
        // OnClickListener()
        ContainOnClickListener();
        // Contain onTextChangeListener
        ContainOnTextChangeListener();

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(StartActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else {
            Objects.requireNonNull(mAuth.getCurrentUser()).reload();
        }
    }

    private void ContainOnTextChangeListener() {
        // email
        emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emailContainer.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // password
        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordContainer.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void ContainOnClickListener() {
        // login button
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailEdit.getText().toString().isEmpty()) {
                    ShowError(EMAIL_BLANK);
                } else if (passwordEdit.getText().toString().isEmpty()) {
                    ShowError(PASSWORD_BLANK);
                }

                // password and email has been filled
                else {
                    String email = emailEdit.getText().toString().trim();
                    String password = passwordEdit.getText().toString().trim();
                    mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(StartActivity.this, "Welcome back", Toast.LENGTH_SHORT).show();
                            // go to main

                            Intent intent = new Intent(StartActivity.this, MainActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                            if(e instanceof FirebaseAuthInvalidUserException){
                                ShowError(INVALID_EMAIL);
                            }
                            else if(e instanceof FirebaseAuthInvalidCredentialsException){
                                passwordContainer.setError("The password that you've entered is incorrect");
                            }
                        }
                    });
                }
            }
        });

        // forgot password
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswordCall();
            }
        });


        // Move to Register activity (Not a user button)
        notAUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, RegisterActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void forgotPasswordCall() {
        String emailAddress = emailEdit.getText().toString();
        if(emailAddress.isEmpty()){
            ShowError(EMAIL_BLANK);
            return;
        } else{
            IsEmailExist(emailAddress);
            if(isNewUser){
                ShowError(INVALID_EMAIL);
                return;
            }
        }
            mAuth.sendPasswordResetEmail(emailAddress) // after everything works
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(StartActivity.this, "Please check your inbox", Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(StartActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }




    private void IsEmailExist(String email) {
        //check email already exist or not.
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<SignInMethodQueryResult> task) {
                isNewUser = task.getResult().getSignInMethods().isEmpty();
            }
        });

    }

    private void ContainOnFocusChange() {

        emailEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    // if email was blank
                    if (emailEdit.getText().toString().isEmpty()) {
                        ShowError(EMAIL_BLANK);
                    }
                } else {
                    emailContainer.setError(null);
                }
            }
        });

        passwordEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    // if email was blank
                    if (passwordEdit.getText().toString().isEmpty()) {
                        ShowError(PASSWORD_BLANK);
                    }
                } else {
                    passwordContainer.setError(null);
                }
            }
        });

    }

    void ShowError(String errorType) {
        switch (errorType) {
            case EMAIL_BLANK:
                emailContainer.setError("Email cannot be blank");
                break;

            case PASSWORD_BLANK:
                passwordContainer.setError("Password cannot be blank");
                break;
            case INVALID_EMAIL:
                emailContainer.setError("The email address you entered isn't connected to an account");
                break;
        }
    }


    private void AnhXa() {
        loginBt = (Button) findViewById(R.id.login_bt);
        notAUser = (TextView) findViewById(R.id.register_field);
        forgotPassword = (LinearLayout) findViewById(R.id.forgot_password_field);

        // firebase
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();

        // new text
        emailContainer = (TextInputLayout) findViewById(R.id.email_field);
        passwordContainer = (TextInputLayout) findViewById(R.id.password_field);

        emailEdit = (TextInputEditText) findViewById(R.id.email);
        passwordEdit = (TextInputEditText) findViewById(R.id.password);

    }
}