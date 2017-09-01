package sharif.deeppersonalheartcare;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import sharif.deeppersonalheartcare.arrays.arrayFactory.ArraysFactory;
import sharif.deeppersonalheartcare.wavelet.Wavelet;

public class MainActivity extends Activity {

    static Random random = new Random();
    private static String message;

    private float[] arrayCutter1D(float[] input, int x) {
        float[] output = new float[x];

        System.arraycopy(input, 0, output, 0, x);
        return output;
    }

    public float[][] transposeMatrix(float[][] m) {

        int rowNumber = m.length;
        int columnNumber = m[0].length;

        float[][] temp = new float[columnNumber][rowNumber];

        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                temp[j][i] = m[i][j];
            }
        }

        return temp;
    }

    private float[][] randomInit2D(float[][] in) {

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

    private float[] appender(float[] x1, float[] x2, float[] x3, float[] x4, float[] x5,
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

    private float[] downSample(float[] x, int rate) {

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

    private float[] newCross(float[] in1, float[][] in2) {

        int n = in2.length;//600
        int m = in2[0].length;//1026

        float[] res = new float[n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                res[i] += in2[i][j] * in1[j];
            }
        }
        return res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_activity_main);
        final TextView mTextView = findViewById(R.id.text);


        List<ApplicationInfo> packages;
        PackageManager pm = getPackageManager();
        packages = pm.getInstalledApplications(0);

        ActivityManager mActivityManager = (ActivityManager) getApplicationContext().
                getSystemService(Context.ACTIVITY_SERVICE);
        String myPackage = getApplicationContext().getPackageName();

        for (ApplicationInfo packageInfo : packages) {
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) continue;
            if (packageInfo.packageName.equals(myPackage)) continue;
            mActivityManager.killBackgroundProcesses(packageInfo.packageName);
        }


        Thread backGroundThread = new Thread(new Runnable() {
            @Override
            public void run() {

                ArraysFactory arrayFactory = new ArraysFactory(getApplicationContext());

                final int rawDownSample = 1;
                final int waveletDownSample = 1;
                final int waveletOmit = 0;

                final int[] allPcaInput = new int[]{396};
                final int[] allPcaOutput = new int[]{300};

                for (int pcaInput : allPcaInput) {
                    for (int pcaOutput : allPcaOutput) {


                        long[] pcaTimes = new long[5];

                        float[] firstRawInput = arrayFactory.get1DFloats("first_raw_input");
                        float[] firstFeature = arrayFactory.get1DFloats("first_feature");
                        float[] secondRawInput = arrayFactory.get1DFloats("second_raw_input");
                        float[] secondFeature = arrayFactory.get1DFloats("second_feature");


                        float[] highPassFilter = arrayFactory.get1DFloats("high_pass_filter");
                        float[] lowPassFilter = arrayFactory.get1DFloats("low_pass_filter");

                        float[][] tempPca = new float[pcaInput][pcaOutput];
                        float[][] pca = transposeMatrix(randomInit2D(tempPca));

                        /*
                        from here the main code start to dot, cross and sum the matrices
                         */
                        for (int index = 0; index < 5; index++) {

                            /*
                            ********************************************************************
                            ***************************   wavelet start  ***********************
                            ********************************************************************
                             */
                            Wavelet wavelet = new Wavelet();

                            float[] wavyInput1 = wavelet.wavelet(waveletOmit,
                                    downSample(firstRawInput, waveletDownSample),
                                    highPassFilter,
                                    lowPassFilter);

                            float[] wavyInput2 = wavelet.wavelet(waveletOmit,
                                    downSample(secondRawInput, waveletDownSample),
                                    highPassFilter,
                                    lowPassFilter);

                            float[] x1 = arrayCutter1D(appender(
                                    downSample(firstRawInput, rawDownSample),
                                    wavyInput1,
                                    firstFeature,
                                    downSample(secondRawInput, rawDownSample),
                                    wavyInput2,
                                    secondFeature), pcaInput);

                            /*
                            ********************************************************************
                            *****************************   pca start  *************************
                            ********************************************************************
                             */

                            long crossStartTime = System.currentTimeMillis();
                            newCross(x1, pca);

                            long crossEndTime = System.currentTimeMillis();
                            long pcaTotalTime = crossEndTime - crossStartTime;
                            pcaTimes[index] = pcaTotalTime;

                            try {
                                if (index != 4)
                                    Thread.sleep(60000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        Log.d("prof.Hashemi", "pca input dim is:" + pcaInput);
                        Log.d("prof.Hashemi", "pca output dim is:" + pcaOutput);
                        for (int i = 0; i < 5; i++) {
                            Log.d("prof.Hashemi", "pca time is:" + pcaTimes[i]);
                        }

                        Arrays.sort(pcaTimes);

                        message = "pca time is:" + pcaTimes[2];

                        Log.w("***", "median of pca is:" + pcaTimes[2]);
                    }
                }

            }
        });

        try {

            backGroundThread.start();
            backGroundThread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mTextView.setText(message);
    }

}
