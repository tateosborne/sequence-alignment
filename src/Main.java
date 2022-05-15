import java.util.Set;
import java.util.function.BiFunction;

public class Main {

    /**
     * Function to be called via the BiFunction to calculate the alpha value
     * @param c1 one of the characters to be compared
     * @param c2 the other character to be compared
     * @return the cost of matching c1 with c2
     */
    public static float helper(char c1, char c2) {
        Set<Character> vowels;
        float cost;

        // initialize the set of vowels
        vowels = Set.of('a', 'e', 'i', 'o', 'u');

        // if the characters are identical, the cost is 0.0
        if (c1 == c2) {
            cost = 0.0f;
        // if they're both vowels or both consonants, the cost is 1.0
        } else if ((vowels.contains(c1) && vowels.contains(c2)) || (!vowels.contains(c1) && !vowels.contains(c2))) {
            cost = 1.0f;
        // otherwise, the cost is 3.0
        } else {
            cost = 3.0f;
        }

        // return the cost
        return cost;
    }

    public static void main(String[] argv) {
        testOne();
        System.out.println();
        testTwo();
    }

    public static void testOne() {
        String s1 = "elevenplustwo";
        String s2 = "twelveplusone";
        float delta = 2.0f;
        float cost;

        BiFunction <Character, Character, Float> alpha = Main::helper;

        Aligner aligner = new Aligner(alpha, delta);
        cost = aligner.align(s1, s2);

        System.out.println("\nminimum cost: " + cost);
  }

    public static void testTwo() {
        String s1 = "electionresults";
        String s2 = "liesletsrecount";
        float delta = 2.0f;
        float cost;

        BiFunction <Character, Character, Float> alpha = Main::helper;

        Aligner aligner = new Aligner(alpha, delta);
        cost = aligner.align(s1, s2);

        System.out.println("\nminimum cost: " + cost);
    }

}
