package si.zanozbot.cobisshackathon;

import java.util.ArrayList;

public class DataSingleton {
    public static final DataSingleton instance = new DataSingleton();
    private ArrayList<DataModel> list;

    private DataSingleton() {}

    public void setState(ArrayList<DataModel> state) {
        this.list = state;
    }

    public ArrayList<DataModel> getState() {
        return list;
    }

    public void resetState() {
        this.list = new ArrayList<DataModel>();
    }
}
