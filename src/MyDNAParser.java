import java.util.*;


public class MyDNAParser implements DNAParser {

    private String dna; // The dna sequence from which to extract genes.

    /** Constructor: an instance with an uninitialized DNA sequence. */
    public MyDNAParser() {
    }
    
    /** Constructor: an instance with DNA sequence dna. */
    public MyDNAParser(String dna) {
        setDNA(dna);
    }


    /** Set the DNA to be parsed to dna 
        @param DNA: A string of amino acids C,T,G,A  */
    public @Override void setDNA(String dna) {
       this.dna= dna;
    }

    /**
     * Return the list of genes that are in the DNA string previously by setDNA().
     * <pre>
     * Rules for parsing the DNA string to find the genes are as follows:
     * 
     * 1. Parse from left to right.
     * 2. In the DNA sequence, a gene is delimited by the start sequence CAG
     *    and end sequence TGC. Therefore, when the start sequence CAG is
     *    encountered, begin a gene, and the next occurrence of the end
     *    sequence TGC terminates the gene.
     * 3. Inside a gene, CAG does not start another gene.
     * 4. The start and end sequences are NOT part of the gene; they
     *       just delimit it.
     * 5. A gene may be empty. If an empty gene is encountered, ignore it --do not add it to the list
     * 6. If there is a start of a gene (CAG) but no following end delimiter (TGC),
     *    the substring beginning at the start is NOT considered to be a gene and
     *    is discarded. In this case, there are no more genes to extract.
     * 7. The DNA string may contain duplicate genes. Function parse
     *    does not remove duplicates from the list it constructs.
     * </pre>
     * @return A list of genes found in the DNA string, in the order in 
     *         which they appear.
     */
    public @Override List<Gene> parse() {
        Vector<Gene> list= new Vector<Gene>();

        String rest= dna;
        int k= rest.indexOf("CAG");
        // inv: rest is a suffix of dna, and all genes of dna before
        //      that suffix are in list.
        //      k = -1 means rest contains no genes
        //      k >= 0 means rest[k..] starts the start delim for the first gene in rest
        while (k >= 0) {
            rest= rest.substring(k+3); // remove stuff before gene, including start delim
            k= rest.indexOf("TGC");
            if (k < 0)
                return list;  // there is no end delimiter. discard rest
            if (k > 0) {      // if gene is nonempty
                try {
                Gene g= new MyGene(rest.substring(0,k));
                list.add(g);
                } catch (IllegalArgumentException e) {
                    // Get here if rest.substring(0,k) was not a
                    // legal gene. There is nothing to do.
                }
            }
            rest= rest.substring(k+3); // remove gene and end delimiter
            
            k= rest.indexOf("CAG");
        }

        return list;
    }

}
