package Server;

public class Policeman extends Human{

    public int id;

    public void getState(int number){
        System.out.println("У " + getClass().getName() + (number+1) + " состояние " + stateEnum);
    }
}