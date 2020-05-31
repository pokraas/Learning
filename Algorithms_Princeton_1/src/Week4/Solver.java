package Week4;
import java.util.LinkedList;
import java.util.Stack;

import edu.princeton.cs.algs4.MinPQ;

/**
 * A class to solve an n*n slider puzzle (rules like those of the 15-puzzle)
 * @author Alex Pokras
 */
public class Solver {
	
	/**
	 * A search node for the priority queue of operations.<br>
	 * Keeps a board, the number of moves made so far to reach it and a pointer to a parent node.
	 * @author Alex Pokras
	 */
	private class Node implements Comparable<Node> {
		final Board board;
		final int moves;
		final Node parent;
		private int manhattan;
		Node(Board board, int moves, Node parent){
			this.board=board;
			this.moves=moves;
			this.parent=parent;
			this.manhattan=-1;
		}
		
		//cache the value of the Manhattan distance
		public int manhattan() {
			if (this.manhattan==-1) this.manhattan=this.board.manhattan();
			return this.manhattan;
		}
		
		/**
		 * Natural ordering for the A* algorithm based on moves+heuristic function
		 * (either Manhattan distance or Hamming distance)
		 */
		@Override
		public int compareTo(Node other) {
			//manhattan() can be changed to board.hamming()
			return this.moves+this.manhattan()-other.moves-other.manhattan(); 
		}
		
		@Override
		public String toString() {
			return "Node after "+this.moves+" moves:\n"+this.board.toString();
		}
	}
	
	private Node curr;
	private boolean solvable;
	private final Board initial;
	private LinkedList<Board> solution; //cached solution
		
	public Solver(Board initial) {
		if (initial==null) throw new IllegalArgumentException();
		this.initial=initial;
		if (initial.isGoal()) {
			return;
		}
		if (initial.twin().isGoal()) {
			solvable=false;
			return;
		}
		
		MinPQ<Node> pq = new MinPQ<Node>();
		curr = new Node(initial, 0,null);
		pq.insert(curr);
		
		//creating a twin of the current node and A* searching from the twin,
		//because either curr or currTwin (but not both) is always solvable
		MinPQ<Node> pqTwin = new MinPQ<Node>();
		Node currTwin = new Node(initial.twin(),0,null);
		pqTwin.insert(currTwin);
		
		//A* search using the PriorityQueues pq and pqTwin
		while ((!curr.board.isGoal()) && (!currTwin.board.isGoal())) {
			//making the best move from curr
			curr = pq.delMin();

			//adding neighbors of the current board to pq (i.e., making one more move)
			for (Board b : curr.board.neighbors()) {
				//critical optimization: do not include equal boards in pq
				if (curr.parent!=null)
					if (b.equals(curr.parent.board)) continue; 
				//add new node to pq
				pq.insert(new Node(b,curr.moves+1,curr));
			}

			//making the best move from currTwin
			currTwin = pqTwin.delMin();
			//System.out.println(currTwin);
			//adding neighbors of the currTwin's board to pqTwin (i.e., making one more move for the twin)
			for (Board b : currTwin.board.neighbors()) {
				//critical optimization: do not include equal boards in pqTwin
				if (currTwin.parent!=null)
					if (b.equals(currTwin.parent.board)) continue; 
				//add new node to pq
				pqTwin.insert(new Node(b,currTwin.moves+1,currTwin));
			}
		}
		if (currTwin.board.isGoal()) {
			//System.out.println("No solution possible!");
			solvable=false;
		} else solvable=true;
		//System.out.println("Solved: "+curr+solvable);
	}
	
	
	/** sequence of boards in the shortest solution*/
	public Iterable<Board> solution(){
		if (solution!=null) return solution;
		if (!isSolvable()) return null;
		Node curr=this.curr;
		solution = new LinkedList<Board>();
		if (initial.isGoal()) {
			solution.add(initial);
			return solution;
		}
		Stack<Board> stack = new Stack<Board>();
		stack.push(curr.board);
		while (curr.parent!=null) {
			curr=curr.parent;
			stack.push(curr.board);
		}
		while(!stack.isEmpty()) {
			solution.add(stack.pop());
		}
		return solution;
		//TODO sometimes non-neighboring boards are on neighboring indices
	}
	
	public int moves() {//TODO returned value is dependent on what procedures have been called before
		//return -1 if puzzle is unsolvable
		if (initial.isGoal()) return 0;
		if (!isSolvable()) return -1;
		return curr.moves;
	}
	
	public boolean isSolvable() {
		if (initial.isGoal()) return true;
		return solvable;
	}

	public static void main(String[] args) {
		int[][] myTiles = {
				{1,2,3},
				{4,5,6},
				{8,7,0}
		};

		Solver solver = new Solver(new Board(myTiles));
		System.out.println("Solvable in "+solver.moves()+" moves");
		System.out.println(solver.isSolvable()? solver.solution().toString() : "No solution");
	}

}
