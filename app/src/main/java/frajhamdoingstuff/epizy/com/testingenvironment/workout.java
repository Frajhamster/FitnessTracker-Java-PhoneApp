package frajhamdoingstuff.epizy.com.testingenvironment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class workout {
    private String name;
    private List<exercise> exercises;

    public workout(String name){
        this.name = name;
        this.exercises = new ArrayList<>();
    }

    public void addExercise(String name){
        this.exercises.add(new exercise(name));
    }
    public void removeExercise(int index){
        this.exercises.remove(index);
    }

    public String getName(){
        return this.name;
    }
    public String getExerciseName(Integer index_e){
        return this.exercises.get(index_e).getName();
    }
    public Integer getExerciseSetCount(Integer index_e){
        return this.exercises.get(index_e).getSetCount();
    }
    public Integer getReps(Integer index_e, Integer index){
        return this.exercises.get(index_e).getReps(index);
    }
    public Integer getWeight(Integer index_e, Integer index){
        return this.exercises.get(index_e).getWeight(index);
    }
    public String getType(Integer index_e, Integer index){
        return this.exercises.get(index_e).getType(index);
    }
    public Integer getExerciseSize(){
        return exercises.size();
    }
    public boolean getIsDone(Integer index_e, Integer index){
        return exercises.get(index_e).getIsDone(index);
    }

    public void setReps(Integer index_e, Integer index, Integer value){
        this.exercises.get(index_e).setReps(index, value);
    }
    public void setWeight(Integer index_e, Integer index, Integer value){
        this.exercises.get(index_e).setWeight(index, value);
    }
    public void setType(Integer index_e, Integer index, String value){
        this.exercises.get(index_e).setType(index, value);
    }
    public void setIsDone(Integer index_e, Integer index, boolean value){
        this.exercises.get(index_e).setIsDone(index, value);
    }

    public void addSet(Integer index_e){
        this.exercises.get(index_e).addSet();
    }
}
