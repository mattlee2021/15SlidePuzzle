import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;

class SlidePuzzle extends JPanel implements ActionListener{

  // Fields
  public final int dimTile = 150;
  public final int tileCount = 15;
  public final int[] board = new int[16];
  public final int l = dimTile * 4;
  public boolean gameOver;
  public int numMoves = 0;
  public int nullTile;

  // Constructor
  public SlidePuzzle() {

    this.setPreferredSize(new Dimension(2 * l + 400, l + 100)); // Set the dimension of the pop-up window
        gameOver = true; // Set gameOver to true initially so we can set up a new game

        // Found sample code on StackOverflow to make button
        JButton hint = new JButton("HINT");
        hint.setPreferredSize(new Dimension(100, 100));
        hint.setFont(new Font("Times", Font.PLAIN, 20));
        this.add(hint);
        hint.addActionListener(this);

        // Add a mouseAdapter in the constructor so we can easily keep track of exactly where is being clicked each time
        addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
            if(gameOver == true) {
              restartGame();
                }
                else {
                  
                  // Get the point that was clicked on
                  // Learned how to do this on Stack Overflow
                  Point pos = e.getPoint();
                    int clickedcol1 = pos.x / dimTile; // The column that was clicked on
                    int clickedrow1 = pos.y / dimTile; // The row that was clicked on

                    // Check that a move can be made at the particular column and row that were clicked on
                    if (canMakeMove(clickedrow1, clickedcol1) == false) {
                      return;
                    }
                                    
                    int blankcol1 = nullTile % 4; // Column of null tile
                    int blankrow1 = nullTile / 4; // Row of null tile

                    int clickedpos = clickedrow1 * 4 + clickedcol1; // Gets the tile that was clicked on

                    // Gets the amount of and direction (positive vs negative) of which way the tile will move
                    int movement = 0;
                    if (clickedrow1 == blankrow1) {
                      if (clickedcol1 > blankcol1)
                        movement = 1;
                        if (blankcol1 > clickedcol1)
                          movement = -1;
                    }
                    else if (clickedcol1 == blankcol1) {
                        if (clickedrow1 > blankrow1)
                            movement = 4;
                        if (blankrow1 > clickedrow1)
                            movement = -4;
                    }

                    // Move null tile to appropriate space when movement has changed
                    // If we do not include this if statement, the puzzle stops working as soon as the use clicks on an invalid space
                    if(movement != 0) {
                      int newNull;
                        do {
                          newNull = movement + nullTile;
                            board[nullTile] = board[newNull];
                            nullTile = newNull;
                        } while (nullTile != clickedpos);
                          board[nullTile] = 0;
                            numMoves++;
                    }
                    gameOver = puzzleComplete(); // Set gameOver to true if the user has solved the puzzle                   
                }
            repaint(); // Updates the board's graphics
          }
        });
        restartGame(); // Sets up the board           
  }

    /**
    Check if the user is making a valid move.
    **/
    public boolean canMakeMove(int row, int column) {
      if(row < 0 || row >= 4 || column < 0 || column >= 4) {
        return false;
        }
        else {
          return true;
        }
    }

    /**
    Reset the board and then randomize it.
    If it is not solvable, try again; Otherwise, keep that random configuration and set gameOver to false.
    **/
    public void restartGame() {
      do {
          for (int i = 0; i < board.length; i++) {
              int reset = (i + 1) % board.length;
              board[i] = reset;
          }
          nullTile = 15;
            randomizeBoard();
      } while (canSolve() == false);
        gameOver = false;
    }

    /**
    Randomize the puzzle configuration
    **/
  public void randomizeBoard() {
    final Random rand = new Random();
      for(int n = 1; n < 15; n++) {
          int random = rand.nextInt(n);
            int temp = board[random];
            board[random] = board[n];
            board[n] = temp;
        }
  }
               
    /**
  Not all random configurations of the fifteen puzzle are solvable.
  In fact, only 50% of configurations are solvable.
  This idea has roots in group theory (group of symmetric permutations of length 16).
  In our case, our puzzle is even (4 x 4 grid), so the puzzle is solvable if either
    - The blank is on an even row counting from the bottom and the number of inversions is odd.
    - Or the blank is on an odd row counting from the bottom and the number of inversions is even.
  We have set up the initial configuration to always have the blank tile in an odd row (the last one).
  This means we want an even number of inversions for the puzzle to be solvable.
  Note that a pair of tiles (a, b) form an inversion if a appears before b but a > b.
  **/
  public boolean canSolve() {
      int inversions = 0;
        for (int i = 0; i < tileCount; i++) {
          for (int j = 0; j < i; j++) {
            if (board[j] > board[i]) {
              inversions++;
              }
          }     
      }
        if(inversions % 2 == 0) {
          return true;
        }
      else {
          return false;
        }
    }
  
    /**
  Checks to see if the puzzle is complete. We do so by making sure the empty tile is in the last position, where it started.
  We also make sure that the tiles are in ascending order from one through fifteen.
  **/
  public boolean puzzleComplete() {
    if (board[tileCount] != 0) {
      return false;
    }
      for(int i = 0; i <= tileCount - 1; i++) {
        if(board[i] != i + 1) {
          return false;
        }
      }
        return true;
    }

    /**
    Implement the graphics of the board.
  **/
  public void drawGrid(Graphics2D g) {
    
      // Loop through each element of the board array, and determine the x and y coordinates
        for (int i = 0; i < board.length; i++) {
          int r = i / 4;
          int c = i % 4;
          int xcoor = c * dimTile;
          int ycoor = r * dimTile;

            // Create the background of the array
          g.setColor(Color.WHITE);
          g.fillRect(xcoor, ycoor, dimTile, dimTile);

          // Creates a dark red color to be used for the tiles
          Color tileColor = new Color (127, 25, 23);

            // For all tiles that are not the null tile, we want to color them the dark red that we specified above
          if (i != nullTile) {
            g.setColor(tileColor);
            g.fillRoundRect(xcoor, ycoor, dimTile, dimTile, 20,20);
          }

          // Color the blank tile white, the same color as the background
          else if (i == nullTile) {
                 g.setColor(Color.WHITE);
                 g.fillRoundRect(xcoor, ycoor, dimTile, dimTile, 20,20);
            }

          // Draw the number onto the tile
          // Learned how to make font from Stack Overflow page
          g.setColor(Color.WHITE);
          Font nums = new Font("ChalkBoard", Font.BOLD, 50);
          g.setFont(nums);
          g.drawString(Integer.toString(board[i]), xcoor+50, ycoor+100);

          // Add a thin black border around the tiles.
          g.setColor(Color.BLACK);
          g.drawRoundRect(xcoor, ycoor, dimTile, dimTile, 20, 20);

        } 
  }

    /**
  Print the number of moves that the user makes onto the screen.
    **/
  public void printnummoves(Graphics2D g) {
      g.setColor(Color.BLACK);
      Font printMoves = new Font("ChalkBoard", Font.BOLD, 50);
      g.setFont(printMoves);
      String printingmoves = "Number of Moves: " + Integer.toString(numMoves);
      g.drawString(printingmoves, 750, 200);
    }

    /**
    Method to implement the graphics changes.
    A lot of understanding of this and repaint came from ntu.edu website and Stack Overflow page about paintComponent
    **/
  public void paintComponent(Graphics g1) { 
    
    super.paintComponent(g1);
    Graphics2D g = (Graphics2D) g1;
    drawGrid(g);

        // While the user is still solving the puzzle, show how many moves they have made so far
      if(gameOver == false) {
        printnummoves(g);
      }

        // When the game is over, print a message and tell the user how many moves it took them to finish
      // Reset numMoves for the next game
      else if(gameOver == true) {
        Font congrats = new Font("ChalkBoard", Font.BOLD, 40);
          g.setFont(congrats);
          String donemessage = "You solved it! Click to play again.";
          String moves = "You finished in " + numMoves + " moves.";
          g.drawString(donemessage, 750, 200);
          g.drawString(moves, 750, 300);
          numMoves = 0;
        }
     }

     /**
     Shows the user a hint if they press the hint button.
     **/
     public String computersolve() { 
       
      int [] target = new int [] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,0}; // Target Array: a completed board 
        boolean depthCapacityReached = false; // Boolean that checks if the BFS has run through 100,000 elements 

        PuzzleProblem problem =new PuzzleProblem(target, board); // Instance of PuzzleProblem class
      ProblemSolver solver = new ProblemSolver(problem); // Instance of ProblemSolver class
              
        // The Node that the BFS returns
      Node temp = solver.BFS(problem); 

      // ArrayList that will store all the hints 
      ArrayList <Node> list = new ArrayList<Node>();

      if (Node.hitdepth == false) { 
        while(temp.parent != null) {
          list.add(temp);
          temp = temp.parent;
        }
      }
      
      else {
        // Error handling that will handle an IndexOutOfBoundsException
          try {
            throw new IndexOutOfBoundsException ("Depth Capacity Reached");
          }
          catch (IndexOutOfBoundsException e) { // Catching once and staying caught 
            System.out.println("Couldn't find the answer yet. Keep playing solving then hit Hint again!");
            depthCapacityReached = true; // This boolean will be set true if there's an IndexOutOfBoundsException 
          }
      } 
  
    if (depthCapacityReached == false && list.size() > 1) { // This code will only run if there is no IndexOutOfBoundsException

      // This code will get the position where the 0 is on the very last Node in the ArrayList that holds the next correct moves
            int topsize = list.size() - 1;
            Node top = list.get(topsize); // This is a node
            State topState = top.parent.s;
            int [] myArray1 = ((PuzzleProblemState)topState).currentArray;
          int indextop = 0;
          
          for (int i = 0; i < myArray1.length; i++) {
            if (myArray1[i] == 0)
              indextop = i;
            }

          // This code will get the position where the 0 is on the second to last Node in the ArrayList that holds the next correct moves
          int undersize = list.size() - 2; 
          Node under = list.get(undersize);
                  State underState = under.parent.s;
                  int [] myArray2 = ((PuzzleProblemState)underState).currentArray;
                  int indexunder = 0;
                  for (int j = 0; j < myArray2.length; j++) {
                    if (myArray2[j] == 0)
                      indexunder = j;
                  }

            /*Finding the difference in the index position between the last Node in the ArrayList and the second to last node
          in the ArrayList. By knowing how the 0 should move, I what next move to make. */
          int wheretomove = indextop - indexunder;

            if (wheretomove == -4) {
              System.out.println("Move White Tile Down");
              return "Move White Tile Down" ;
            }
            else if (wheretomove == 4) {
              System.out.println("Move White Tile up");
              return "Move White Tile Up";
            }
            else if (wheretomove == -1) {
              System.out.println("Move White Tile right");
              return "Move White Tile Right" ;
            }
            else if (wheretomove == 1) {
              System.out.println("Move White Tile left");
              return "Move White Tile Left"; 
            }
            else {
              System.out.println("Error");
              return " Error ";
            }

    }

    /* This code handles the case where there is only one move left to be made (i.e the last move). 
    We can't use the code above because you must have at least two indices left to figure out what the next move is. */ 
    else if (!depthCapacityReached) {  

      int lastsize = list.size() - 1;
      Node last = list.get(lastsize); // this is a node; Only running at a depth of 1000 
      State lastState = last.parent.s;
      int [] myArray3 = ((PuzzleProblemState)lastState).currentArray;
      int indexlast = 0;
      
      for (int z = 0; z < myArray3.length; z++) {
        if (myArray3[z] == 0)
              indexlast = z;
      }

      if (indexlast == 11) {
        System.out.println("Move White Tile Down");
        return "Move White Tile Down";
          }
            else if (indexlast == 14) {
              System.out.println("Move White Tile right");
              return "Move White Tile Right";
            }
            else
              System.out.println("Error");
                return "Error";
    }
    
    else {
      return "Run again later";
    }
    
  }
     
    /**
    Calls computersolve method to get hint for user when the hint button is pressed
    **/
  public void actionPerformed(ActionEvent e) { 
    computersolve();
    }

  /**
  Main method. Creates an instance of the slide puzzle.
  **/
  public static void main(String[] args) {
    JFrame frame = new JFrame("Slide Puzzle!");
        frame.setSize(1000,1000);
        frame.getContentPane().setBackground(Color.RED);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      SlidePuzzle mainInstance = new SlidePuzzle();
      frame.setContentPane(mainInstance);
      frame.pack();
        frame.setVisible(true);
    }

}
