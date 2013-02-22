package com.royalpaw.randsense;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.royalpaw.randsense.util.Constants;
import com.royalpaw.randsense.util.WebClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class LaunchActivity extends Activity
{
    private static final String TAG = "LaunchActivity";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void newSentence(View view) throws Exception {
        WebClient webClient = new WebClient();
        webClient.getNewSentence(this);
    }
}
