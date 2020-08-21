package frajhamdoingstuff.epizy.com.testingenvironment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainProgram extends AppCompatActivity {
	//Global variables for background video
	private VideoView mVideo;
	private MediaPlayer mMediaPlayer;
	int mCurrentVideoPosition;
	//Global variables for slider
	private ViewPager mSlideViewPager;
	private SliderAdapter sliderAdapter;
	//Global variables for actual application
	private Activity activity = this;
	private List<workout> wkList;
	private int indexSelectedContextMenuWorkout;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_program);

		wkList = loadData();

		setVideo();
		setSlider();
		setButtons();
	}
	@Override
	protected void onResume(){
		super.onResume();

		setVideo();
	}

	private void setVideo(){
		mVideo = (VideoView) findViewById(R.id.videoViewStartScreen);
		Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_startscreen);

		mVideo.setVideoURI(uri);
		mVideo.start();
		mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
			@Override
			public void onPrepared(MediaPlayer mediaPlayer){
				mMediaPlayer = mediaPlayer;
				mMediaPlayer.setLooping(true);
				if(mCurrentVideoPosition != 0){
					mMediaPlayer.seekTo(mCurrentVideoPosition);
					mMediaPlayer.start();
				}
			}
		});
	}
	private void setSlider(){
		mSlideViewPager = (ViewPager) findViewById(R.id.ViewPagerSlider);
		sliderAdapter = new SliderAdapter(this);
		mSlideViewPager.setAdapter(sliderAdapter);
		//Add layouts to the slider
		addSliderLayout(R.layout.slider_layout);
		addSliderLayout(R.layout.slider_layout_2);
		//Update slider in case workouts exist
		updateSliderLayout(0);
	}
	private void setButtons(){
		setButtonCreateWorkout();
	}
	private void setButtonCreateWorkout(){
		//Find "button_createworkout"
		LayoutInflater inflater = this.getLayoutInflater();
		View view = inflater.inflate(R.layout.slider_layout, null);
		Button button = view.findViewById(R.id.button_createworkout);
		//ON-CLICK
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Create builder
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle("Name");
				final EditText input = new EditText(activity);
				input.setInputType(InputType.TYPE_CLASS_TEXT);
				builder.setView(input);
				builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String name = input.getText().toString();
						for(int i = 0; i < wkList.size(); i++){
							if(wkList.get(i).getName().equals(name) || name.equals("")) {
								dialog.cancel();
								return;
							}
						}
						if(wkList.size() < 12)
							addWorkout(name);
						else
							dialog.cancel();
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
		});
		removeSliderLayout(0);
		addSliderLayout(view, 0);
	}//Called from "setButtons()"

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
	private void updateSliderLayout(int setIndex){
		removeSliderLayout(1);
		//Inflate second slider layout and change properties
		LayoutInflater inflater = this.getLayoutInflater();
		View view = inflater.inflate(R.layout.slider_layout_2, null);

		//Create buttons
		LinearLayout ll = (LinearLayout) view.findViewWithTag("workout_list_layout");
		for(int i = 0; i < wkList.size(); i++){
			//New Button
			Button btn = new Button(MainProgram.this);
			//Layout Parameters
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(0,0,0,40);
			params.gravity = Gravity.CENTER;
			btn.setLayoutParams(params);
			//Tag
			btn.setTag("button_workout_" + (i + 1));
			//Background
			btn.setBackgroundColor(Color.parseColor("#00FFFFFF"));
			//Text
			btn.setText(wkList.get(i).getName());
			btn.setTextAppearance(MainProgram.this, R.style.style_casual_20);
			//ON-CLICK
			btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					//Get index of pressed button
					String tagName = view.getTag().toString();
					String indexString = getDigits(tagName);
					int index = Integer.parseInt(indexString) - 1;
					//Switch to WorkoutActivity.java + Pass the index to this activity
					Intent intent = new Intent(MainProgram.this, WorkoutActivity.class);
					intent.putExtra("index", index);
					startActivity(intent);
				}
			});
			//Add to context menu list
			registerForContextMenu(btn);
			//Add to view
			ll.addView(btn);
		}
		//Put second slider layout back to slider
		addSliderLayout(view);
		//Show specific layout
		if(setIndex >= 0)
			setSliderLayout(setIndex);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);

		getMenuInflater().inflate(R.menu.activity_mainprogram_contextmenu_1, menu);

		indexSelectedContextMenuWorkout = Integer.parseInt(getDigits(v.getTag().toString())) - 1;
	}

	@Override
	public boolean onContextItemSelected(@NonNull MenuItem item){
		switch(item.getItemId()){
			case R.id.activity_mainprogram_contextmenu_1_delete:
				deleteWorkout();
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

	private void addWorkout(String name){
		wkList.add(new workout(name));
		saveData(wkList);
		updateSliderLayout(0);
	}//Called from "setButtonCreateWorkout()"
	private void deleteWorkout(){
		//Remove from list and save
		wkList.remove(indexSelectedContextMenuWorkout);
		saveData(wkList);
		//Update screen
		updateSliderLayout(1);
	}

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