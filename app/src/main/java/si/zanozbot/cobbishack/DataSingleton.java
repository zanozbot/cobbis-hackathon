package si.zanozbot.cobbishack;

import java.util.ArrayList;

public class DataSingleton {
    public static final DataSingleton instance = new DataSingleton();
    private ArrayList<String> list;

    private DataSingleton() {}

    public void setState(ArrayList<String> state) {
        this.list = state;
    }

    public ArrayList<String> getState() {
        return list;
    }

    public void resetState() {
        this.list = new ArrayList<String>();
    }
}
