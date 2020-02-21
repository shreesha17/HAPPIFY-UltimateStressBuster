package com.example.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class regt extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth myauth;
    private EditText name, email, passw, cpas,cname;
    private Button submit;
    private String vname, vemail, vpassw, vcpas,vcname;
    private ProgressBar pg;
    private int type;
    private FirebaseUser ussr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regt);
        name = findViewById(R.id.name2);
        email = findViewById(R.id.email2);
       // etid = findViewById(R.id.eusn2);
        //sub1 = findViewById(R.id.subject1);
        cname = findViewById(R.id.subject1);
        passw = findViewById(R.id.pas2);
        cpas = findViewById(R.id.cpas2);
        pg = findViewById(R.id.progressBar3);
        pg.setVisibility(View.GONE);
        myauth = FirebaseAuth.getInstance();
        submit = findViewById(R.id.submit);
        findViewById(R.id.sub2).setOnClickListener(this);

    }

    public void registerUser() {

        vname = name.getText().toString().trim();
        vemail = email.getText().toString().trim();
        //vetid = etid.getText().toString().trim();
        //vsub1 = sub1.getText().toString().trim();
        vcname = cname.getText().toString().trim();
        vpassw = passw.getText().toString().trim();
        vcpas = cpas.getText().toString().trim();

        if (TextUtils.isEmpty(vemail)|| TextUtils.isEmpty(vpassw) || TextUtils.isEmpty(vname)||TextUtils.isEmpty(vcpas))
            Toast.makeText(regt.this, "Fields can't be empty", Toast.LENGTH_LONG).show();
        else if (!vcpas.equals(vpassw))
            Toast.makeText(regt.this, "Please enter the same password", Toast.LENGTH_LONG).show();

        else {

            pg.setVisibility(View.VISIBLE);

            myauth.createUserWithEmailAndPassword(vemail, vpassw)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                //store in db
                                    tuser tusr = new tuser(vname, vemail,vcname, type);
                                FirebaseDatabase.getInstance().getReference("corporate").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(tusr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        pg.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            Toast.makeText(regt.this, "registration successfull", Toast.LENGTH_LONG).show();
                                            finish();
                                            Intent intent = new Intent(regt.this, ques.class);
                                            startActivity(intent);
                                        } else {
                                            ussr = FirebaseAuth.getInstance().getCurrentUser();
                                            ussr.delete();
                                            Toast.makeText(regt.this, "Sorry registration unsuccessfull", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });

                            } else {
                                pg.setVisibility(View.GONE);
                                Toast.makeText(regt.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        registerUser();
    }
}