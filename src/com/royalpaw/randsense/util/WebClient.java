package com.royalpaw.randsense.util;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.royalpaw.randsense.R;
import com.royalpaw.randsense.db.Sentence;
import com.royalpaw.randsense.db.SentencesDataSource;
import org.json.JSONException;
import org.json.JSONObject;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: jamey
 * Date: 2/21/13
 * Time: 11:24 PM
 */
public class WebClient {
    private static final String TAG = "WebClient";

    private Context mContext;

    public WebClient(Context context) {
        this.mContext = context;
    }

    public void getNewSentence() {
        new FetchSentence(mContext).execute();
    }

    private static class FetchSentence extends AsyncTask<Void, Void, JSONObject> {
        private static Activity mActivity;

        FetchSentence(Context context) {
            mActivity = (Activity) context;
        }

        @Override
        protected void onPreExecute() {
            Button button = (Button) mActivity.findViewById(R.id.sentence_button);
            button.setEnabled(false);
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            String finalString = "";
            URL url;
            StringBuilder sb = new StringBuilder();

            try {
                url = new URL(Constants.GENERATE_SENTENCE_URL);
            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException while creating URL for " + Constants.GENERATE_SENTENCE_URL, e);
                return null;
            }

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
            } catch (IOException e) {
                Log.e(TAG, "IOException when making BufferedReader", e);
                return null;
            }

            try {
                JSONObject sentenceJSON = new JSONObject(sb.toString());
                return sentenceJSON;
            } catch (JSONException e) {
                Log.e(TAG, "JSONException while getting sentence from response", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject sentenceJSON) {
            String newSentence;
            long newPk;

            try {
                newSentence = sentenceJSON.getString("sentence");
                newPk = sentenceJSON.getInt("pk");
            } catch (JSONException e) {
                Log.e(TAG, "JSONException while grabbing sentence and pk: ", e);
                return;
            }

            TextView textView = (TextView) mActivity.findViewById(R.id.sentence);
            textView.setText(newSentence);

            SentencesDataSource dataSource = new SentencesDataSource(mActivity);
            dataSource.open();
            Sentence sentenceObject = dataSource.createSentence(newSentence, newPk);
            dataSource.close();

            Button button = (Button) mActivity.findViewById(R.id.sentence_button);
            button.setEnabled(true);

            ListView listView = (ListView) mActivity.findViewById(R.id.sentences_list);
            ArrayAdapter<Sentence> adapter = (ArrayAdapter<Sentence>) listView.getAdapter();
            adapter.insert(sentenceObject, 0);
            adapter.notifyDataSetChanged();
        }
    }
}
