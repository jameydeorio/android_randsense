package com.royalpaw.randsense;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.royalpaw.randsense.db.Sentence;
import com.royalpaw.randsense.db.SentencesDataSource;
import com.royalpaw.randsense.util.WebClient;

import java.util.List;

public class LaunchActivity extends Activity
{
    private static final String TAG = "LaunchActivity";

    private SentencesDataSource mDataSource;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListView sentencesList = (ListView) findViewById(R.id.sentences_list);
        registerForContextMenu(sentencesList);

        mDataSource = new SentencesDataSource(this);
        List<Sentence> sentences = mDataSource.getAllSentences();
        ArrayAdapter<Sentence> adapter = new ArrayAdapter<Sentence>(this, android.R.layout.simple_list_item_1, sentences);
        ListView listView = (ListView) findViewById(R.id.sentences_list);
        listView.setAdapter(adapter);
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
                deleteAllSentences();
                ArrayAdapter<Sentence> adapter = getSentenceAdapter();
                adapter.clear();
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sentence_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ArrayAdapter<Sentence> adapter = getSentenceAdapter();
        Sentence sentence = adapter.getItem(info.position);

        switch (item.getItemId()) {
            case R.id.favorite_sentence:
                return true;
            case R.id.delete_sentence:
                deleteSentence(sentence);
                adapter.remove(sentence);
                adapter.notifyDataSetChanged();
                return true;
            case R.id.share_sentence:
                shareSentence(sentence);
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void shareSentence(Sentence sentence) {
        // TODO: Figure out how to discern which service is chosen,
        // so we can add arbitrary stuff, like hash tags for Twitter
        String message = sentence.toString();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_sentence_intent_heading)));
    }

    private void deleteAllSentences() {
        mDataSource.deleteAllSentences();
    }

    private void deleteSentence(Sentence sentence) {
        mDataSource.deleteSentence(sentence);
    }

    private ArrayAdapter<Sentence> getSentenceAdapter() {
        ListView listView = (ListView) findViewById(R.id.sentences_list);
        return (ArrayAdapter<Sentence>) listView.getAdapter();
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
