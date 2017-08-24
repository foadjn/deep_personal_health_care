package sharif.deeppersonalheartcare.convolution;

/**
 * Created by Foad Jafari
 */
public class Convolution {

    public float[] convolution1D(float[] inputSignal, float[] kernel) {

        kernel = reverser(kernel);

        float inputLength = inputSignal.length;
        float mirrorPaddingSizeForBeginingOfArray = (float) kernel.length - 2;
        float mirrorPaddingSizeForEndOfArray = (float) kernel.length - 2;

        if(inputLength %2 != 0){
            mirrorPaddingSizeForEndOfArray++;
        }

        float outputLength = (float) Math.ceil(((inputLength - kernel.length + mirrorPaddingSizeForBeginingOfArray + mirrorPaddingSizeForEndOfArray) / 2) + 1);

//        System.out.println((int) outputLength);

        inputSignal = makeMirrorPadding(inputSignal, (int) mirrorPaddingSizeForBeginingOfArray, (int) mirrorPaddingSizeForEndOfArray);
        float[] output = new float[(int) outputLength];

        int index = 0;
        for (int i = 0; i < outputLength * 2; i += 2) {
            output[index] = multiplier(inputSignal, kernel, i);
            index++;
        }

        return output;
    }

    private float[] makeMirrorPadding(float[] inputSignal, int mirrorPaddingSizeForBeginingOfArray, int mirrorPaddingSizeForEndOfArray) {


        int inputLength = inputSignal.length;
        float[] outPutVector = new float[inputLength + mirrorPaddingSizeForBeginingOfArray + mirrorPaddingSizeForEndOfArray];

        int counter = 0;
        for (int index = 0; index < mirrorPaddingSizeForBeginingOfArray; index++) {
            outPutVector[index] = inputSignal[mirrorPaddingSizeForBeginingOfArray - 1 - index];
        }

        for (int index = mirrorPaddingSizeForBeginingOfArray; index < inputLength + mirrorPaddingSizeForBeginingOfArray; index++) {
            outPutVector[index] = inputSignal[counter];
            counter++;
        }

        counter = 1;
        for (int index = inputLength + mirrorPaddingSizeForBeginingOfArray; index < (inputLength + mirrorPaddingSizeForBeginingOfArray + mirrorPaddingSizeForEndOfArray); index++) {
            outPutVector[index] = inputSignal[inputLength - counter];
            counter++;
        }

        return outPutVector;
    }

    private float[] reverser(float[] input){

        float[] output = new float[input.length];

        int kernelSize = input.length;
        for(int index = 0; index < kernelSize; index++){
            output[index] = input[kernelSize - 1 - index];
        }
        return output;
    }

    private float multiplier(float[] inputSignal, float[] kernel, int startIndexOfInput) {

        float sum = 0;
        int kernelIndex = 0;
        for (int index = startIndexOfInput; index < kernel.length + startIndexOfInput; index++, kernelIndex++) {
            sum += inputSignal[index] * kernel[kernelIndex];
        }

        return sum;
    }
}
