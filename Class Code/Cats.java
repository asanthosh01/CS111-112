public class Cats {
    public static void main(String[] args) {
        int ellenCats;
        ellenCats = Integer.parseInt(args[0]);

        int anaCats;
        anaCats = Integer.parseInt(args[1]);

        int sumOfCats = ellenCats + anaCats;

        System.out.println(sumOfCats);
    }
}