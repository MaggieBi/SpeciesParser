import java.util.*;

/** An instance maintains information about a species. */
public class MySpecies extends Species implements Comparable<MySpecies> {

	private String name; // The species' common name
	private String latinName; // The species scientific name
	private int weight; // The species weight. > 0
	private String collector; // The collector --first person to collect the
								// species
	private String dna; // The species' dna
	private String imageFilename; // Name of the file that contains the image
	private HashSet<Gene> genes; // If not null, contains the set of genes for
									// dna
	private Gene[] allGenes; // stores all possible genes in
								// all species.
	public int[] index; // indices of species.
	public static int[][] gd; // all genes' distance matrix.

	/** Default constructor. Does nothing. */
	public MySpecies() {
	}

	/** Set the Species' common name to n */
	public void setName(String n) {
		name = n;
	}

	/** Return the Species' common name */
	public String getName() {
		return name;
	}

	/** Set the Species' scientific name to n */
	public void setLatinName(String n) {
		latinName = n;
	}

	/** Return the Species' scientific name */
	public String getLatinName() {
		return latinName;
	}

	/**
	 * Set the Species' weight to w. Precondition: w > 0
	 */
	public void setWeight(int w) {
		assert w > 0;
		weight = w;
	}

	/** Return the Species' weight */
	public int getWeight() {
		return weight;
	}

	/** Set the Species' collector to c */
	public void setCollector(String c) {
		collector = c;
	}

	/** Return the Species' collector */
	public String getCollector() {
		return collector;
	}

	/** Set the Species' DNA to dna */
	public void setDNA(String dna) {
		this.dna = dna;
		genes = null;
	}

	/** Return the Species' DNA */
	public String getDNA() {
		return dna;
	}

	/** Set the filename to point to imageFilename */
	public void setImageFilename(String imageFilename) {
		this.imageFilename = imageFilename;
	}

	/** Return the filename pointing to the Species' image */
	public String getImageFilename() {
		return imageFilename;
	}

	/**
	 * Get the Species' genome. A genome is the set of genes parsed from raw
	 * DNA. The return value of getGenome SHOULD NOT contain duplicate genes,
	 * even if genes are duplicated in the raw DNA.
	 * 
	 * You can parse the genome when setDNA is called ("eager"), or when
	 * getGenome is called ("lazy").
	 * 
	 * If the latter, avoid parsing the genome every time getGenome is called.
	 * 
	 * @return A duplicate-free set of genes found in this Species' DNA.
	 */
	public Collection<Gene> getGenome() {
		if (genes != null) {
			return genes;
		}
		MyDNAParser p = new MyDNAParser();
		p.setDNA(dna);
		List<Gene> list = p.parse();
		genes = new HashSet<Gene>(list);
		return genes;
	}

	/**
	 * Return representation of this instance. Contains all attributes --but for
	 * DNA, only the first 30 characters
	 */
	public @Override
	String toString() {
		String d = getDNA();
		if (d != null) {
			d = d.substring(0, Math.min(d.length(), 30));
		}
		return "Name=\"" + getName() + "\"" + "\nLatinName=\"" + getLatinName()
				+ "\"" + "\nImageFilename=\"" + getImageFilename() + "\""
				+ "\nWeight=" + getWeight() + "\nCollected-by=\""
				+ getCollector() + "\"" + "\nDNA=\"" + d + "\"";
	}

	/**
	 * Overrides the compareTo method in Comparable interface compare species
	 * according to their names. Parameter: a MySpecies object
	 */
	@Override
	public int compareTo(MySpecies s) {
		return name.compareTo(s.name);
	}

	/**
	 * Returns the distance of 2 species. one self-species and one species
	 * received from the parameter.
	 * 
	 * The specification in the handout specifies the way to calculate distance
	 * between 2 species.
	 * 
	 * We used a helper method in order to prevent duplication of same codes
	 * twice.
	 * 
	 * Parameter: a MySpecies object.
	 * */
	public int getDistanceTwoSpecies(MySpecies s) {
		// da = compare distance of a to b.
		int da = getDistanceTwoSpeciesHelper(s);
		// db = compare distance of b to a.
		int db = s.getDistanceTwoSpeciesHelper(this);
		// return truncated average.
		return (da + db) / 2;
	}

	/**
	 * Returns distance between two species. Parameter: a MySpecies object.
	 */
	public int getDistanceTwoSpeciesHelper(MySpecies s) {
		// int array with the distance of genes that stores
		// the smallest distance between 2 genes.
		int[] smallDistance = new int[genes.size()];

		int counter1 = 0;
		int counter2 = 0;
		int d = 0; // distance

		// find the distance between one element of A
		// with all elements of B. Do this one by one.
		// and then put all the distances into one int
		// array, sort them, use the first element as
		// the smallest distance.
		for (int x : index) {
			int[] distances = new int[s.index.length];
			for (int y : s.index) {
				distances[counter1] = gd[x][y];
				counter1++;
			}
			Arrays.sort(distances);
			smallDistance[counter2] = distances[0];
			counter2++;
			counter1 = 0;
		}
		// this adds all the smallest distances
		// together to get the distance of
		// A to B.
		for (int z : smallDistance) {
			d = d + z;
		}
		return d; // return distance.
	}

	/**
	 * Sets the gene distance matrix for all genes, which is use in internal
	 * calculation later. Since this is the same for the entire program we make
	 * it a static method. Parameter: A gene array containing all genes
	 */
	public static void gdSetter(Gene[] g) {
		// declare gene distance 2 dimensional
		// array to have the length of gene array.
		gd = new int[g.length][g.length];

		// calculates the top triangle of the matrix,
		// so we dont have duplicate calculation later.
		for (int i = 0; i < g.length; i++) {
			for (int j = i; j < g.length; j++) {
				// sets gd row by row
				// does not calculate the bottom triangle.
				gd[i][j] = ((MyGene) g[i]).getDistance((MyGene) g[j]);
			}
		}
		// sets the bottom triangle using the calculated top triangle.
		for (int k = 1; k < g.length; k++) {
			for (int l = 0; l < k; l++) {
				gd[k][l] = gd[l][k];
			}
		}
	}

	/**
	 * Sets a sorted array of genes into allGenes and finds the indices of genes
	 * within the species in allGenes
	 * 
	 * @param a
	 *            gene array that containing all genes.
	 */
	public void indexDistanceMatrix(Gene[] g) {
		allGenes = g; // set the field allGene to the parameter g.

		Arrays.sort(allGenes); // sort allGene according to alphabetical order.

		// declare an arraylist that contains the Genes of all Genes.
		// We do this because we want to use the indexOf method of arraylist.
		ArrayList<Gene> a = new ArrayList<Gene>(Arrays.asList(allGenes));

		getGenome(); // this sets the gene field, and returns a collection.

		index = new int[genes.size()];// set index array length to genes' size.

		int n = 0;
		for (Gene b : genes) { // for every gene in genes, set the index.
			// find the index of genes, and put in int array.
			index[n] = a.indexOf(b);
			n++;
		}

		Arrays.sort(index); // sorts index.
	}
}
