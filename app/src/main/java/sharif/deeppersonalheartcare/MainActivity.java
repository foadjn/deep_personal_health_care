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

import sharif.deeppersonalheartcare.arrays.arrayFactory.ArraysFactory;

public class MainActivity extends Activity {

    private static String message;

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

    private static float[] sigmoid(float[] in) {
        for (int i = 0; i < in.length; i++) {
            in[i] = (float) (1 / (1 + Math.pow(Math.E, (-1 * in[i]))));
        }
        return in;
    }

    private static float[][] arrayCutter2D(float[][] input, int x, int y) {
        float[][] output = new float[x][y];

        for (int i = 0; i < y; i++) {
            System.arraycopy(input[i], 0, output[i], 0, y);
        }
        return output;
    }

    private static float[] arrayCutter1D(float[] input, int x) {
        float[] output = new float[x];

        System.arraycopy(input, 0, output, 0, x);
        return output;
    }

    public static float[][] transposeMatrix(float[][] m) {

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

    private static float[] newCrossInRange(float[] in1, float[][] in2, int j1, int j2) {

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

                int[] allPcaOutput = new int[]{420};
                int[] allDepth = new int[]{5};
                int[] allNh = new int[]{60};

                for (int i = 0; i < allPcaOutput.length; i++) {


                    int pcaOutput = allPcaOutput[i];
                    int lstmDepth = allDepth[i];
                    int lstmNh = allNh[i];

                    final int lstmWidth = pcaOutput / lstmDepth;

                    ArraysFactory arrayFactory = new ArraysFactory(getApplicationContext());

                    long[] lstmTimes = new long[9];

                    float[][] w0 = transposeMatrix(arrayCutter2D(
                            arrayFactory.get2DFloats("w0"), pcaOutput, lstmNh));

                    float[][] w1 = transposeMatrix(arrayCutter2D(
                            arrayFactory.get2DFloats("w1"), pcaOutput, lstmNh));

                    float[][] w2 = transposeMatrix(arrayCutter2D(
                            arrayFactory.get2DFloats("w2"), pcaOutput, lstmNh));

                    float[][] w3 = transposeMatrix(arrayCutter2D(
                            arrayFactory.get2DFloats("w3"), pcaOutput, lstmNh));

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
                    float[] x = arrayCutter1D(arrayFactory.get1DFloats("x"), pcaOutput);

        /*
        from here the main code start to dot, cross and sum the matrices
         */
                    for (int index = 0; index < 9; index++) {

            /*
            ********************************************************************
            ****************************   LSTM start  *************************
            ********************************************************************
             */
                        long lstmStart = System.currentTimeMillis();

                        for (int l = 0; l < lstmDepth; l++) {


                            c = sum2Vector(
                                    dot(
                                            sigmoid(sum3vector(
                                                    newCrossInRange(x, w1, l * lstmWidth, (l + 1) * lstmWidth),
                                                    newCross(h, u1),
                                                    b1)
                                            ),
                                            tanHEval(sum3vector(
                                                    newCrossInRange(x, w0, l * lstmWidth, (l + 1) * lstmWidth),
                                                    newCross(h, u0),
                                                    b0)
                                            )
                                    ),
                                    dot(
                                            sigmoid(sum3vector(
                                                    newCrossInRange(x, w2, l * lstmWidth, (l + 1) * lstmWidth),
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
                        lstmTimes[index] = lstmEnd - lstmStart;

                    }

                    for (int ii = 0; ii < 9; ii++) {
                        Log.d("prof.Hashemi", "lstm time is:" + lstmTimes[ii]);
                    }

                    Arrays.sort(lstmTimes);

                    Log.d("pof.Hashemi", "nh is: " + lstmNh +
                            "\nLSTM depth is:" + lstmDepth +
                            "\npca output dim is: " + pcaOutput
                    );
                    message = "median of lstm is: " + lstmTimes[4];
                    Log.w("***", "median of lstm is:" + lstmTimes[4]);


                }


            }

        });


        try {

            backgroundThread.start();
            backgroundThread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mTextView.setText(message);

    }
}
