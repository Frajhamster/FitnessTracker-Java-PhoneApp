package frajhamdoingstuff.epizy.com.testingenvironment;

import android.app.ActionBar;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SliderAdapter extends PagerAdapter {
    Context context;

    public ArrayList<View> mviews = new ArrayList<>();

    public SliderAdapter(Context context){
        this.context = context;
    }
    @Override
    public int getCount() {
        return mviews.size();
    }
    @Override
    public int getItemPosition (Object object) {
        int index = mviews.indexOf(object);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position){
        View view = mviews.get(position);
        container.addView(view);

        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView(mviews.get(position));
    }

    //Adds view
    public int addView(View v){
        return addView(v, mviews.size());
    }
    public int addView(View v, int position){
        mviews.add(position, v);
        return position;
    }
    //Removes view
    public int removeView(ViewPager pager, View v){
        return removeView(pager, mviews.indexOf(v));
    }
    public int removeView(ViewPager pager, int position){
        pager.setAdapter(null);
        mviews.remove(position);
        pager.setAdapter(this);

        return position;
    }
    //Removes all views
    public void removeAllViews(ViewPager pager){
        pager.setAdapter(null);
        mviews.clear();
        pager.setAdapter(this);
    }
    //Returns the view at "position"
    public View getView(int position){
        return mviews.get(position);
    }
}
