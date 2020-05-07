package com.vojsace.projectalpha;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
private TextInputLayout signUpUsername, signUpMail, signUpPass;
private Button signUpBtn;
private FirebaseAuth auth;

private DatabaseReference reference;

private String username, email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpUsername = (TextInputLayout) findViewById(R.id.username_registerID);
        signUpMail = (TextInputLayout) findViewById(R.id.email_registerID);
        signUpPass = (TextInputLayout) findViewById(R.id.pass_registerID);
        reference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        signUpBtn = (Button) findViewById(R.id.signUp_btn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = signUpUsername.getEditText().getText().toString();
                email = signUpMail.getEditText().getText().toString();
                pass = signUpPass.getEditText().getText().toString();

                if (username.isEmpty()){
                    signUpUsername.setError("Please enter your Username");
                }else
                    signUpUsername.setError(null);
                if (email.isEmpty()){
                    signUpMail.setError("Please enter your E-mail");
                }else
                    signUpMail.setError(null);
                if (pass.isEmpty()){
                    signUpPass.setError("Please enter a Password");
                }else if (pass.length() < 8){
                    signUpPass.setError("Password too weak");
                }else
                    signUpPass.setError(null);

                    if (!username.isEmpty() && !email.isEmpty() && !pass.isEmpty()) {
                        auth.createUserWithEmailAndPassword(email, pass)
                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                        } else {
                                            String uId = auth.getUid();
                                            writeNewUser(uId, username);
                                            startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                            finish();
                                        }

                                    }
                                });
                    }else {
                        Toast.makeText(SignUpActivity.this, "Error Occupied", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }
    public void navigate_sign_in(View v){
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
    }
    private void writeNewUser(String userId, String username){
        UserInfo userInfo = new UserInfo(username);

        reference.child("Users").child(userId).setValue(userInfo);
    }
}
