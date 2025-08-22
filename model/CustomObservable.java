package model;

import java.util.Observer;

public interface CustomObservable {
    void addObserver(CustomObserver observer);
    void removeObserver(CustomObserver observer);
    void notifyObservers(String event, Object data);

}
