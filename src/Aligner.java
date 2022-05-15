
import java.util.function.BiFunction;


public class Aligner {
    BiFunction <Character, Character, Float> alpha;
    float delta;
    float[][] A;
    String s1, s2;
    StringBuilder s1backwards, s2backwards;

    /**
     * Constructor to create an Aligner object
     * @param comparator the BiFunction to calculate alpha, the cost of a match
     * @param delta the cost of a gap
     */
    public Aligner(BiFunction <Character, Character, Float> comparator, float delta) {
        this.alpha = comparator;
        this.delta = delta;
        this.s1backwards = new StringBuilder();
        this.s2backwards = new StringBuilder();
    }

    /**
     * Function to take two strings as arguments and return the optimal cost of alignment for these strings
     * @param s1 the first string to be aligned
     * @param s2 the second string to be aligned
     * @return the minimum cost for the alignment of s1 and s2
     */
    public float align(String s1, String s2) {
        int rowLen, columnLen;
        String alignment;

        // initialize fields and variables
        this.s1 = s1;
        this.s2 = s2;
        rowLen = this.s1.length() + 1;
        columnLen = this.s2.length() + 1;
        A = new float[rowLen][columnLen];

        // fill in memoized array in nested for loops and calling opt() on each entry
        for (int i = 0; i < rowLen; ++i) {
            for (int j = 0; j < columnLen; ++j) {
                // use indices i-1 and j-1 to keep indices within range of the strings' indices
                this.A[i][j] = opt(i-1, j-1);
            }
        }

        // trace backwards through the memoized array to construct the optimal alignment of the strings
        // use indices i = rowLen-2 and j = columnLen - 1 to keep indices within range of the strings' indices
        alignment = traceback(rowLen-2, columnLen-2);

        // print out the optimal alignment
        System.out.println(alignment);

        // return the cost of the optimal alignment
        return A[rowLen-1][columnLen-1];
    }

    /**
     * Function to apply the recurrence relation for the Array[i][j] entry of the memoized array
     * @param i the index for row of the memoized array A
     * @param j the index for the column of the memoized array A
     * @return the value to go in the A[i][j] entry of the memoized array after applying recurrence relation
     */
    private float opt(int i, int j) {
        float left, above, diagonal;
        float match, s2gap, s1gap;

        // base cases: matching a gap with a letter in the other string
        //  *** note: i or j will be zero since when opt is called -1 is subtracted from i and j ***
        if (i == -1) {
            return delta * (j+1);
        } else if (j == -1) {
            return delta * (i+1);
        }

        // store the surrounding entries as variables to avoid calling opt again on costs already in the array
        left = this.A[i+1][j];
        above = this.A[i][j+1];
        diagonal = this.A[i][j];

        // calculate each part of the recurrence relation
        match =  this.alpha.apply(s1.charAt(i), s2.charAt(j)) + diagonal;
        s2gap = this.delta + above;
        s1gap = this.delta + left;

        // return the min value of the recurrence relation so that it's filled in entry A[i][j] of the array
        return Math.min(Math.min(match, s2gap), s1gap);
    }

    /**
     * Function to unwind the memoized array to reproduce the optimal alignment of s1 and s2
     * @param i the index for row of the memoized array A
     * @param j the index for column of the memoized array A
     * @return the output string, showing the optimal alignment of s1 and s2
     */
    private String traceback(int i, int j) {
        float curr, left, above, diagonal;
        String[] unwound;
        String output;

        // initialize variables
        unwound = new String[2];
        output = "";

        // if the strings aren't the same length, this will fill dashes in when one string has been used completely
        // but the other string has characters left
        if (i == -1 && j != -1) {
            this.s1backwards.append('-');
            this.s2backwards.append(s2.charAt(j));
            --j;
            traceback(i, j);
        } else if (i != -1 && j == -1) {
            this.s1backwards.append(s1.charAt(i));
            this.s2backwards.append('-');
            --i;
            traceback(i, j);
        }

        // if both indices aren't -1, then there's still entries in the array to trace through
        if (i != -1 && j != -1) {
            // initialize variables to store entries relative to the current indices being checked
            curr = this.A[i+1][j+1];
            left = this.A[i+1][j];
            above = this.A[i][j+1];
            diagonal = this.A[i][j];
        // else: we are done; return the alignment
        } else {
            return output;
        }

        // if the characters at this entry of the array are the same, then they match
        if (s1.charAt(i) == s2.charAt(j)) {
            this.s1backwards.append(s1.charAt(i));
            this.s2backwards.append(s2.charAt(j));
            --i;
            --j;
            traceback(i, j);
        // if the diagonal entry plus the cost of alpha equals the curr entry, the optimal solution has these letters matched
        } else if (diagonal + alpha.apply(s1.charAt(i), s2.charAt(j)) == curr) {
            this.s1backwards.append(s1.charAt(i));
            this.s2backwards.append(s2.charAt(j));
            --i;
            --j;
            traceback(i, j);
        // if the above entry plus the cost of delta equals the curr entry, the optimal solution has this letter of s1 matched with a gap
        } else if (above + delta == curr) {
            this.s1backwards.append(s1.charAt(i));
            this.s2backwards.append('-');
            --i;
            traceback(i, j);
        // if the left entry plus the cost of delta equals the curr entry, the optimal solution has this letter of s2 matched with a gap
        } else if (left + delta == curr) {
            this.s1backwards.append('-');
            this.s2backwards.append(s2.charAt(j));
            --j;
            traceback(i, j);
        }

        // assign the elements of the unwound array as the backward strings
        unwound[0] = String.valueOf(this.s1backwards);
        unwound[1] = String.valueOf(this.s2backwards);

        // construct the output string; reverse the strings in the unwound array
        for (int m = unwound[0].length()-1; m >= 0; --m) {
            output += unwound[0].charAt(m);
        }
        output += '\n';
        for (int m = unwound[1].length()-1; m >= 0; --m) {
            output += unwound[1].charAt(m);
        }

        // return the output string
        return output;
    }
}
