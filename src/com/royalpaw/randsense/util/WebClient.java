package com.royalpaw.randsense.util;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import com.royalpaw.randsense.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: jamey
 * Date: 2/21/13
 * Time: 11:24 PM
 */
public class WebClient {
    private static final String TAG = "WebClient";

    public void getNewSentence(Activity activity) {
        new FetchSentenceJSON(activity).execute();
    }

    private static class FetchSentenceJSON extends AsyncTask<Void, Void, String> {
        private static Activity mActivity;

        FetchSentenceJSON(Activity activity) {
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            Button button = (Button)mActivity.findViewById(R.id.sentence_button);
            button.setEnabled(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
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
                JSONObject json = new JSONObject(sb.toString());
                return json.getString("sentence");
            } catch (JSONException e) {
                Log.e(TAG, "JSONException while getting sentence from response", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String newSentence) {
            TextView textView = (TextView)mActivity.findViewById(R.id.sentence);
            textView.setText(newSentence);

            Button button = (Button)mActivity.findViewById(R.id.sentence_button);
            button.setEnabled(true);
        }
    }
}
