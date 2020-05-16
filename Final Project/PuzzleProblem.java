import java.util.ArrayList;
import java.util.Arrays;

public class PuzzleProblem extends Problem { 

    // Fields 
    private int[] target;
    private int[] currentGame;

    // Constructor 
    public PuzzleProblem(int[] target, int[] currentGame) {
        this.target = target;
        this.currentGame = currentGame;
    }
 
    /**Fetches the start state, which here is this.currentGame.
    We pass in our start state "board" in the SlidePuzzle Class 
    **/
    public State getStartState () {  
        return new PuzzleProblemState(this.target, this.currentGame);
    } 
    
    /** Checks if the target array, which is the sorted puzzle, 
    is equal to the given "board" that we pass in at SlidePuzzle
    **/
    public boolean isGoal(State s) { 
        if (Arrays.equals(((PuzzleProblemState)s).target, ((PuzzleProblemState)s).currentArray)) {
            return true;
        }
        else {
            return false;
        }
    }
    
}


class PuzzleProblemState extends State { 

    // Fields
    int [] target;
    public int [] currentArray; 
    public int index; 

    // Constructor
    public PuzzleProblemState(int [] target, int [] currentArray) {
      this.currentArray = currentArray;
      this.target = target;
    } 

    /** Get possible successor states **/
    public ArrayList<State> getSuccessors(){ 

        for (int k = 0; k < currentArray.length; k++) { 
            if (currentArray[k] == 0)
                index = k;
        } 

        /**
        This code below is creating the, at most, four possible next board configurations given the current board.
        These configurations are moving the empty "White Tile" up, right, down, and left. There is also code here to 
        handle when there the white tile is on an edge and four successors cannot be created.
         **/
        int [] up = new int [16]; 
        if (this.index != 0 && this.index != 1 && this.index != 2 && this.index != 3) {

            for (int i = 0; i < 16; i++) { 
                up[i] = currentArray[i]; 
            }
            
            int temp1 = up[index]; 
            up[index] = up[index - 4];
            up[index - 4] = temp1;
            
        }

        int [] down = new int [16];
        if (this.index != 12 && this.index != 13 && this.index != 14 && this.index != 15) {
            
            for (int j = 0; j < 16; j++){ 
                down[j] = currentArray[j];
            }
            
            int temp2 = down[index];
            down[index] = down[index + 4];
            down[index + 4] = temp2;
            
        }

        int [] left = new int [16]; 
        if (this.index !=0 && this.index != 4 && this.index != 8 && this.index != 12) {
            
            for (int q = 0; q < 16; q++){ 
                left[q] = currentArray[q];
            }
            
            int temp3 = left[index];
            left[index] = left[index - 1];
            left[index - 1] = temp3;
            
        }

        int [] right= new int [16];
        if (this.index != 3 && this.index != 7 && this.index != 11 && this.index != 15) {
            
            for (int d = 0; d < 16; d++){ 
                right[d] = currentArray[d];
            }
            
            int temp4 = right[index];
            right[index] = right[index + 1];
            right[index + 1] = temp4;
            
        }

        // Enter the arrays into the ArrayList of Successors
        ArrayList<State> toR = new ArrayList<State>(); 
        if (isEmpty(right) == false)  {
            
            if (this.index != 3 && this.index != 7 && this.index !=11 && this.index != 15)
                toR.add(new PuzzleProblemState(this.target, right));
            
        }
        if (isEmpty(left) == false) {
            
            if (this.index != 0 && this.index != 4 && this.index != 8 && this.index != 12)
                toR.add(new PuzzleProblemState(this.target, left));
            
        }
        if (isEmpty(up) == false) {
            
            if (this.index != 0 && this.index != 1 && this.index != 2 && this.index != 3)
                toR.add(new PuzzleProblemState(this.target, up));
            
        }
        if (isEmpty(down) == false) {
            
            if (this.index != 12 && this.index != 13 && this.index != 14 && this.index != 15)
                toR.add(new PuzzleProblemState(this.target, down));
            
        }
        
        return toR;
        
    }

    /** Method for printing array **/
    public String toString(){
        return (Arrays.toString(this.currentArray)); 
    }

    /** Checks if the array is empty; another piece of code that handles cases where there are not 4 successors **/
    public boolean isEmpty(int [] a) { 
        
        for (int i = 0; i < a.length; i++) { 
            if (a[i] != 0)
                return false;
        }
        
        return true;
    }
    
}