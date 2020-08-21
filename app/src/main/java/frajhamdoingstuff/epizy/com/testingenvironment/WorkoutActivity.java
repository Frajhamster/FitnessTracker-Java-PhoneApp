package frajhamdoingstuff.epizy.com.testingenvironment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
	//Program variables
		//Global variables for slider
		private ViewPager mSlideViewPager;
		private SliderAdapter sliderAdapter;
		//Current workout and its index
		private workout cw;
		private Integer cwIndex;
		//Shared workout data
		private List<workout> wkList;
		//Context menu index of selected item
		private Integer indexSelectedContextMenuObject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workout);
		//Get index from selected workout
		Intent intent = getIntent();
		cwIndex = intent.getIntExtra("index", 0);
		//Load data into wkList
		wkList = loadData();
		//Set title
		TextView text = (TextView) findViewById(R.id.activity_workout_name);
		text.setText(wkList.get(cwIndex).getName());

		setSlider();
		setButtons();
		checkIfInProgress();
	}

	private void checkIfInProgress(){
		for(int i = 0; i < wkList.get(cwIndex).getExerciseSize(); i++){
			for(int j = 0; j < wkList.get(cwIndex).getExerciseSetCount(i); j++){
				if(wkList.get(cwIndex).getIsDone(i, j)){
					askIfContinue();
					return;
				}
			}
		}
	}
	private void setSlider(){
		mSlideViewPager = (ViewPager) findViewById(R.id.activity_workout_pagerslider);
		sliderAdapter = new SliderAdapter(this);
		mSlideViewPager.setAdapter(sliderAdapter);

		updateSliderLayout(-1);
	}
	private void setButtons(){
		setButton_return();
		setButton_popupmenu();
	}
	private void setButton_return(){
		Button button = (Button) findViewById(R.id.activity_workout_button_return);
		button.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				saveData(wkList);
				//Switch back to MainProgram.java
				Intent intent = new Intent(WorkoutActivity.this, MainProgram.class);
				startActivity(intent);
			}
		});
	}
	private void setButton_popupmenu(){
		Button button = (Button) findViewById(R.id.activity_workout_button_popupmenu);
		button.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				PopupMenu popup = new PopupMenu(WorkoutActivity.this, view);
				popup.setOnMenuItemClickListener(WorkoutActivity.this);
				popup.inflate(R.menu.activity_workout_popupmenu);
				popup.show();
			}
		});
	}

	private void addSliderLayout(int slider_layout){
		LayoutInflater inflater = this.getLayoutInflater();
		View view = inflater.inflate(slider_layout, null);
		sliderAdapter.addView(view);
		sliderAdapter.notifyDataSetChanged();
	}
	private void addSliderLayout(View view){
		sliderAdapter.addView(view);
		sliderAdapter.notifyDataSetChanged();
	}
	private void addSliderLayout(View view, int position){
		sliderAdapter.addView(view, position);
		sliderAdapter.notifyDataSetChanged();
	}
	private void removeSliderLayout(int position){
		sliderAdapter.removeView(mSlideViewPager, position);
	}
	private void setSliderLayout(int index){
		mSlideViewPager.setCurrentItem(index, true);
	}
	private Integer getSliderIndex(){
		return sliderAdapter.getItemPosition(sliderAdapter.getView(mSlideViewPager.getCurrentItem()));
	}
	private void updateSliderLayout(int setIndex){
		//Remove all views
		sliderAdapter.removeAllViews(mSlideViewPager);
		//If no exercises exist
		if(wkList.get(cwIndex).getExerciseSize() == 0){
			addSliderLayout(R.layout.activity_workout_empty);
		} else{
			//For each exercise
			for(int i = 0; i < wkList.get(cwIndex).getExerciseSize(); i++){
				LayoutInflater inflater = this.getLayoutInflater();
				View view = inflater.inflate(R.layout.activity_workout_exercise, null);
				//Exercise name
				TextView ename = (TextView) view.findViewById(R.id.activity_workout_exercise_name);
				ename.setText(wkList.get(cwIndex).getExerciseName(i));
				//For each set
				for(int j = 0; j < wkList.get(cwIndex).getExerciseSetCount(i); j++){
					//On last set make "add" button visible
					if(j + 1 == wkList.get(cwIndex).getExerciseSetCount(i) && j + 1 < 5){
						String tag = "activity_workout_exercise_add_" + (j + 1);
						Button button = (Button) view.findViewWithTag(tag);
						button.setVisibility(View.VISIBLE);
						button.setOnClickListener(new View.OnClickListener(){
							@Override
							public void onClick(View view){
								Integer index = getSliderIndex();
								wkList.get(cwIndex).addSet(index);
								saveData(wkList);
								updateSliderLayout(getSliderIndex());
							}
						});
					} else if(j + 1 < 5){
						String tag = "activity_workout_exercise_add_" + (j + 1);
						Button button = (Button) view.findViewWithTag(tag);
						button.setVisibility(View.INVISIBLE);
					}
					//Show layout
					String tag = "activity_workout_exercise_set_" + (j + 1);
					ConstraintLayout layout = (ConstraintLayout) view.findViewWithTag(tag);
					layout.setVisibility(View.VISIBLE);

					//Reps button and text --- OnClick
					tag = "button_reps_" + (j + 1);
					Button button = (Button) view.findViewWithTag(tag);
					button.setText(wkList.get(cwIndex).getReps(i, j).toString());
					button.setOnClickListener(new View.OnClickListener(){
						@Override
						public void onClick(View view){
							buttonChangeReps(view);
						}
					});
					//Weight button and text --- OnClick
					tag = "button_weight_" + (j + 1);
					button = (Button) view.findViewWithTag(tag);
					button.setText(wkList.get(cwIndex).getWeight(i, j).toString() + " kg");
					button.setOnClickListener(new View.OnClickListener(){
						@Override
						public void onClick(View view){
							buttonChangeWeight(view);
						}
					});
					//WType button --- OnLongClick contextMenu
					tag = "button_wtype_" + (j + 1);
					button = (Button) view.findViewWithTag(tag);
					setButtonIcon(i, j, button);
					registerForContextMenu(button);

					//Set Textview --- OnClick
					tag = "text_set_" + (j + 1);
					TextView tview = (TextView) view.findViewWithTag(tag);
					updateSetTextview(i, j, tview);
					tview.setOnClickListener(new View.OnClickListener(){
						@Override
						public void onClick(View view){
							changeSetDone(view);
						}
					});
				}
				addSliderLayout(view);
			}
		}
		//Show specific layout
		if(setIndex >= 0)
			setSliderLayout(setIndex);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);

		getMenuInflater().inflate(R.menu.activity_workout_contextmenu_type, menu);

		indexSelectedContextMenuObject = Integer.parseInt(getDigits(v.getTag().toString())) - 1;
	}
	@Override
	public boolean onContextItemSelected(@NonNull MenuItem item){
		switch(item.getItemId()){
			case R.id.activity_workout_type_normal:
				changeButtonIcon("normal");
				return true;
			case R.id.activity_workout_type_cluster:
				changeButtonIcon("cluster");
				return true;
			case R.id.activity_workout_type_drop:
				changeButtonIcon("drop");
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem item){
		switch(item.getItemId()){
			case R.id.activity_workout_popupmenu_addexercise:
				buttonAddExercise();
				return true;

			case R.id.activity_workout_popupmenu_deleteexercise:
				buttonDeleteExercise();
				return true;

			default:
				return false;
		}
	}

	private void buttonAddExercise(){
		//Create builder
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Name your exercise");
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);
		builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String name = input.getText().toString();
				wkList.get(cwIndex).addExercise(name);
				saveData(wkList);

				updateSliderLayout(getSliderIndex() + 1);
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
	}//Adds exercise
	private void buttonDeleteExercise(){
		if(wkList.get(cwIndex).getExerciseSize() > 0){
			//Create builder
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Are you sure about that?");
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Integer index = getSliderIndex();
					wkList.get(cwIndex).removeExercise(index);
					saveData(wkList);

					updateSliderLayout(getSliderIndex() - 1);
				}
			});
			builder.setNegativeButton("Nah", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			//Show input field
			builder.show();
		}
	}//Deletes exercise
	private void buttonChangeReps(final View view){
		//Assign needed index values
		final int index_e = getSliderIndex();
		String sindex = getDigits(view.getTag().toString());
		final int index = Integer.parseInt(sindex) - 1;
		//Create builder
		AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutActivity.this);
		builder.setTitle("Reps");
		final NumberPicker input = new NumberPicker(WorkoutActivity.this);
		input.setMinValue(1);
		input.setMaxValue(20);
		if(wkList.get(cwIndex).getReps(index_e, index) < 1)
			input.setValue(1);
		else
			input.setValue(wkList.get(cwIndex).getReps(index_e, index));
		builder.setView(input);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Integer value = input.getValue();
				wkList.get(cwIndex).setReps(index_e, index, value);
				saveData(wkList);

				updateSliderLayout(getSliderIndex());
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
	}//Set new Rep value
	private void buttonChangeWeight(final View view){
		//Get required index values
		final int index_e = getSliderIndex();
		String sindex = getDigits(view.getTag().toString());
		final int index = Integer.parseInt(sindex) - 1;
		//Create builder
		AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutActivity.this);
		builder.setTitle("Weight");
		final NumberPicker input = new NumberPicker(WorkoutActivity.this);
		input.setMinValue(1);
		input.setMaxValue(500);
		if(wkList.get(cwIndex).getWeight(index_e, index) < 1)
			input.setValue(1);
		else
			input.setValue(wkList.get(cwIndex).getWeight(index_e, index));
		builder.setView(input);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Integer value = input.getValue();
				wkList.get(cwIndex).setWeight(index_e, index, value);
				saveData(wkList);

				updateSliderLayout(getSliderIndex());
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
	}//Set new Weight value



	private void changeButtonIcon(String type){
		if(type.equals("normal")){
			wkList.get(cwIndex).setType(getSliderIndex(), indexSelectedContextMenuObject, "normal");
		}
		else if(type.equals("cluster")){
			wkList.get(cwIndex).setType(getSliderIndex(), indexSelectedContextMenuObject, "cluster");
		}
		else if(type.equals("drop")){
			wkList.get(cwIndex).setType(getSliderIndex(), indexSelectedContextMenuObject, "drop");
		}
		saveData(wkList);
		updateSliderLayout(getSliderIndex());
	}//Changes the icon of the button
	private void setButtonIcon(Integer i, Integer j, Button button){
		if(wkList.get(cwIndex).getType(i, j).equals("normal")){
			button.setBackgroundResource(R.drawable.ic_workout_normal);
		}
		else if(wkList.get(cwIndex).getType(i, j).equals("cluster")){
			button.setBackgroundResource(R.drawable.ic_workout_cluster);
		}
		else if(wkList.get(cwIndex).getType(i, j).equals("drop")){
			button.setBackgroundResource(R.drawable.ic_workout_drop);
		}
	}//Shows the icon of the button

	private void changeSetDone(final View view){
		//Get required index values
		final int index_e = getSliderIndex();
		String sindex = getDigits(view.getTag().toString());
		final int index = Integer.parseInt(sindex) - 1;
		//Toggle between true/false
		if(wkList.get(cwIndex).getIsDone(index_e, index))
			wkList.get(cwIndex).setIsDone(index_e, index, false);
		else
			wkList.get(cwIndex).setIsDone(index_e, index, true);
		saveData(wkList);

		updateSliderLayout(getSliderIndex());
	}
	private void updateSetTextview(Integer i, Integer j, TextView tview){
		if(wkList.get(cwIndex).getIsDone(i, j))
			tview.setTextColor(getResources().getColor(R.color.colorLightGreen));
		else
			tview.setTextColor(getResources().getColor(R.color.colorLightRed));
	}

	private void askIfContinue(){
		//Create builder
		AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutActivity.this);
		builder.setTitle("Continue from last time?");
		builder.setPositiveButton("Yes, Continue", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.setNegativeButton("Reset", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				for(int i = 0; i < wkList.get(cwIndex).getExerciseSize(); i++){
					for(int j = 0; j < wkList.get(cwIndex).getExerciseSetCount(i); j++){
						wkList.get(cwIndex).setIsDone(i, j, false);
					}
				}
				updateSliderLayout(getSliderIndex());
			}
		});
		//Show input field
		builder.show();
	}//Continue or reset workout

	private String getDigits(final CharSequence inputstring){
		final StringBuilder sb = new StringBuilder(inputstring.length());
		for(int i = 0; i < inputstring.length(); i++){
			final char c = inputstring.charAt(i);
			if(c > 47 && c < 58){
				sb.append(c);
			}
		}
		return sb.toString();
	}//Returns digits from 0-9 from a string

	private List<workout> loadData(){
		List<workout> data;
		//Set up shared preferences
		SharedPreferences sharedPreferences = getSharedPreferences("storage_WorkoutData_1", Context.MODE_PRIVATE);
		Gson gson = new Gson();

		//Load data
		String json = sharedPreferences.getString("workoutData_1", null);
		Type type = new TypeToken<ArrayList<workout>>() {}.getType();
		data = gson.fromJson(json, type);

		//If no data exists, allocate ArrayList
		if(data == null){
			data = new ArrayList<>();
		}

		return data;
	}//Loads data
	private void saveData(List<workout> data){
		//Set up shared preferences
		SharedPreferences sharedPreferences = getSharedPreferences("storage_WorkoutData_1", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		Gson gson = new Gson();
		//Save data
		String json = gson.toJson(data);
		editor.putString("workoutData_1", json);
		editor.apply();
	}//Saves data
}