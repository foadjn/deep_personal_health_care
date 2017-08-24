package sharif.deeppersonalheartcare;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import sharif.deeppersonalheartcare.arrays.arrayFactory.ArraysFactory;
import sharif.deeppersonalheartcare.wavelet.Wavelet;

public class MainActivity extends Activity {

    private TextView mTextView;

    private static float[] appender(float[] x1, float[] x2, float[] x3, float[] x4, float[] x5,
                                    float[] x6) {
        float[] output =
                new float[x1.length + x2.length + x3.length + x4.length + x5.length + x6.length];

        int index = 0;
        for (float temp : x1) {
            output[index] = temp;
            index++;
        }
        for (float temp : x2) {
            output[index] = temp;
            index++;
        }
        for (float temp : x3) {
            output[index] = temp;
            index++;
        }
        for (float temp : x4) {
            output[index] = temp;
            index++;
        }
        for (float temp : x5) {
            output[index] = temp;
            index++;
        }
        for (float temp : x6) {
            output[index] = temp;
            index++;
        }
        return output;
    }

    private static float[] downSample(float[] x, int rate) {

        if (rate == 1)
            return x;
        else {
            float[] output = new float[x.length / 2];
            int index = 0;
            for (int i = 0; i < x.length; i += 2, index++) {
                output[index] = x[i];
            }
            return output;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = stub.findViewById(R.id.text);
                List<ApplicationInfo> packages;
                PackageManager pm = getPackageManager();
                //get a list of installed apps.
                packages = pm.getInstalledApplications(0);

                ActivityManager mActivityManager = (ActivityManager) getApplicationContext()
                        .getSystemService(Context.ACTIVITY_SERVICE);

                for (ApplicationInfo packageInfo : packages) {
                    mActivityManager.killBackgroundProcesses(packageInfo.packageName);
                }

                final int[] allRawDownSample = new int[]{1, 2};
                final int[] allWaveletDownSample = new int[]{1, 2};
                final int[] allWaveletOmit = new int[]{0, 1, 2};


                for (int waveletDownSample : allWaveletDownSample) {
                    for (int waveletOmit : allWaveletOmit) {
                        for (int rawDownSample : allRawDownSample) {
                            ArraysFactory arrayFactory = new ArraysFactory(getApplicationContext());

                            long[] waveletTimes = new long[9];

                            float[] firstRawInput = arrayFactory.get1DFloats("first_raw_input");
                            float[] firstFeature = arrayFactory.get1DFloats("first_feature");
                            float[] secondRawInput = arrayFactory.get1DFloats("second_raw_input");
                            float[] secondFeature = arrayFactory.get1DFloats("second_feature");

                            float[] highPassFilter = arrayFactory.get1DFloats("high_pass_filter");
                            float[] lowPassFilter = arrayFactory.get1DFloats("low_pass_filter");


        /*
        from here the main code start to dot, cross and sum the matrices
         */
                            for (int index = 0; index < 9; index++) {

            /*
            ********************************************************************
            ***************************   wavelet start  ***********************
            ********************************************************************
             */
                                long waveletStartTime = System.currentTimeMillis();

                                Wavelet wavelet = new Wavelet();

                                float[] wavyInput1 = wavelet.wavelet(waveletOmit,
                                        downSample(firstRawInput, waveletDownSample),
                                        highPassFilter,
                                        lowPassFilter);

                                float[] wavyInput2 = wavelet.wavelet(waveletOmit,
                                        downSample(secondRawInput, waveletDownSample),
                                        highPassFilter,
                                        lowPassFilter);

                                appender(
                                        downSample(firstRawInput, rawDownSample),
                                        wavyInput1,
                                        firstFeature,
                                        downSample(secondRawInput, rawDownSample),
                                        wavyInput2,
                                        secondFeature);

                                long waveletEndTime = System.currentTimeMillis();
                                long totalWaveletTime = waveletEndTime - waveletStartTime;
                                waveletTimes[index] = totalWaveletTime;
                            }

                            Log.d("prof.hashemi", "wavelet down sample is: " + waveletDownSample);
                            Log.d("prof.hashemi", "wavelet omit is: " + waveletOmit);
                            Log.d("prof.hashemi", "raw down sample is: " + rawDownSample);

                            for (int i = 0; i < 9; i++) {
                                Log.d("prof.Hashemi", "wavelet time is: " + waveletTimes[i]);
                            }

                            Arrays.sort(waveletTimes);
                            String massage = "wavelet time is: " + waveletTimes[4];
                            mTextView.setText(massage);

                            Log.w("***", "wavelet median time is: " + waveletTimes[4]);

                        }
                    }
                }
            }
        });
    }
}
