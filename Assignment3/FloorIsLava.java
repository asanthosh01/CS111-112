
/*************************************************************************
 *  Compilation:  javac FloorIsLava.java
 *  Execution:    java FloorIsLava n
 *
 *  @author Shane Haughton, Maaz Mansuri
 *
 **************************************************************************/

public class FloorIsLava {

    public static void main (String[] args ) {

	int n = Integer.parseInt(args[0]);

    for(int i = 2; i <= n; i += 2) {
        System.out.print(i + " ");
    }
    if (n % 2 == 0) {
        n = n - 1;
    }

    for(int i = n; i >= 1; i -= 2) {
        System.out.print(i + " ");
    }
    System.out.println();
    }
    
}
