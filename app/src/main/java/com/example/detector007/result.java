package com.example.detector007;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class result extends AppCompatActivity {

    private Button decode,browser,back;
    public static final String NAME = "NAME";
    private TextView nameText;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        browser=(Button) findViewById(R.id.browserbtn);
        back=(Button) findViewById(R.id.backbtn);
        nameText=findViewById(R.id.mName);
        Intent i=getIntent();
        name=i.getStringExtra(NAME);
        nameText.setText(name);
        browser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openbrowser();
            }
        });
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    public void openbrowser(){
        Intent intent= new Intent(this, browser.class);
        intent.putExtra(result.NAME,name);
        startActivity(intent);
    }
    public void back(){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
        //frontpage fact=new frontpage();
        //fact.openmainActivity2();

    }
}