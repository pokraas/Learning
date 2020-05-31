package Week4;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A class to represent a n*n board with sliding tiles, like in the 15-puzzle.
 * @author Alex Pokras
 */
public class Board {
	private final int[][]tiles;
	private int n;
	private int manhattan, hamming; //cached values
	
	/** Create a new board from a n*n array of tiles*/
	public Board(int[][]tiles) {
		this.tiles=tiles;
		n=tiles.length;
		manhattan=-1;
		hamming=-1;
	}
	
	/** number of tiles out of place (Hamming distance)*/
	public int hamming() {
		if (hamming!=-1) return hamming; //return previously computed value
		int h=0;
		for (int i=0;i<n;i++) {
			for (int j=0;j<n;j++) {
				if (tiles[i][j]==0) continue; //disregard Hamming distance for the empty tile
				if (tiles[i][j]!=n*i+j+1) h++;				
			}
		}
		hamming=h;
		return h;
	}
	
	/** In which row should this tile be placed?*/
	private int row(int tile) {
		if (tile==0) return n-1;
		return (tile-1)/n;
	}
	
	/** In which column should this tile be placed?*/
	private int col(int tile) {
		if (tile==0) return n-1;
		return (tile-1)%n;
	}	
	
	/** For a tile at position (row,col) calculates Manhattan distance to its goal position*/
	private int manhattanForTile (int row, int col, int tile) {
		if (tile==0) return 0; //disregard Manhattan distance for the empty tile
		return Math.abs(row-row(tile))+Math.abs(col-col(tile));
	}
	
	/** Sum of Manhattan distances between tiles and their goals*/
	public int manhattan() {
		if (manhattan!=-1) return manhattan; //return previously computed value
		int m=0;
		for (int i=0;i<n;i++) {
			for (int j=0;j<n;j++) {
				m+=manhattanForTile(i,j,tiles[i][j]);
			}
		}
		manhattan=m;
		return m;
	}
	
	/** Is this board the goal board?
	 * (Are all tiles in growing order?)*/
	public boolean isGoal() {
		return hamming()==0;
	}
	
	/** create a new Board with swapped tiles at (row1,col1) and (row2,col2)*/
	private Board swap(int row1, int col1,int row2,int col2) {
		int[][] s = new int[n][n];
		//copy tiles to s
		for (int i=0;i<n;i++) {
			for (int j=0;j<n;j++) {
				s[i][j]=tiles[i][j];
			}
		}
		//swap elements in s
		int temp = s[row1][col1];
		s[row1][col1]=s[row2][col2];
		s[row2][col2]=temp;
		
		return new Board(s);
	}
	
	/** Returns a board obtained by exchanging a pair of tiles. Useful to detect unsolvable boards.*/
	public Board twin() {
		if (tiles[0][0]==0 || tiles[0][1]==0)return swap(1,0,1,1); //swap in the 1st row
		return swap(0,0,0,1); //swap in the 0th row
	}
	
	/** All neighboring boards (obtained via moving a neighboring non-empty tile to blank tile)*/
	public Iterable<Board> neighbors(){//TODO sometimes non-neighboring boards are returned (?)
		ArrayList<Board> a = new ArrayList<Board>();
		int i0=0,j0=0;	//row and column of the empty tile
		//searching for the empty tile
		for (int i=0;i<n;i++) {
			for (int j=0;j<n;j++) {
				if (tiles[i][j]==0) {
					i0=i;
					j0=j;
					break;
				}
			}
		}
		//adding neighbors to the ArrayList
		if (i0>0) a.add(swap(i0,j0,i0-1,j0));
		if (i0<n-1) a.add(swap(i0,j0,i0+1,j0));
		if (j0>0) a.add(swap(i0,j0,i0,j0-1));
		if (j0<n-1) a.add(swap(i0,j0,i0,j0+1));

		return a;
	}
	
	@Override
	public boolean equals (Object other) {
		if (! (other instanceof Board)) return false;
		if (this.n!=((Board)other).n) return false;
		for (int i=0;i<n;i++) {
			for (int j=0;j<n;j++) {
				if (this.tiles[i][j] != ((Board)other).tiles[i][j]) return false;
			}
		}
		return true;
	}
	
	/** Board dimension n*/
	public int dimension() {
		return n;
	}
	
	@Override
	public String toString() {	
		StringBuilder sb = new StringBuilder(Integer.toString(n));
		sb.append('\n');
		for (int i=0;i<n;i++) {
			for (int j=0;j<n;j++) {
				sb.append(tiles[i][j]);	
				sb.append(' ');
			}
			sb.append('\n');
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		int[][] myTiles = {
				{8,1,3},
				{4,0,2},
				{7,6,5}
		};
		Board board = new Board(myTiles);
		int[][] myTiles2 = {
				{0,3,7},
				{4,5,6},
				{2,8,1}
		};
		Board board2 = new Board(myTiles2);
		
		System.out.println(board2.twin().toString());

	}

}
