package com.minhthieu.instagramofficial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    // may thu binh thuong
    private Button register_bt;
    private TextView notRegister;

    // text to fill in
    private TextInputEditText fullnameEdit;
    private TextInputEditText emailEdit;
    private TextInputEditText userNameEdit;
    private TextInputEditText passwordEdit;

    private TextInputLayout fullnameContainer;
    private TextInputLayout emailContainter;
    private TextInputLayout userNameContainer;
    private TextInputLayout passwordContainer;

    //fire base
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;

    // all the errors

    private final String USERNAME_ALREADY_EXIST = "USERNAME_ALREADY_EXIST";
    // error này nếu như nhập kí tự lung tung vào usernam
    private final String USER_NAME_INVALID = "USERNAME_INVALID";
    private final String EMAIL_INVALID = "EMAIL_INVALID";
    private final String ALREADY_EXITED_ENAIL = "ALREADY_EXITED_ENAIL";

    private final String USER_NAME_BLANK = "USER_NAME_BLANK";
    private final String FULLNAME_BLANK = "FULLNAME_BLANK";
    private final String PASSWORD_BLANK = "PASSWORD_BLANK";
    private final String EMAIL_BLANK = "EMAIL_BLANK";
    private final String PASSWORD_TOO_WEAK = "PASSWORD_TOO_WEAK";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Anh xa
        AnhXa();
        // ContainOnTextChangeListener
        ContainOnTextChangeListener();
        // ContainOnFocusChangeListener
        ContainOnFocusChangeListener();
        //ContainOnClickListener
        ContainOnClickListener();
    }

    // OnClick ================= ++ ==============================
    private void ContainOnClickListener() {
        // register button
        register_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckIfEmailValid();
                // CheckIfSomethingIsBlank
                CheckIfSomethingIsBlank(EMAIL_BLANK);
                CheckIfSomethingIsBlank(PASSWORD_BLANK);
                CheckIfSomethingIsBlank(FULLNAME_BLANK);
                CheckIfSomethingIsBlank(USER_NAME_BLANK);

                if (!userNameEdit.getText().toString().isEmpty()) {
                    CheckIfUsernameHasInvalidSyntax(userNameEdit.getText().toString());

                }

                if (emailContainter.getError() == null && passwordContainer.getError() == null &&
                        fullnameContainer.getError() == null && userNameContainer.getError() == null) {

                    CreateNewUser();
                }

            }
        });

        // Sign in
        notRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });

    }

    // OnClick ================= ++ ==============================


    // CREATE USER ================== ++ ==================================

    private void CreateNewUser() {
        String email = emailEdit.getText().toString().trim();
        String fullname = fullnameEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        String username = userNameEdit.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(RegisterActivity.this, "Welcome to Instagram", Toast.LENGTH_SHORT).show();


                PushUserInfoToDatabase(email, password, fullname, username);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                if (e instanceof FirebaseAuthUserCollisionException) {
                    String errorCode = ((FirebaseAuthUserCollisionException) e).getErrorCode();
                    if (errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE")) {
                        ShowError(ALREADY_EXITED_ENAIL);
                        // showing error
                    }
                } else if (e instanceof FirebaseAuthWeakPasswordException) {
                    Toast.makeText(RegisterActivity.this, ((FirebaseAuthWeakPasswordException) e).getReason(), Toast.LENGTH_SHORT).show();
                    ShowError(PASSWORD_TOO_WEAK);
                }
            }
        });


    }

    private String GetImageUrl() {
        // get drawable Uri
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(R.drawable.anh)
                + '/' + getResources().getResourceTypeName(R.drawable.anh) + '/' + getResources().getResourceEntryName(R.drawable.anh) );


       return imageUri.toString();
    }

    private void PushUserInfoToDatabase(String email, String password, String fullname, String username) {
        DatabaseReference mUserReference = mDatabase.getReference("Users");

        // a object of shit
        HashMap<String, Object> info = new HashMap<>();
        info.put("Username", username);
        info.put("Password", password);
        info.put("Email", email);
        info.put("Fullname", fullname);
        info.put("UserID", mAuth.getCurrentUser().getUid());
        info.put("imageUrl",GetImageUrl());

        // create followers and following

        // followers
        FirebaseDatabase.getInstance().getReference("Follow").child("Followers").child(mAuth.getCurrentUser().getUid());
        // following

        mUserReference.child(mAuth.getCurrentUser().getUid()).setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Upload data successful. Please update your profile"
                            , Toast.LENGTH_SHORT).show();

                    // move to main activity
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else  {
                    Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // END ======================== ++ ==========================


    private void ContainOnFocusChangeListener() {
        // email
        userNameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    CheckIfSomethingIsBlank(USER_NAME_BLANK);

                } else {
                    userNameContainer.setError(null);
                }
            }
        });
        // ================== ++ ==========================

        passwordEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    CheckIfSomethingIsBlank(PASSWORD_BLANK);

                } else {
                    passwordContainer.setError(null);
                }
            }
        });
        // ================== ++ ==========================

        fullnameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    CheckIfSomethingIsBlank(FULLNAME_BLANK);

                } else {
                    fullnameContainer.setError(null);
                }
            }
        });
        // ================== ++ ==========================

        emailEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    CheckIfSomethingIsBlank(EMAIL_BLANK);
                    CheckIfEmailValid();

                } else {
                    emailContainter.setError(null);
                }
            }
        });


    }


    private void ContainOnTextChangeListener() {
        // user name
        userNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userNameContainer.setError(null);

            }

            @Override

            public void afterTextChanged(Editable editable) {
                // có thể start 1 cái counter ở đây, nhưng mà muốn thì implement sau

                // cái này là cái sẽ lưu mấy cái change của text ổn định nhất á
                CheckIfUsernameHasExited(editable.toString());

                CheckIfUsernameHasInvalidSyntax(editable.toString());
            }
        });
        // ================ ++ =====================
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
        // ================ ++ =====================

        fullnameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fullnameContainer.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // ================ ++ =====================

        emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emailContainter.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    void CheckIfUsernameHasExited(String username) {


    }


    // THE CHECK ERROR PART
    // *********************** ++ **********************

    private void CheckIfEmailValid() {
        String email = emailEdit.getText().toString().trim();
        if (!email.isEmpty()) {
            Pattern email_pattern = Patterns.EMAIL_ADDRESS;
            Matcher matcher_1 = email_pattern.matcher(email);
            boolean check_1 = matcher_1.matches();
            // neu như email ko theo form
            if (!check_1) {
                ShowError(EMAIL_INVALID);
            }
        }
    }


    private void CheckIfUsernameHasInvalidSyntax(String username) {
        Pattern my_pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = my_pattern.matcher(username);
        boolean check = matcher.find();

        if (check || username.contains(" ")) {
            ShowError(USER_NAME_INVALID);
        }

    }

    private void CheckIfSomethingIsBlank(String field) {
        // cailoz nay phai dem xuong kia, ma met vailoz nen đ làm occee
        switch (field) {
            case USER_NAME_BLANK:
                if (userNameEdit.getText().toString().trim().isEmpty()) {
                    userNameContainer.setError("Username cannot be blank");
                }
                break;

            case PASSWORD_BLANK:
                if (passwordEdit.getText().toString().trim().isEmpty()) {
                    passwordContainer.setError("Password cannot be blank");
                }
                break;

            case EMAIL_BLANK:
                if (emailEdit.getText().toString().trim().isEmpty()) {
                    emailContainter.setError("Email cannot be blank");
                }
                break;

            case PASSWORD_TOO_WEAK:
                passwordContainer.setError("");
                break;
            case FULLNAME_BLANK:
                if (fullnameEdit.getText().toString().trim().isEmpty()) {
                    fullnameContainer.setError("Fullname cannot be blank");
                }
                break;
        }
    }


    // *********************** ++ **********************


    void ShowError(String errorType) {
        switch (errorType) {
            case USER_NAME_INVALID:
                userNameContainer.setError("Please not use special character or white space");
                break;

            case EMAIL_INVALID:
                emailContainter.setError("Please enter valid email");
                break;

            case ALREADY_EXITED_ENAIL:
                emailContainter.setError("Already in use. Sign in?");
                break;

            case USERNAME_ALREADY_EXIST:

                break;


        }
    }


    private void AnhXa() {
        register_bt = (Button) findViewById(R.id.register_bt);
        notRegister = (TextView) findViewById(R.id.sign_in);

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        fullnameEdit = (TextInputEditText) findViewById(R.id.fullname);
        emailEdit = (TextInputEditText) findViewById(R.id.email);
        passwordEdit = (TextInputEditText) findViewById(R.id.password);
        userNameEdit = (TextInputEditText) findViewById(R.id.user_name);

        fullnameContainer = (TextInputLayout) findViewById(R.id.fullname_field);
        emailContainter = (TextInputLayout) findViewById(R.id.email_field);
        passwordContainer = (TextInputLayout) findViewById(R.id.password_field);
        userNameContainer = (TextInputLayout) findViewById(R.id.user_name_field);

    }
}