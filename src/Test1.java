import static org.junit.Assert.*;

import org.junit.Test;


public class Test1 {

	@org.junit.Test
	public void testGetDistance1() {
	  //Normal case
       MyGene a = new MyGene("CGCCAAGAGAATCAAAGACGTGGCGTACGTTACACTCGGAACATCTGAGGGAAACTCCTGACGTTAGACTTGAG");
	   MyGene b = new MyGene("CTCCCCCTGGGGATTAAACGTACCTAATCACGACTGTAGGACCATAGGCGCTCACGCCCTTTAGTTACTGGGAG");
	   int x = a.getDistance(b);
	   assertEquals(x,2207);
	  
	}
	
	@org.junit.Test
	public void testGetDistance2() {
		//Normal case with different lengths 
	     MyGene a = new MyGene("CGCCAAGAGAATCAAAGACGTGGCGCGCCTGTACGTTACACTCGGAACATCTGAGGGAAACTCCTGACGTTAGACTTGAG");
		 MyGene b = new MyGene("CGGAATAATACTTTTACCGTCCAAGCGCTATACTTATCGAATCGAGAGCACGACCAAATCAACCCTAAGGTTGATATCGATAGAGCCGATGTCCAAGGGATCGTCGGAGAGGCAACG");
		 int x = a.getDistance(b);
		 assertEquals(x,1956);
	}
	
	@org.junit.Test
	public void testGetDistance3() {
		//Normal case with self gene, should return a distance of 0 
	     MyGene a = new MyGene("CGCCAAGAGAATCAAAGACGTGGCGCGCCTGTACGTTACACTCGGAACATCTGAGGGAAACTCCTGACGTTAGACTTGAG");
		 int x = a.getDistance(a);
		 assertEquals(x,0);
	}
	
	@org.junit.Test
	public void testGetDistance4() {
		//Normal case with two same gene with length 1, should return a distance of 0 
	     MyGene a = new MyGene("G"); 
	     MyGene b = new MyGene("G"); 
		 int x = a.getDistance(b);
		 assertEquals(x,0);
	}
	
	@org.junit.Test
	public void testGetDistance5() {
		//Normal case with two genes with no match; 
	     MyGene a = new MyGene("ATC"); 
	     MyGene b = new MyGene("GCT"); 
		 int x = a.getDistance(b);
		 assertEquals(x,80);
	}
	
	@org.junit.Test
	public void testGetDistance6() {
		//Normal case with two genes with everything matched; 
	     MyGene a = new MyGene("ATCG"); 
	     MyGene b = new MyGene("ATCG"); 
		 int x = a.getDistance(b);
		 assertEquals(x,0);
}

}
