import java.util.ArrayList;
public abstract class Problem{
    public abstract State getStartState();
    public abstract boolean isGoal(State s);
}
abstract class State{
    public abstract ArrayList<State> getSuccessors();
}
