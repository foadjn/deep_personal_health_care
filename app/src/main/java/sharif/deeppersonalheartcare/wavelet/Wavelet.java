package sharif.deeppersonalheartcare.wavelet;


import sharif.deeppersonalheartcare.convolution.Convolution;

public class Wavelet {

    public float[] wavelet(int ommit, float[] inputSignal, float[] highPassFilter, float[] lowPassFilter) {

        Convolution convolution = new Convolution();

        float[] convolutionWithHighPass1 = convolution.convolution1D(inputSignal, highPassFilter);
        float[] convolutionWithLowPass1 = convolution.convolution1D(inputSignal, lowPassFilter);

        float[] convolutionWithHighPass2 = convolution.convolution1D(convolutionWithLowPass1, highPassFilter);
        float[] convolutionWithLowPass2 = convolution.convolution1D(convolutionWithLowPass1, lowPassFilter);

        float[] convolutionWithHighPass3 = convolution.convolution1D(convolutionWithLowPass2, highPassFilter);
        float[] convolutionWithLowPass3 = convolution.convolution1D(convolutionWithLowPass2, lowPassFilter);

        float[] convolutionWithHighPass4 = convolution.convolution1D(convolutionWithLowPass3, highPassFilter);
        float[] convolutionWithLowPass4 = convolution.convolution1D(convolutionWithLowPass3, lowPassFilter);

        if(ommit == 1){
            convolutionWithHighPass1 = new float[0];
        }
        if(ommit == 2){
            convolutionWithHighPass1 = new float[0];
            convolutionWithHighPass2 = new float[0];
        }

        int size = convolutionWithHighPass1.length + convolutionWithHighPass2.length + convolutionWithHighPass3.length + convolutionWithHighPass4.length + convolutionWithLowPass4.length;
        float[] finalResult = new float[size];

        int index = 0;
        for (float temp : convolutionWithLowPass4) {
            finalResult[index] = temp;
            index++;
        }

        for (float temp : convolutionWithHighPass4) {
            finalResult[index] = temp;
            index++;
        }

        for (float temp : convolutionWithHighPass3) {
            finalResult[index] = temp;
            index++;
        }

        for (float temp : convolutionWithHighPass2) {
            finalResult[index] = temp;
            index++;
        }

        for (float temp : convolutionWithHighPass1) {
            finalResult[index] = temp;
            index++;
        }

        return finalResult;
    }

}
