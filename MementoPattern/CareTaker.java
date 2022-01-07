package MementoPattern;

import java.util.ArrayList;
import java.util.List;

public class CareTaker {
    private List<MementoMori> mementoList = new ArrayList<MementoMori>();

    public void add(MementoMori state){
        mementoList.add(state);
    }

    public MementoMori get(int index){
        return mementoList.get(index);
    }
}