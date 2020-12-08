import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class SplayTreeTest {
    public static void main(String[] args) {

        Integer[] arr = new Integer[1000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i+1;
        }
        Collections.shuffle(Arrays.asList(arr));

        List<Long> res = new ArrayList<>();

        for (int i = 0; i< 1000; i++){
            SplayTree<Integer> tree = new SplayTree<Integer>();
            int n = i+1;
            long startTime = System.nanoTime();

            for (int j = 0; j < n; j++){
                tree.add(arr[j]);
            }
            long endTime = System.nanoTime();

            long cur  = endTime - startTime;
            res.add(cur);
        }
        System.out.println(res);
    }

}
