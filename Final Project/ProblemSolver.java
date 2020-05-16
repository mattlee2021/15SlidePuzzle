import java.util.ArrayList;
import java.util.LinkedList;

public class ProblemSolver{
	Problem p; // Field that will take our PuzzleProblem

	// Constructor 
	public ProblemSolver (Problem p) {  
		this.p = p;
	}

	// Breadth First Search Code
	public static Node BFS(Problem p) {
		System.out.println("Running Breadth First Search");
		return getSolution(p, new MyQueue());
	}

	/** Breadth First Search algorithm; We made some edits so that it goes through 100,000 elements in the pile, 
    then break out of the loop if it doesn't return the answer within that 100,000 times.
    **/
	public static Node getSolution(Problem p, PileLike pile) { 
		
		ArrayList<State> considered = new ArrayList<State>();
		State initialState = p.getStartState();  
		int depth = 0;
		Node.hitdepth = false;

		Node initialNode = new Node(null, initialState);
		if (p.isGoal(initialState)) {
			return initialNode;
		}
		
		pile.push(initialNode);
		considered.add(initialState);
		
		while (!pile.isEmpty()) {
			Node popped = pile.pop();
			ArrayList<State> succs = popped.s.getSuccessors(); 
			depth++;
			
			for (State s : succs) { 
				if (p.isGoal(s)) {
					return new Node(popped, s);
				} 
				else{
					if (!considered.contains(s)) {
						pile.push(new Node(popped, s));
						considered.add(s);  
						if (depth == 100000) {
							break;
						}
					}
				}
			}
		
			if (depth == 100000) { 
				Node.hitdepth = true;
				break;
			}
		}
		return null; // Return null if there is no solution. Since we are producing only solvable boards, null should never be returned 
	}
} 


/** Node Class that holds the current PuzzleProblem State, which has the array representing the puzzle in it. 
The Node parent has the PuzzleProblem State that is before the PuzzleProblem State in this current Node 
**/ 
class Node { 
	
	Node parent;
	State s;
    static boolean hitdepth; // Boolean used to check if 100,000 cases have been tried
    
    public Node(Node parent, State s) {
    	this.parent = parent;
    	this.s = s;
    }
    
    public String toString() {
    	if (this.parent == null){
    		return this.s.toString();
    	}
    	else{
    		return this.parent.toString() + " -> " + this.s.toString();
    	}
    }
    
}

interface PileLike {
	public boolean isEmpty();
	public Node pop();
	public void push(Node s);
}

class MyQueue extends LinkedList<Node> implements PileLike {
	public Node pop() {
		return this.removeLast();
	}
}
