package sharif.deeppersonalheartcare.arrays.arrayFactory;

import android.content.Context;

import java.io.InputStream;
import java.io.ObjectInputStream;


public class ArraysFactory {

    private Context context;
    public ArraysFactory(Context context){
        this.context = context;
    }

    public float[][] get2DFloats(String fileName) {

        try {

            InputStream inputStream = context.getResources().openRawResource(context.getResources().getIdentifier(fileName, "raw", context.getPackageName()));
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            return (float[][]) objectInputStream.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    public float[] get1DFloats(String fileName) {

        try {
            InputStream inputStream = context.getResources().openRawResource(context.getResources().getIdentifier(fileName, "raw", context.getPackageName()));
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            return (float[]) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }
}
