package frajhamdoingstuff.epizy.com.fitnesstracker;

import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class exercise {
    private List<String> buttonList = new ArrayList<>();

    private String name = "";
    private int sets = 0;
    public List<Integer> reps = new ArrayList<>();
    public List<Integer> weight = new ArrayList<>();

    public exercise(String name, int sets){
        this.name = name;
        this.sets = sets;

        this.setup();
    }

    public String getName(){
        return this.name;
    }
    public int getSets(){
        return this.sets;
    }
    public List<String> getButtonList(){
        return this.buttonList;
    }
    public void addButtonToList(String btnname){
        buttonList.add(btnname);
    }

    private void setup(){
        for(int i = 0; i < this.sets; i++){
            reps.add(0);
            weight.add(0);
        }
    }

    public void setRepsWeight(int reps, int weight, int index){
        this.reps.set(index, reps);
        this.weight.set(index, weight);
    }
}