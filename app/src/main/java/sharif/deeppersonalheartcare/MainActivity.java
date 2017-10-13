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

    private String message;
    private Random random = new Random();

    private int pcaInputCalculator(int rawDownSample, int waveletDownSample, int waveletOmit) {

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

    private float[] dot(float[] in1, float[] in2) {

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

    private float[] sum3vector(float[] in1, float[] in2, float[] in3) {

        for (int i = 0; i < in1.length; i++) {
            in1[i] = in1[i] + in2[i] + in3[i];
        }

        return in1;
    }

    private float[] sum2Vector(float[] in1, float[] in2) {

        for (int i = 0; i < in1.length; i++) {
            in1[i] = in1[i] + in2[i];
        }

        return in1;
    }

    private float[] tanHEval(float[] in) {

        for (int i = 0; i < in.length; i++) {
            in[i] = (float) Math.tanh(in[i]);
        }

        return in;
    }

    private float[] sigmoid(float[] in) {
        for (int i = 0; i < in.length; i++) {
            in[i] = (float) (1 / (1 + Math.exp(-1 * in[i])));
        }
        return in;
    }

    private float[][] arrayCutter2D(float[][] input, int x, int y) {
        float[][] output = new float[x][y];

        for (int i = 0; i < x; i++) {
            System.arraycopy(input[i], 0, output[i], 0, y);
        }
        return output;
    }

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

    private float[] newCrossInRange(float[] in1, float[][] in2, int j1, int j2) {

        int n = in2.length;
        float[] res = new float[n];

        for (int i = 0; i < n; i++) {
            for (int j = j1; j < j2; j++) {
                res[i] += in2[i][j] * in1[j];
            }
        }
        return res;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.round_activity_main);
        TextView mTextView = findViewById(R.id.text);

        Context context = getApplicationContext();
        List<ApplicationInfo> packages;
        PackageManager pm = getPackageManager();
        packages = pm.getInstalledApplications(0);

        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        String myPackage = getApplicationContext().getPackageName();

        for (ApplicationInfo packageInfo : packages) {
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) continue;
            if (packageInfo.packageName.equals(myPackage)) continue;
            mActivityManager.killBackgroundProcesses(packageInfo.packageName);
        }

        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {

                double[] allPcaTime = new double[9];
                double[] allWaveletTime = new double[9];
                double[] allLstmTime = new double[9];

                ArraysFactory arrayFactory = new ArraysFactory(getApplicationContext());

                final int rawDownSample = 1;
                final int waveletDownSample = 1;
                final int waveletOmit = 0;
                final int pcaInputDim = pcaInputCalculator(
                        rawDownSample, waveletDownSample, waveletOmit);
                final int pcaOutputDim = 600;

                final int lstmDepth = 4;
                final int lstmNh = 30;

                final int lstmWidth = pcaOutputDim / lstmDepth;

                float[][] w0 = transposeMatrix(arrayCutter2D(
                        arrayFactory.get2DFloats("w0"), pcaOutputDim, lstmNh));

                float[][] w1 = transposeMatrix(arrayCutter2D(
                        arrayFactory.get2DFloats("w1"), pcaOutputDim, lstmNh));

                float[][] w2 = transposeMatrix(arrayCutter2D(
                        arrayFactory.get2DFloats("w2"), pcaOutputDim, lstmNh));

                float[][] w3 = transposeMatrix(arrayCutter2D(
                        arrayFactory.get2DFloats("w3"), pcaOutputDim, lstmNh));

                float[][] u0 = transposeMatrix(arrayCutter2D(
                        arrayFactory.get2DFloats("u0"), lstmNh, lstmNh));

                float[][] u1 = transposeMatrix(arrayCutter2D(
                        arrayFactory.get2DFloats("u1"), lstmNh, lstmNh));

                float[][] u2 = transposeMatrix(arrayCutter2D(
                        arrayFactory.get2DFloats("u2"), lstmNh, lstmNh));

                float[][] u3 = transposeMatrix(arrayCutter2D(
                        arrayFactory.get2DFloats("u3"), lstmNh, lstmNh));

                float[] b0 = arrayCutter1D(arrayFactory.get1DFloats("b0"), lstmNh);
                float[] b1 = arrayCutter1D(arrayFactory.get1DFloats("b1"), lstmNh);
                float[] b2 = arrayCutter1D(arrayFactory.get1DFloats("b2"), lstmNh);
                float[] b3 = arrayCutter1D(arrayFactory.get1DFloats("b3"), lstmNh);
                float[] fullyConnectedB = arrayCutter1D(
                        arrayFactory.get1DFloats("fully_connected_b"), 7);

                float[] c = arrayCutter1D(arrayFactory.get1DFloats("c"), lstmNh);
                float[] h = arrayCutter1D(arrayFactory.get1DFloats("h"), lstmNh);

                //fully connected
                float[][] fullyConnected = transposeMatrix(arrayCutter2D(
                        arrayFactory.get2DFloats("fully_connected_w"), lstmNh, 7));

                //according to the paper this is the lstm input and it's name must be x.
                float[] x = arrayCutter1D(arrayFactory.get1DFloats("x"), pcaOutputDim);


                float[] firstRawInput = arrayFactory.get1DFloats("first_raw_input");
                float[] firstFeature = arrayFactory.get1DFloats("first_feature");
                float[] secondRawInput = arrayFactory.get1DFloats("second_raw_input");
                float[] secondFeature = arrayFactory.get1DFloats("second_feature");


                float[] highPassFilter = arrayFactory.get1DFloats("high_pass_filter");
                float[] lowPassFilter = arrayFactory.get1DFloats("low_pass_filter");

                float[][] tempPca = new float[pcaInputDim][pcaOutputDim];
                float[][] pca = transposeMatrix(randomInit2D(tempPca));


                for (int index = 0; index < 9; index++) {

                    /*
                    ********************************************************************
                    *****************************   wavelet start  *********************
                    ********************************************************************
                     */
                    double waveletStart = System.currentTimeMillis();

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
                            secondFeature), pcaInputDim);

                    double waveletEnd = System.currentTimeMillis();
                    double waveletTotalTime = waveletEnd - waveletStart;
                    allWaveletTime[index] = waveletTotalTime;

                    /*
                    ********************************************************************
                    *****************************   pca start  *************************
                    ********************************************************************
                     */
                    long crossStartTime = System.currentTimeMillis();

                    newCross(x1, pca);

                    long crossEndTime = System.currentTimeMillis();
                    allPcaTime[index] = crossEndTime - crossStartTime;

                    /*
                    ********************************************************************
                    *****************************   Lstm start  ************************
                    ********************************************************************
                     */
                    long lstmStart = System.currentTimeMillis();
                    for (int l = 0; l < lstmDepth; l++) {

                        c = sum2Vector(
                                dot(
                                        sigmoid(sum3vector(
                                                newCrossInRange(x, w1,
                                                        l * lstmWidth, (l + 1) * lstmWidth),
                                                newCross(h, u1),
                                                b1)
                                        ),
                                        tanHEval(sum3vector(
                                                newCrossInRange(x, w0,
                                                        l * lstmWidth, (l + 1) * lstmWidth),
                                                newCross(h, u0),
                                                b0)
                                        )
                                ),
                                dot(
                                        sigmoid(sum3vector(
                                                newCrossInRange(x, w2,
                                                        l * lstmWidth, (l + 1) * lstmWidth),
                                                newCross(h, u2),
                                                b2)
                                        ),
                                        c
                                )
                        );
                        h = dot(
                                sigmoid(
                                        sum3vector(
                                                newCrossInRange(x, w3, l * lstmWidth,
                                                        (l + 1) * lstmWidth),
                                                newCross(h, u3),
                                                b3)
                                ),
                                tanHEval(c)
                        );

                    }

                    //it's the fully connected layer
                    sum2Vector(newCross(h, fullyConnected), fullyConnectedB);

                    long lstmEnd = System.currentTimeMillis();
                    allLstmTime[index] = lstmEnd - lstmStart;

                }

                Arrays.sort(allWaveletTime);
                Arrays.sort(allPcaTime);
                Arrays.sort(allLstmTime);

                double totalTime = allWaveletTime[4] + allPcaTime[4] + allLstmTime[4];

                message = "execution time is: " + totalTime + "\t the lstm time is:" + allLstmTime[4];

            }
        });

        try {
            backgroundThread.start();
            backgroundThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mTextView.setText(message);
        Log.d("TIME", message);
    }
}