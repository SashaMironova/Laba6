package Server;

import java.util.Comparator;

public class InjuredPolicemanComparator implements Comparator<InjuredPoliceman> {
    @Override
    public int compare(InjuredPoliceman o1, InjuredPoliceman o2){
        return o1.name.compareTo(o2.name);
    }
}

