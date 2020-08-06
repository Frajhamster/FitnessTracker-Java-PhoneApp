package frajhamdoingstuff.epizy.com.fitnesstracker;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class buttonmenu {
    private List<String> buttonList = new ArrayList<>();
    private List<workoutdata> workoutDataList = new ArrayList<>();

    private Activity activity;
    private LinearLayout sv;

    private Integer workoutIndex;

    private boolean deleteMode = false;

    public buttonmenu(Activity activity){
        this.activity = activity;
        this.sv = activity.findViewById(R.id.linearLayoutScrollView1);
    }

    public void addNewButton(){
        //Create input field
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Workout name");
        final EditText input = new EditText(activity);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Get user input
                String buttonName = input.getText().toString();
                if(buttonList.contains(buttonName)){
                    return;
                }
                //Create button
                createButton(buttonName);
                //Add button to the list
                buttonList.add(buttonName);
                //Save new workout data object
                workoutDataList.add(new workoutdata(activity, buttonName));
                //Save data
                saveData();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //Show input field
        builder.show();
    }

    public void deleteButton(boolean isChecked) {
        if (isChecked)
            this.deleteMode = true;
        else
            this.deleteMode = false;
    }

    public void loadData(){
        //Set up shared preferences
        SharedPreferences sharedPreferences = activity.getSharedPreferences("fitnessTracker_f", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        //Load button list
        String json = sharedPreferences.getString("button list", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        buttonList = gson.fromJson(json, type);
        if (buttonList == null) {
            buttonList = new ArrayList<>();
        }
        else{
            LinearLayout sv = activity.findViewById(R.id.linearLayoutScrollView1);
            for(int i = 0; i < buttonList.size(); i++){
                createButton(buttonList.get(i));
            }
        }
        //Load workout data list
        json = sharedPreferences.getString("workout data list", null);
        type = new TypeToken<ArrayList<workoutdata>>() {}.getType();
        workoutDataList = gson.fromJson(json, type);
        if (workoutDataList == null) {
            workoutDataList = new ArrayList<>();
        }
        else{
            for(int i = 0; i < workoutDataList.size(); i++){
                workoutDataList.get(i).setActivity(activity);
            }
        }
    }

    public void saveData(){
        //Set up shared preferences
        SharedPreferences sharedPreferences = activity.getSharedPreferences("fitnessTracker_f", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        //Save button list
        String json = gson.toJson(this.buttonList);
        editor.putString("button list", json);
        editor.apply();
        //Save workout data list
        String json2 = gson.toJson(this.workoutDataList);
        editor.putString("workout data list", json2);
        editor.apply();
    }

    private void createButton(String buttonName){
        //Create and style new button
        Button button = new Button(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(200,0,200,50);
        button.setLayoutParams(params);
        button.setTag(buttonName);
        button.setText(buttonName);
        button.setBackgroundResource(R.drawable.button_exercise_background);
        //Add button to the layout
        sv.addView(button);
        //OnClick listener for button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete button and workout data
                if(deleteMode){
                    sv.removeViewAt(buttonList.indexOf(v.getTag()));
                    workoutDataList.remove(buttonList.indexOf(v.getTag()));
                    buttonList.remove(v.getTag());
                    saveData();
                }
                //Open clicked workout
                else{
                    //Open new activity with workoutDataList.get(buttonList.indexOf(v.getTag()));
                    activity.findViewById(R.id.mainMenu).setVisibility(View.GONE);
                    activity.findViewById(R.id.workoutMenu).setVisibility(View.VISIBLE);
                    //Save index from workout from list
                    workoutIndex = buttonList.indexOf(v.getTag());
                    //Show all existing exercises
                    workoutDataList.get(workoutIndex).whenClicked();
                }
            }
        });
    }

    public void setButtonAddWorkout(){
        Button buttonAddWorkout = activity.findViewById(R.id.buttonAddWorkout);
        buttonAddWorkout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addNewButton();
            }
        });
    }
    public void setButtonDeleteWorkout(){
        ToggleButton buttonDeleteWorkout = activity.findViewById(R.id.toggleButtonDeleteWorkout);
        buttonDeleteWorkout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                deleteButton(isChecked);
            }
        });
    }
    public void setButtonAddExercise(){
        Button button = activity.findViewById(R.id.buttonAddExercise);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                workoutDataList.get(workoutIndex).addExercise();
            }
        });
    }
    public void setButtonDeleteExercise(){

    }
    public void setButtonBack(){
        Button button = activity.findViewById(R.id.buttonBack);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                activity.findViewById(R.id.mainMenu).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.workoutMenu).setVisibility(View.GONE);
                LinearLayout ll = activity.findViewById(R.id.linearLayoutScrollView2);
                ll.removeAllViews();
                saveData();
            };
        });
    }
}