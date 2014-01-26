import java.io.*;
import java.util.*;
import java.io.IOException;

/** Class to run assignment A3. */
public class Main {

	/**
	 * Do what is required in the A3 handout.
	 */
	public static void main(String[] args) throws IOException {

		// read all .dat files from SpeciesData
		Species[] species = getSpecies();
		// get all genes from all species
		Gene[] genes = getGenes(species);
		// print all the genes
		printHeader(genes);

		// calculate and print the gene distance matrix
		int[][] geneDistance = distanceMatrix(genes);
		print(geneDistance, 'G');

		// get gd matrix
		MySpecies.gdSetter(genes);

		// print the species's names and it's indices
		int x = 0;
		for (Species a : species) {
			((MySpecies) a).indexDistanceMatrix(genes);
			String s = Arrays.toString(((MySpecies) a).index);
			System.out.println("S" + x + "=" + a.getName() + ":Genes" + s);
			x++;
		}

		// calculate and print the species distance matrix
		int[][] distanceSpeciesMatrix = distanceMatrix(species);
		print(distanceSpeciesMatrix, 'S');

	}

	/** Return distance matrix for species */
	public static int[][] distanceMatrix(Species[] species) {
		// create a 2D array
		int[][] distanceSpeciesMatrix = new int[species.length][species.length];

		// get distances
		for (int x = 0; x < species.length; x++) {
			for (int y = 0; y < species.length; y++) {
				distanceSpeciesMatrix[x][y] = ((MySpecies) species[x])
						.getDistanceTwoSpecies((MySpecies) species[y]);
			}
		}

		return distanceSpeciesMatrix;
	}

	/** Return an array of all Species in SpeciesData. */
	public static Species[] getSpecies() throws IOException {
		// get all the files
		File f = new File("SpeciesData");
		File[] allFiles = f.listFiles();

		ArrayList<Species> a = new ArrayList<Species>();

		MySpeciesReader msr = new MySpeciesReader();

		int x = 0;

		while (x < allFiles.length) {
			String fileName = allFiles[x].getName();

			// read only if it's .dat file
			if (fileName.substring(fileName.length() - 4, fileName.length())
					.equals(".dat") == true) {
				a.add(msr.readSpecies(allFiles[x]));
				x++;
			} else {
				x++;
			}
		}

		Species[] species = new Species[a.size()];
		// put elements in the arraylist into an array
		for (int y = 0; y < species.length; y++) {
			species[y] = a.get(y);
		}

		// sort this array
		Arrays.sort(species);

		return species;
	}

	/**
	 * Return an array of all Species whose names appear in args. The names are
	 * like: SpeciesData/A0.dat
	 */
	public static Species[] getSpecies(String[] args) throws IOException {
		MySpeciesReader msr = new MySpeciesReader();
		Vector<Species> species = new Vector<Species>();
		for (int k = 0; k < args.length; k = k + 1) {
			species.add(msr.readSpecies(args[k]));
		}

		Species[] a = new Species[species.size()];

		for (int k = 0; k < a.length; k = k + 1) {
			a[k] = species.get(k);
		}

		return a;
	}

	/** Return an array of all genes (with no duplicates) in species */
	public static Gene[] getGenes(Species[] species) {
		HashSet<Gene> geneSet = new HashSet<Gene>();
		for (Species sp : species) {
			MyDNAParser p = new MyDNAParser(sp.getDNA());
			List<Gene> geneList = p.parse();
			for (Gene g : geneList) {
				geneSet.add(g);
			}
		}

		Gene[] gArray = new Gene[geneSet.size()];
		int i = 0;
		for (Gene ge : geneSet) {
			gArray[i] = ge;
			i = i + 1;
		}

		Arrays.sort(gArray);
		return gArray;
	}

	/** Compute and return a distance matrix for genes */
	public static int[][] distanceMatrix(Gene[] genes) {
		int[][] matrix = new int[genes.length][genes.length];

		for (int r = 0; r < matrix.length; r = r + 1) {
			// Compute the distances for matrix[r..][r..]
			for (int c = r; c < matrix.length; c = c + 1) {
				int d = ((MyGene) (genes[r])).getDistance(genes[c]);
				matrix[r][c] = d;
				matrix[c][r] = d;
			}
		}
		return matrix;
	}

	/** Print the genes in g, one per line, as described in A2 handout. */
	public static void printHeader(Gene[] genes) {
		for (int r = 0; r < genes.length; r = r + 1) {
			System.out.println("G" + r + "=" + genes[r]);
		}

	}

	/**
	 * Print matrix m, with a header row, as per the A0 handout, with GS being
	 * 'G' or 'S' for gene or species
	 */
	public static void print(int[][] m, char GS) {
		// Print the matrix header
		String row = "";
		for (int r = 0; r < m.length; r = r + 1) {
			if (r < 10)
				row = row + "    " + GS + r;
			else if (r < 100)
				row = row + "   " + GS + r;
			else
				row = row + "  " + GS + r;
		}
		System.out.println(row);

		// Print the matrix
		for (int r = 0; r < m.length; r = r + 1) {
			row = "";
			for (int c = 0; c < m[r].length; c = c + 1) {
				row = row + String.format("%6d", m[r][c]);
			}
			row = row + "     // " + GS + r;
			System.out.println(row);
		}
	}

	/** Return a sorted list genes in hs */
	public static Gene[] getGenes(HashSet<Gene> hs) {
		int n = hs.size(); // The number of genes
		Gene[] geneArray = new Gene[n];
		hs.toArray(geneArray);
		Arrays.sort(geneArray);
		return geneArray;
	}

	/**
	 * Return a list of elements of b, separated by ", " and delimited by [ and
	 * ].
	 */
	public static String toString(int[] b) {
		String res = "[";
		for (int k = 0; k < b.length; k = k + 1) {
			if (k > 0)
				res = res + ", ";
			res = res + b[k];
		}
		return res + "]";
	}

}
