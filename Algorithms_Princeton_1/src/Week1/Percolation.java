package Week1;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * A model of a 2D percolation system
 * based on union-find.
 */

public class Percolation {
	@SuppressWarnings("unused")
	private class UnionFind {
		private int[] id;
		private int[] sz;
		
		UnionFind(int n){
			id = new int[n];
			sz = new int[n];
			Arrays.fill(sz, 1);
			for (int i=0; i<n; i++) {
				id[i]=i;
			}
		}
		
		void union(int p,int q){
			int i=root(p);
			int j=root(q);
			if (i==j) return;
			if (sz[i]<sz[j]) {
				id[i]=j;
				sz[j]+=sz[i];
			}
			else {
				id[j]=i;
				sz[i]+=sz[j];
			}
		}
		
		int root (int i) {
			while (id[i] != i) {
				id[i]=id[id[i]];
				i=id[i];
			}
			return i;
		}
		
		boolean find (int p,int q) {
			return root(p)==root(q);
		}
		@Override
		public String toString() {
			return "id"+Arrays.toString(id)+"\nsz"+Arrays.toString(sz);
		}
	}
	
	private boolean[] grid;
	private int n;
	private int openSites;
	private UnionFind uf;
	
	public Percolation (int n) {
		if (n<0) throw new IllegalArgumentException();
		this.n=n;
		grid = new boolean[n*n+2];
		//grid[0] is a pseudopoint connected to all points in the top row
		//grid[1] is a pseudopoint connected to all points in the bottom row
		uf = new UnionFind(n*n+2);
		for (int col=1;col<=n;col++) {
			uf.union(0, getIndex(1,col));
			uf.union(1, getIndex(n,col));
		}
		//System.out.println("Init complete\n"+uf);
	}
	
	/**
	 * Finds an index of the element of the square matrix in a one-dimensional array.
	 * @param row row in matrix
	 * @param col column in matrix
	 * @return index
	 */
	private int getIndex(int row, int col) {
		if (row<1 || col<1 || row>n || col>n) throw new IndexOutOfBoundsException();
		row--;
		col--;
		return 2+row*n+col;
	}
	
	private void connect(int row1, int col1, int row2, int col2) {	
		if (isOpen(row1,col1) && isOpen(row2,col2)) {
			uf.union(getIndex(row1,col1), getIndex(row2, col2));
		}
	}
	
    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
    	if (grid[getIndex(row,col)]) return;
    	grid[getIndex(row,col)]=true;
    	if(row>1) connect(row, col, row-1, col);
    	if (row<n) connect(row, col, row+1, col);
    	if (col>1) connect(row, col, row, col-1);
    	if (col<n) connect(row, col, row, col+1);
    	openSites++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
    	return grid[getIndex(row,col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
    	return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
    	return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
    	return uf.find(0, 1);
    }
    
    @Override
    public String toString() {
    	String ret="";
    	for (int i=1;i<=n;i++) {
    		for (int j=1;j<=n;j++) {
    			char c = '◼';
    			if (isOpen(i,j)) {
    				c='◻';
    				if (uf.find(getIndex(i,j), 0)) c = 'x';
    			}
    			ret+=c;
    		}
    		ret+="\n";
    	}
    	return ret;
    }
	
	public static void main(String[] args) {
//		Percolation p = new Percolation(4);
//		p.open(2,2);
//		p.open(1,1);
//		p.open(2,1);
//		p.open(3,2);
//		p.open(4,2);
//		System.out.println(p);
//		System.out.println(p.uf);
//		if (p.percolates()) {
//			System.out.println("Percolates\n"+p);
//		}
		int size=100;
		int trials = 700;
		double percolationThreshold=0;
		HashMap<Integer,Integer> freqs = new HashMap<Integer, Integer>();
		for (int i=0;i<trials;i++) {
			Percolation bigP = new Percolation(size);
			while(!bigP.percolates() && bigP.numberOfOpenSites()<size*size) {
				bigP.open(new Random().nextInt(size)+1,new Random().nextInt(size)+1);
				//System.out.println("Opened "+bigP.openSites);
			}
			int open = bigP.numberOfOpenSites();
			percolationThreshold+=(double)open/(size*size*trials);
			freqs.put(open,freqs.get(open)==null? 1:freqs.get(open)+1);
		//System.out.println(bigP+"\n"+(double)bigP.numberOfOpenSites()/(size*size));
		//System.out.println(bigP.uf);
		}
		for (int i:freqs.keySet()) {
			//System.out.println(i+"\t"+freqs.get(i));
		}
		System.out.println(percolationThreshold);

	}

}
