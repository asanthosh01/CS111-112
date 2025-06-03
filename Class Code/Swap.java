public class Swap {
 //   public static void swap(int[] arr, int index1, int index2) {
 //       int temp = arr[index1];
 //       arr[index1] = arr[index2];
 //       arr[index2] = temp;
//    }
// }

    public static int max(int[] arr) {
        int maxVal = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > maxVal) {
                maxVal = arr[i];
            }
        }
        return maxVal;
    }
}