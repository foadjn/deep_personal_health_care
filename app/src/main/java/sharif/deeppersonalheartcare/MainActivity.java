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
import java.util.Random;

import sharif.deeppersonalheartcare.arrays.arrayFactory.ArraysFactory;
import sharif.deeppersonalheartcare.wavelet.Wavelet;

public class MainActivity extends Activity {

    static Random random = new Random();
    private TextView mTextView;

    private static float[] cross(float[] in1, float[][] in2) {

        int m = in2.length;
        int n = in2[0].length;

        float[] res = new float[n];

        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                res[j] += in2[i][j] * in1[i];
            }
        }

        return res;
    }

    private static float[][] randomInit2D(float[][] in) {

        int dim1 = in.length;
        int dim2 = in[1].length;

        for (int i = 0; i < dim1; i++) {
            in[i] = new float[dim2];
        }

        for (int i = 0; i < dim1; i++) {
            for (int j = 0; j < dim2; j++) {
                in[i][j] = random.nextFloat();
            }
        }
        return in;
    }

    private static int pcaInputCalculator(int rawDownSample, int waveletDownSample, int waveletOmit) {

        int pcaInput = 0;

        if (rawDownSample == 1 && waveletDownSample == 1 && waveletOmit == 0) {
            pcaInput = 1026;
        } else if (rawDownSample == 1 && waveletDownSample == 1 && waveletOmit == 1) {
            pcaInput = 774;
        } else if (rawDownSample == 1 && waveletDownSample == 1 && waveletOmit == 2) {
            pcaInput = 646;
        } else if (rawDownSample == 1 && waveletDownSample == 2 && waveletOmit == 0) {
            pcaInput = 778;
        } else if (rawDownSample == 1 && waveletDownSample == 2 && waveletOmit == 1) {
            pcaInput = 650;
        } else if (rawDownSample == 1 && waveletDownSample == 2 && waveletOmit == 2) {
            pcaInput = 584;
        } else if (rawDownSample == 2 && waveletDownSample == 1 && waveletOmit == 0) {
            pcaInput = 776;
        } else if (rawDownSample == 2 && waveletDownSample == 1 && waveletOmit == 1) {
            pcaInput = 524;
        } else if (rawDownSample == 2 && waveletDownSample == 1 && waveletOmit == 2) {
            pcaInput = 396;
        } else if (rawDownSample == 2 && waveletDownSample == 2 && waveletOmit == 0) {
            pcaInput = 528;
        } else if (rawDownSample == 2 && waveletDownSample == 2 && waveletOmit == 1) {
            pcaInput = 400;
        } else if (rawDownSample == 2 && waveletDownSample == 2 && waveletOmit == 2) {
            pcaInput = 334;
        }

        return pcaInput;
    }

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

                final int rawDownSample = 1;
                final int waveletDownSample = 1;
                final int waveletOmit = 0;
                final int pcaInput =
                        pcaInputCalculator(rawDownSample, waveletDownSample, waveletOmit);

                final int pcaOutput = 600;

                ArraysFactory arrayFactory = new ArraysFactory(getApplicationContext());

                long[] pcaTimes = new long[9];
                long[] waveletTimes = new long[9];

                float[] firstRawInput = arrayFactory.get1DFloats("first_raw_input");
                float[] firstFeature = arrayFactory.get1DFloats("first_feature");
                float[] secondRawInput = arrayFactory.get1DFloats("second_raw_input");
                float[] secondFeature = arrayFactory.get1DFloats("second_feature");


                //according to the paper this is the lstm input and it's name must be x.
                float[] x = arrayFactory.get1DFloats("x");

                float[] highPassFilter = arrayFactory.get1DFloats("high_pass_filter");
                float[] lowPassFilter = arrayFactory.get1DFloats("low_pass_filter");

                float[][] pca = new float[pcaInput][pcaOutput];
                pca = randomInit2D(pca);

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

                    float[] x1 = appender(
                            downSample(firstRawInput, rawDownSample),
                            wavyInput1,
                            firstFeature,
                            downSample(secondRawInput, rawDownSample),
                            wavyInput2,
                            secondFeature);

                    long waveletEndTime = System.currentTimeMillis();
                    long totalWaveletTime = waveletEndTime - waveletStartTime;
                    waveletTimes[index] = totalWaveletTime;

            /*
            ********************************************************************
            *****************************   pca start  *************************
            ********************************************************************
             */

                    long crossStartTime = System.currentTimeMillis();

                    cross(x1, pca);

                    long crossEndTime = System.currentTimeMillis();
                    long pcaTotalTime = crossEndTime - crossStartTime;
                    pcaTimes[index] = pcaTotalTime;


                    for (int i = 0; i < 9; i++) {
                        Log.d("prof.Hashemi", "lstm time is:" + pcaTimes[i] + "\nwavelet time is: " +
                                waveletTimes[i]);
                    }

                    Arrays.sort(pcaTimes);
                    Arrays.sort(waveletTimes);

                    mTextView.setText("lstm time is:" +
                            pcaTimes[4] + "\n wavelet time is: " + waveletTimes[4]);

                    Log.w("***", "lstm time is:" +
                            pcaTimes[4] + "\nwavelet time is: " + waveletTimes[4]);
                }
            }
        });
    }
}
