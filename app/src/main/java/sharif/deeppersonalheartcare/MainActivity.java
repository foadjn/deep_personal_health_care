package sharif.deeppersonalheartcare;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import sharif.deeppersonalheartcare.arrays.arrayFactory.ArraysFactory;
import sharif.deeppersonalheartcare.wavelet.Wavelet;

public class MainActivity extends Activity {

    static Random random = new Random();

    private static float[] dot(float[] in1, float[] in2) {

        if (in2.length == 1) {
            for (int i = 0; i < in1.length; i++) {
                in1[i] = in1[i] * in2[0];
            }
        } else {
            for (int i = 0; i < in1.length; i++) {
                in1[i] = in1[i] * in2[i];
            }
        }

        return in1;
    }

    private static float[] crossInRange(float[] in1, float[][] in2, int i1, int i2) {

        int n = in2[0].length;

        float[] res = new float[n];

        for (int j = 0; j < n; j++) {
            for (int i = i1; i < i2; i++) {
                res[j] += in2[i][j] * in1[i];
            }
        }
        return res;
    }

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

    private static float[] sum3vector(float[] in1, float[] in2, float[] in3) {

        for (int i = 0; i < in1.length; i++) {
            in1[i] = in1[i] + in2[i] + in3[i];
        }

        return in1;
    }

    private static float[] sum2Vector(float[] in1, float[] in2) {

        for (int i = 0; i < in1.length; i++) {
            in1[i] = in1[i] + in2[i];
        }

        return in1;
    }

    private static float[] tanHEval(float[] in) {

        for (int i = 0; i < in.length; i++) {
            in[i] = (float) Math.tanh(in[i]);
        }

        return in;
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

    private static float[] sigmoid(float[] in) {
        for (int i = 0; i < in.length; i++) {
            in[i] = (float) (1 / (1 + Math.pow(Math.E, (-1 * in[i]))));
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

    private static float[][] arrayCutter2D(float[][] input, int y, int x) {
        float[][] output = new float[y][x];

        for (int i = 0; i < y; i++) {
            System.arraycopy(input[i], 0, output[i], 0, x);
        }
        return output;
    }

    private static float[] arrayCutter1D(float[] input, int x) {
        float[] output = new float[x];

        System.arraycopy(input, 0, output, 0, x);
        return output;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        final int lstmDepth = 1;
        final int pcaOutput = 600;
        final int lstmWidth = pcaOutput / lstmDepth;
        final int lstmNh = 30;

        ArraysFactory arrayFactory = new ArraysFactory(getApplicationContext());

        long[] lstmTimes = new long[9];
        long[] pcaTimes = new long[9];
        long[] waveletTimes = new long[9];

        float[] firstRawInput = arrayFactory.get1DFloats("first_raw_input");
        float[] firstFeature = arrayFactory.get1DFloats("first_feature");
        float[] secondRawInput = arrayFactory.get1DFloats("second_raw_input");
        float[] secondFeature = arrayFactory.get1DFloats("second_feature");

        float[][] w0 = arrayCutter2D(arrayFactory.get2DFloats("w0"), pcaOutput, lstmNh);
        float[][] w1 = arrayCutter2D(arrayFactory.get2DFloats("w1"), pcaOutput, lstmNh);
        float[][] w2 = arrayCutter2D(arrayFactory.get2DFloats("w2"), pcaOutput, lstmNh);
        float[][] w3 = arrayCutter2D(arrayFactory.get2DFloats("w3"), pcaOutput, lstmNh);

        float[][] u0 = arrayCutter2D(arrayFactory.get2DFloats("u0"), lstmNh, lstmNh);
        float[][] u1 = arrayCutter2D(arrayFactory.get2DFloats("u1"), lstmNh, lstmNh);
        float[][] u2 = arrayCutter2D(arrayFactory.get2DFloats("u2"), lstmNh, lstmNh);
        float[][] u3 = arrayCutter2D(arrayFactory.get2DFloats("u3"), lstmNh, lstmNh);

        float[] b0 = arrayCutter1D(arrayFactory.get1DFloats("b0"), lstmNh);
        float[] b1 = arrayCutter1D(arrayFactory.get1DFloats("b1"), lstmNh);
        float[] b2 = arrayCutter1D(arrayFactory.get1DFloats("b2"), lstmNh);
        float[] b3 = arrayCutter1D(arrayFactory.get1DFloats("b3"), lstmNh);
        float[] b4 = arrayCutter1D(arrayFactory.get1DFloats("fully_connected_b"), 7);

        float[] c = arrayCutter1D(arrayFactory.get1DFloats("c"), lstmNh);
        float[] h = arrayCutter1D(arrayFactory.get1DFloats("h"), lstmNh);

        //fully connected
        float[][] fullyConnected = arrayCutter2D(
                arrayFactory.get2DFloats("fully_connected_w"), lstmNh, 7);

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

            /*
            ********************************************************************
            ****************************   LSTM start  *************************
            ********************************************************************
             */
            long lstmStart = System.currentTimeMillis();

            for (int l = 0; l < lstmDepth; l++) {
                float[] i = tanHEval(sum3vector(
                        crossInRange(x, w0, l * lstmWidth, (l + 1) * lstmWidth),
                        cross(h, u0),
                        b0)
                );

                c = sum2Vector(
                        dot(
                                sigmoid(sum3vector(
                                        crossInRange(x, w1, l * lstmWidth,
                                                (l + 1) * lstmWidth),
                                        cross(h, u1),
                                        b1)
                                ),
                                i
                        ),
                        dot(
                                sigmoid(sum3vector(
                                        crossInRange(x, w2, l * lstmWidth,
                                                (l + 1) * lstmWidth),
                                        cross(h, u2),
                                        b2)
                                ),
                                c
                        )
                );
                h = dot(
                        sigmoid(
                                sum3vector(
                                        crossInRange(x, w3, l * lstmWidth,
                                                (l + 1) * lstmWidth),
                                        cross(h, u3),
                                        b3)
                        ),
                        tanHEval(c)
                );

            }

            sum2Vector(cross(h, fullyConnected), b4);//it's the fully connected layer

            long lstmEnd = System.currentTimeMillis();
            lstmTimes[index] = lstmEnd - lstmStart;

            Log.d("times", "For the " + String.valueOf(index) + " time.");
            try {
                if (index != 8)
                    Thread.sleep(0);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        for (int i = 0; i < 9; i++) {
            Log.d("prof.Hashemi", "lstm time is:" + lstmTimes[i] +
                    "\npca time is: " + pcaTimes[i] + "\nwavelet time is: " +
                    waveletTimes[i]);
        }

        Arrays.sort(lstmTimes);
        Arrays.sort(pcaTimes);
        Arrays.sort(waveletTimes);

//        mTextView.setText("lstm time is:" + lstmTimes[4] + "\npca time is: " +
//                pcaTimes[4] + "\n wavelet time is: " + waveletTimes[4]);

        Log.w("***", "lstm time is:" + lstmTimes[4] + "\npca time is: " +
                pcaTimes[4] + "\nwavelet time is: " + waveletTimes[4]);

    }
}
