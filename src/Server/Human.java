package Server;

import java.util.ArrayList;

public abstract class Human implements HumanInterface {

    public StateEnum stateEnum = StateEnum.GOOD;

    public ArrayList<BodyPartEnum> bodyPartsEnum = new ArrayList<BodyPartEnum>();

    @Override
    public void goIn(int number) {
        System.out.println(this.getClass().getName() + (number + 1) + " вошел в комнату");
    }

    @Override
    public void goOut() {
        System.out.println(this.getClass().getName() + " вышел из комнаты");
    }
}