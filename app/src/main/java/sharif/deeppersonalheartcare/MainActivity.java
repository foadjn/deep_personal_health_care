package sharif.deeppersonalheartcare;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import sharif.deeppersonalheartcare.newModel.OurModel;

public class MainActivity extends Activity {

    private static String message;
    OurModel personalECG = null;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_activity_main);
        mTextView = findViewById(R.id.text);
        String welcomeString = "welcome to Deep Personal Heart Care(DPHC)";
        mTextView.setText(welcomeString);

        personalECG = new OurModel(getApplicationContext());

        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                message = personalECG.modelCore();
            }
        });

        try {

            backgroundThread.start();
            backgroundThread.join();

        } catch (Exception e) {
            e.printStackTrace();
        }


        ModelMainFunctionTask mainFunctionTask = new ModelMainFunctionTask();
        mainFunctionTask.execute();


        String wholeMessage = mTextView.getText() + "\n" + message;
        mTextView.setText(wholeMessage);
        Log.d("TIME", "Total execution time with Thread is: " + message);
    }

    @SuppressLint("StaticFieldLeak")
    private class ModelMainFunctionTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return personalECG.modelCore();
        }

        @Override
        protected void onPostExecute(String message) {
            Log.d("Time", "The total time with AsyncTask is:" + message);
            mTextView.setText(mTextView.getText() + "\n" + message);
        }
    }
}
