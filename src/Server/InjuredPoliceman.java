package Server;

import java.util.Date;

public class InjuredPoliceman extends Policeman {

    public String name;
    public int yearOfBirth;
    public String location;
    Date date = new Date();
    public String dateOfCreation = date.toString();

    public InjuredPoliceman() {
        this.id = this.hashCode();
    }

    public FacePartEnum injuredFacePart;

    public void writeFaceParts(int number){
        System.out.println("У " + getClass().getName() + (number+1) + " пластырь на " + injuredFacePart);
    }

    public void writeBodyParts(int number){
        for (int i = 0; i<bodyPartsEnum.size(); i++) {
            System.out.println("У " + getClass().getName() + (number+1) + " ранена " + bodyPartsEnum.get(i));
        }
    }

    public int getId (){
        return id;
    }
}