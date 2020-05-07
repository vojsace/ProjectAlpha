package com.vojsace.projectalpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private TextInputLayout signInMail, signInPass;
    private FirebaseAuth auth;
    private Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInMail = (TextInputLayout) findViewById(R.id.signIn_Email_input);
        signInPass = (TextInputLayout) findViewById(R.id.signIn_Pass_input);
        signInBtn = (Button) findViewById(R.id.signIn_btn);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = signInMail.getEditText().getText().toString();
                final String pass = signInPass.getEditText().getText().toString();
                boolean isvalid = true;

                if (email.isEmpty()){
                    signInMail.setError("Please enter your E-mail");
                    isvalid = false;
                }else
                    signInMail.setError(null);

                if (pass.isEmpty()){
                    signInPass.setError("Please enter a Password");
                    isvalid = false;
                }else
                    signInPass.setError(null);

                //authenticate user
                if (isvalid) {
                    auth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        //there was an error
                                        if (email.length() < 5){
                                            signInMail.setError("Invalid E-mail");
                                        }else
                                            signInMail.setError(null);
                                        if (pass.length() < 8) {
                                            signInPass.setError("Invalid Password");
                                        } else {
                                            signInPass.setError(null);
                                            Toast.makeText(SignInActivity.this, "Error",Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    public void NavigateSignUp(View v) {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
    public void NavigateForgetMyPassword(View v) {
        Intent intent = new Intent(SignInActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }

}
