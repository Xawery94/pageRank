import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class PageRank {
    private static int ITERATIONS = 100;
    private static double q = 0.15;
    private double[][] M;

    public static void main(String[] args) {
        PageRank pageRank = new PageRank();
        pageRank.run();
    }

    public void run() {
        double[][] L = new double[10][];
        L[0] = new double[]{0, 1, 1, 0, 1, 0, 0, 0, 0, 0};
        L[1] = new double[]{1, 0, 0, 1, 0, 0, 0, 0, 0, 0};
        L[2] = new double[]{0, 1, 0, 0, 0, 0, 1, 0, 0, 0};
        L[2] = new double[]{0, 1, 0, 0, 0, 0, 1, 0, 0, 0};
        L[3] = new double[]{0, 1, 1, 0, 0, 0, 0, 0, 0, 0};
        L[4] = new double[]{0, 0, 0, 0, 0, 1, 1, 0, 0, 0};
        L[5] = new double[]{0, 0, 0, 0, 0, 0, 1, 1, 0, 0};
        L[6] = new double[]{0, 0, 0, 0, 1, 1, 1, 1, 1, 1};
        L[7] = new double[]{0, 0, 0, 0, 0, 0, 1, 0, 1, 0};
        L[8] = new double[]{0, 0, 0, 0, 0, 0, 1, 0, 0, 1};
        L[9] = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

//        System.out.println("Matrix L (indices)");
//        printMatrix(L);

        M = getM(L);
        System.out.println("Matrix M (indices)");
//        printMatrix(M);

        System.out.println("PAGERANK");
        double[] pr = pageRank(q);
        sortAndPrint(pr);

//        System.out.println("TRUSTRANK (DOCUMENTS 1 AND 2 ARE GOOD)");
        L[0][4] = 0;
        L[2][6] = 0;
        M = getM(L);

        sortAndPrint(trustRank(q));
    }

    private double[][] getM(double[][] L) {
        //TODO 1: Compute stochastic matrix M
        double[][] M = new double[10][10];
        int[] c = new int[10];

        int i = 0;
        while (i < M.length) {
            int j = 0;
            if (j < 10) {
                do {
                    c[i] = (int) (c[i] + L[i][j]);
                    j++;
                } while (j < 10);
            }
            i++;
        }

        int x = 0;
        while (x < M.length) {
            for (int j = 0; j < 10; j++) {
                if (L[x][j] == 1) {
                    M[j][x] = 1.0 / c[x];
                } else {
                    M[j][x] = 0;
                }
            }
            x++;
        }

        return M;
    }

    private double[] pageRank(double q) {
        //TODO 2: compute PageRank with damping factor q (method parameter, by default q value from class constant)
        //return array of PageRank values (indexes: page number - 1, e.g. result[0] = TrustRank of page 1).

        double[] data = new double[10];
        double[] temp = new double[10];

        IntStream.range(0, data.length).forEach(i -> data[i] = 1 / (double) data.length);

        int k = 0;
        while (k < ITERATIONS) {
            int i = 0;
            while (i < data.length) {
                int j = 0;
                while (j < data.length) {
                    temp[i] = temp[i] + (M[i][j] * data[j]);
                    j++;
                }

                temp[i] = q + ((1 - q) * (temp[i]));
                i++;
            }

            double sum = sum(temp);
            IntStream.range(0, data.length).forEach(x -> data[x] = temp[x] / sum);
            k++;
        }

        System.err.println(sum(data));

        return data;
    }

    private double sum(double... values) {
        double result = 0;
        for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
            double value = values[i];
            result += value;
        }
        return result;
    }

    private double[] trustRank(double q) {
        //TODO 3: compute trustrank with damping factor q (method parameter, by default q value from class constant)
        //Documents that are good = 1, 2 (indexes = 0, 1)
        //return array of TrustRank values (indexes: page number - 1, e.g. result[0] = TrustRank of page 1).

        return new double[0];
    }

    private void sortAndPrint(double[] vector) {
        Map<String, Double> map = new HashMap<>();
        for (int i = 0; i < vector.length; i++) {
            map.put((i + 1) + "", vector[i]);
        }

        map.entrySet()
                .stream()
                .sorted(Comparator.comparing(item -> -item.getValue()))
                .forEach(item -> System.out.println(item.getKey() + ": " + item.getValue()));
    }

    private void printMatrix(double[][] L) {
        for (double[] row : L) {
            for (double val : row) {
                System.out.print(val + " ");
            }

            System.out.println();
        }
    }
}
