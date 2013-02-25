package com.royalpaw.randsense;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.royalpaw.randsense.db.Sentence;
import com.royalpaw.randsense.db.SentencesDataSource;
import com.royalpaw.randsense.util.WebClient;

import java.util.List;

public class LaunchActivity extends Activity
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

        List<Sentence> sentences = dataSource.getAllSentences();
        ArrayAdapter<Sentence> adapter = new ArrayAdapter<Sentence>(this, android.R.layout.simple_list_item_1, sentences);
        ListView listView = (ListView) findViewById(R.id.sentences_list);
        listView.setAdapter(adapter);
        dataSource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                dataSource.open();
                dataSource.deleteAllSentences();
                dataSource.close();
                ListView listView = (ListView) findViewById(R.id.sentences_list);
                ArrayAdapter<Sentence> adapter = (ArrayAdapter<Sentence>) listView.getAdapter();
                adapter.clear();
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
