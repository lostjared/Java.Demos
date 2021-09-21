package com.lostsidedead.sourcelexer;

import androidx.appcompat.app.AppCompatActivity;
import android.webkit.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.view.View.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view1 = (WebView) findViewById(R.id.webview1);
        button1 = (Button) findViewById(R.id.button);
        text1 = (EditText) findViewById(R.id.text1);
        button1.setOnClickListener( new OnClickListener() {
            public void onClick(View v) {
                String source = text1.getText().toString();
                String data = LexScanner.lexString(source);
                view1.loadData(data, "text/html", "UTF-8");
            }
        });
    }


    public WebView view1;
    public Button button1;
    public EditText text1;
}