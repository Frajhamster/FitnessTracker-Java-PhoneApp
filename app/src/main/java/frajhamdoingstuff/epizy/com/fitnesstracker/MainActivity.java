package frajhamdoingstuff.epizy.com.fitnesstracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.ToggleButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    buttonmenu wcontent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wcontent = new buttonmenu(this);
        //wcontent.saveData();
        wcontent.loadData();

        setButtons();
    }

    public void setButtons(){
        wcontent.setButtonAddWorkout();
        wcontent.setButtonDeleteWorkout();
        wcontent.setButtonBack();
        wcontent.setButtonAddExercise();
        wcontent.setButtonDeleteExercise();
    }
}

//OPEN NEW ACTIVITY
//Intent intent = new Intent(this, AddWorkout.class);
//startActivity(intent);