package notifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Notifier implements Observable, Runnable {
    public static int DEFAULTDELTA = 1;

    private List<Observer> observers;
    private int delta;// checking current window title time interval (calculates in min)

    private String lastWindowTitle;
    private String currentWindowTitle;
    private boolean needToNotify;

    public Notifier(int delta) {
        observers = new ArrayList<Observer>();
        currentWindowTitle = lastWindowTitle = "None";// getActiveWindowTitle returns formatted title, like: "Window title"|"Program's name"
        needToNotify = false;
        this.delta = delta;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (needToNotify) {
                currentWindowTitle = WindowTitle.getActiveWindowTitle();
                // if no different in title, no need to notify observers
                if (currentWindowTitle.equals(lastWindowTitle)) {
                    notifyObservers();
                }
                lastWindowTitle = currentWindowTitle;
            }
            try {
                Thread.sleep(delta * 1000);// 60000
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        int dashIndex = currentWindowTitle.lastIndexOf('-');
        String programName;
        String windowTitle;

        if (dashIndex < 0) {
            windowTitle = "None";
            programName = currentWindowTitle.length() == 0 ? "None" : currentWindowTitle;
        } else {
            windowTitle = currentWindowTitle.substring(0, dashIndex - 1);// cut from start to space before dash
            programName = currentWindowTitle.substring(dashIndex + 2);// cut from space after dash to end
        }

        Iterator iterator = observers.iterator();
        while (iterator.hasNext()) {
            Observer observer = (Observer) iterator.next();
            observer.update(programName, windowTitle,delta);
        }
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public boolean isNeedToNotify() {
        return needToNotify;
    }

    public void setNeedToNotify(boolean needToNotify) {
        this.needToNotify = needToNotify;
    }
}
