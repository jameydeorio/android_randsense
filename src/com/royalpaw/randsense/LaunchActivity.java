package com.royalpaw.randsense;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.royalpaw.randsense.db.Sentence;
import com.royalpaw.randsense.db.SentencesDataSource;
import com.royalpaw.randsense.util.Constants;
import com.royalpaw.randsense.util.WebClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class LaunchActivity extends ListActivity
{
    private static final String TAG = "LaunchActivity";

    private SentencesDataSource dataSource;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        dataSource = new SentencesDataSource(this);
        dataSource.open();

        List<Sentence> values = dataSource.getAllSentences();
        ArrayAdapter<Sentence> adapter = new ArrayAdapter<Sentence>(this, android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
        dataSource.close();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sentence_button:
                WebClient webClient = new WebClient(this);
                webClient.getNewSentence();
                break;
        }
    }
}
