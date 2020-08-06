package frajhamdoingstuff.epizy.com.fitnesstracker;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class workoutdata {
    private List<String> exerciseNameList = new ArrayList<>();
    private List<exercise> exerciseData = new ArrayList<>();

    private String workoutName = "";

    private transient Activity activity;

    public workoutdata(Activity activity, String name){
        this.activity = activity;
        this.workoutName = name;
    }//Constructor

    public void setActivity(Activity activity){
        this.activity = activity;
    }//Activity cannot be saved (Serialized) so it will be set everytime with this method

    public void whenClicked(){
        for(int i = 0; i < exerciseData.size(); i++){
            createExercise(exerciseData.get(i).getName(), exerciseData.get(i).getSets(), i);
        }
    }//When workout is clicked - This is setup actually

    public void addExercise(){
        //Create input field
        LayoutInflater inflater = activity.getLayoutInflater();
        final View infView = inflater.inflate(R.layout.get_exercise_data, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Exercise");
        builder.setView(infView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Get user input
                String exerciseName = ((EditText)infView.findViewById(R.id.editTextExerciseName)).getText().toString();
                String exerciseSetsString = ((EditText)infView.findViewById(R.id.editTextExerciseSets)).getText().toString();
                int exerciseSets = 0;
                if(!exerciseSetsString.equals(""))
                    exerciseSets = Integer.parseInt(exerciseSetsString);
                if(!exerciseName.equals("") && exerciseSets != 0 && !exerciseNameList.contains(exerciseName)) {
                    exerciseData.add(new exercise(exerciseName, exerciseSets));
                    createExercise(exerciseName, exerciseSets, exerciseData.size()-1);
                    exerciseNameList.add(exerciseName);
                    for(int i = 0; i < exerciseSets; i++){
                        String btnname = exerciseName + i;
                        exerciseData.get(exerciseData.size()-1).addButtonToList(btnname);
                    }
                }
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
    }//When button "Add exercise" is pressed this method is called

    private void createExercise(String exerciseName, int btncount, int index){
        //Find the right layout
        LinearLayout ll = activity.findViewById(R.id.linearLayoutScrollView2);
        //Create new layout for exercise
        LinearLayout newll = new LinearLayout(activity);
        newll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,0,10,40);
        newll.setLayoutParams(params);
        newll.setBackgroundResource(R.drawable.exercise_background);
        newll.setPadding(25,25,25,25);
            //newll.setGravity(Gravity.CENTER);
        //Create text with exercise name in it
        TextView title = new TextView(activity);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        params.setMargins(0,10,0,30);
        title.setLayoutParams(params);
        title.setText(exerciseName);
        Typeface font = ResourcesCompat.getFont(activity, R.font.open_sans);
        title.setTypeface(font);
        title.setTextColor(Color.BLUE);
        title.setTextSize(20);
        title.setGravity(Gravity.CENTER);
        //Add title to display
        newll.addView(title);
        //Create buttons for all sets
        for(int i = 0; i < btncount; i++){
            String btnName = exerciseName + i;
            //Create new layout where buttons and text are placed
            LinearLayout newllbtns = new LinearLayout(activity);
            newllbtns.setOrientation(LinearLayout.HORIZONTAL);
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,30,0,0);
            newllbtns.setLayoutParams(params);
            newllbtns.setGravity(Gravity.START);
            //Button
            Button button = new Button(activity);
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20,0,20,0);
            button.setLayoutParams(params);
            button.setTag(btnName);
            String myString = exerciseData.get(index).reps.get(i).toString() + " | " + exerciseData.get(index).weight.get(i).toString() + "kg";
            button.setText(myString);

            //Add button to display
            newllbtns.addView(button);
            newll.addView(newllbtns);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    editExercise(v);
                }
            });
        }

        //Add entire Layout to display
        ll.addView(newll);
    }//Creates the entire layout for the exercise and reads data from "exercise" object

    private void editExercise(final View v){
        //Create input field
        LayoutInflater inflater = activity.getLayoutInflater();
        final View infView = inflater.inflate(R.layout.edit_exercise_set_data, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Edit set");
        builder.setView(infView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Get user input
                String exerciseRepsS = ((EditText)infView.findViewById(R.id.editTextRepCount)).getText().toString();
                String exerciseWeightS = ((EditText)infView.findViewById(R.id.editTextWeight)).getText().toString();
                int exerciseReps = 0;
                int exerciseWeight = 0;
                if(!exerciseRepsS.equals("") && !exerciseWeightS.equals("")) {
                    exerciseReps = Integer.parseInt(exerciseRepsS);
                    exerciseWeight = Integer.parseInt(exerciseWeightS);
                }
                if(exerciseReps != 0 && exerciseWeight != 0) {
                    for(int i = 0; i < exerciseData.size(); i++){
                        if(exerciseData.get(i).getButtonList().contains(v.getTag())){
                            //Get the index
                            int index = exerciseData.get(i).getButtonList().indexOf(v.getTag());
                            //Update data in list in "exercise" object
                            exerciseData.get(i).setRepsWeight(exerciseReps, exerciseWeight, index);
                            //Set new background color
                            v.setBackgroundColor(0x5500FF00);
                            //Find button that has been clicked by its tag
                            Button button = v.findViewWithTag(v.getTag());
                            //Create text for this button corresponding to the user input
                            String myString = exerciseReps + " | " + exerciseWeight + "kg";
                            //Set text - Show text
                            button.setText(myString);
                            //Break out of the for loop - Performance increase
                            break;
                        }
                    }
                }
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
}