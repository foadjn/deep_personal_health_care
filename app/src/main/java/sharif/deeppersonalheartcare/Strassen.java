package sharif.deeppersonalheartcare;


public class Strassen {


    public static float[][] strassen(float[][] a, float[][] b) {
        int n = a.length;
        float[][] result = new float[n][n];

        if ((n % 2 != 0) && (n != 1)) {
            float[][] a1, b1, c1;
            int n1 = n + 1;
            a1 = new float[n1][n1];
            b1 = new float[n1][n1];
            c1 = new float[n1][n1];

            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++) {
                    a1[i][j] = a[i][j];
                    b1[i][j] = b[i][j];
                }
            c1 = strassen(a1, b1);
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    result[i][j] = c1[i][j];
            return result;
        }

        if (n == 1) {
            result[0][0] = a[0][0] * b[0][0];
        } else {
            float[][] A11 = new float[n / 2][n / 2];
            float[][] A12 = new float[n / 2][n / 2];
            float[][] A21 = new float[n / 2][n / 2];
            float[][] A22 = new float[n / 2][n / 2];

            float[][] B11 = new float[n / 2][n / 2];
            float[][] B12 = new float[n / 2][n / 2];
            float[][] B21 = new float[n / 2][n / 2];
            float[][] B22 = new float[n / 2][n / 2];

            divide(a, A11, 0, 0);
            divide(a, A12, 0, n / 2);
            divide(a, A21, n / 2, 0);
            divide(a, A22, n / 2, n / 2);

            divide(b, B11, 0, 0);
            divide(b, B12, 0, n / 2);
            divide(b, B21, n / 2, 0);
            divide(b, B22, n / 2, n / 2);

            float[][] P1 = strassen(add(A11, A22), add(B11, B22));
            float[][] P2 = strassen(add(A21, A22), B11);
            float[][] P3 = strassen(A11, sub(B12, B22));
            float[][] P4 = strassen(A22, sub(B21, B11));
            float[][] P5 = strassen(add(A11, A12), B22);
            float[][] P6 = strassen(sub(A21, A11), add(B11, B12));
            float[][] P7 = strassen(sub(A12, A22), add(B21, B22));

            float[][] C11 = add(sub(add(P1, P4), P5), P7);
            float[][] C12 = add(P3, P5);
            float[][] C21 = add(P2, P4);
            float[][] C22 = add(sub(add(P1, P3), P2), P6);

            copy(C11, result, 0, 0);
            copy(C12, result, 0, n / 2);
            copy(C21, result, n / 2, 0);
            copy(C22, result, n / 2, n / 2);
        }
        return result;
    }

    public static float[][] add(float[][] A, float[][] B) {
        int n = A.length;

        float[][] result = new float[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                result[i][j] = A[i][j] + B[i][j];

        return result;
    }

    public static float[][] sub(float[][] A, float[][] B) {
        int n = A.length;

        float[][] result = new float[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                result[i][j] = A[i][j] - B[i][j];

        return result;
    }

    public static void divide(float[][] p1, float[][] c1, int iB, int jB) {
        for (int i1 = 0, i2 = iB; i1 < c1.length; i1++, i2++)
            for (int j1 = 0, j2 = jB; j1 < c1.length; j1++, j2++) {
                c1[i1][j1] = p1[i2][j2];
            }
    }

    public static void copy(float[][] c1, float[][] p1, int iB, int jB) {
        for (int i1 = 0, i2 = iB; i1 < c1.length; i1++, i2++)
            for (int j1 = 0, j2 = jB; j1 < c1.length; j1++, j2++) {
                p1[i2][j2] = c1[i1][j1];
            }
    }


}
