package MementoPattern;

public class Originator {
    private Object state;

    public void setState(Object state){
        this.state = state;
    }

    public Object getState(){
        return state;
    }

    public MementoMori saveStateToMemento(){
        return new MementoMori(state);
    }

    public void getStateFromMemento(MementoMori memento){
        state = memento.getState();
    }
}
