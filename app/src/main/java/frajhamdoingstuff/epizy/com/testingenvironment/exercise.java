package frajhamdoingstuff.epizy.com.testingenvironment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class exercise {
    private String name;
    private List<Integer> reps;
    private List<Integer> weight;
    private List<String> type;
    private List<Boolean> isDone;

    public exercise(String name){
        this.name = name;
        this.reps = new ArrayList<>();
        this.weight = new ArrayList<>();
        this.type = new ArrayList<>();
        this.isDone = new ArrayList<>();

        this.setup();
    }
    private void setup(){
        this.reps.add(0);
        this.weight.add(0);
        this.type.add("normal");
        this.isDone.add(false);
    }

    public String getName(){
        return this.name;
    }
    public Integer getReps(Integer index){
        return this.reps.get(index);
    }
    public Integer getWeight(Integer index){
        return this.weight.get(index);
    }
    public String getType(Integer index){
        return this.type.get(index);
    }
    public Integer getSetCount(){
        return this.reps.size();
    }
    public boolean getIsDone(Integer index){
        return this.isDone.get(index);
    }

    public void setReps(Integer index, Integer value){
        this.reps.set(index, value);
    }
    public void setWeight(Integer index, Integer value){
        this.weight.set(index, value);
    }
    public void setType(Integer index, String value){
        this.type.set(index, value);
    }
    public void setIsDone(Integer index, boolean value){
        this.isDone.set(index, value);
    }

    public void addSet(){
        this.reps.add(0);
        this.weight.add(0);
        this.type.add("normal");
        this.isDone.add(false);
    }
}
