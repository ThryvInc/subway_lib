package com.rndapp.subway_lib;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.android.volley.VolleyError;
import com.flurry.android.FlurryAgent;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: ell
 * Date: 9/29/13
 * Time: 10:23 AM
 */
public abstract class MainActivity extends Activity implements
        View.OnClickListener,
        LineScheduleLoader.OnLineScheduleLoadedListener {

    protected ViewAnimator va;
    protected Animation slideLeftIn;
    protected Animation slideLeftOut;
    protected Animation slideRightIn;
    protected Animation slideRightOut;

    protected abstract String getFlurryApiKey();

    // e.g., http://developer.mbta.com/lib/rthr/blue.json
    protected abstract String getLineUrl(Line line);

    // holds the schedule data
    protected JSONObject fetchedData;

    // appears when request is loading schedule
    protected ProgressDialog pd;

    // Used by subprojects in onClick
    protected final void makeRequest(Line line) {
        pd = ProgressDialog.show(this, "", "Loading", true, true);
        LineScheduleLoader.load(this, getLineUrl(line));
    }

    @Override
    public void onLineScheduleLoaded(JSONObject jsonObject) {
        pd.dismiss();

        // presumably a non-null schedule
        fetchedData = jsonObject;

        // just in case, check for success, then toggle animations
        if (jsonObject != null) {
            va.setInAnimation(slideLeftIn);
            va.setOutAnimation(slideLeftOut);
            va.showNext();
        }
    }

    @Override
    public void onFailure(VolleyError volleyError) {
        pd.dismiss();
        fetchedData = null;
        Toast.makeText(MainActivity.this, "No Internet Connection.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.see_map) {
            va.setInAnimation(slideRightIn);
            va.setOutAnimation(slideRightOut);
            va.showPrevious();
        } else if (id == R.id.see_sched) {
            va.setInAnimation(slideLeftIn);
            va.setOutAnimation(slideLeftOut);
            va.showNext();
        } else if (id == R.id.back_to_sched) {
            va.setInAnimation(slideRightIn);
            va.setOutAnimation(slideRightOut);
            va.showPrevious();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setXML();
    }

    protected void setXML() {
        va = (ViewAnimator) findViewById(R.id.chooser_anim);
        setAnimations();

        // see schedule button
        Button sched = (Button) findViewById(R.id.see_sched);
        sched.setOnClickListener(this);
        sched.setBackgroundColor(getResources().getColor(R.color.grey));

        // show map button
        Button map = (Button) findViewById(R.id.see_map);
        map.setOnClickListener(this);
        map.setBackgroundColor(getResources().getColor(R.color.grey));

        // back to schedule button
        Button backsched = (Button) findViewById(R.id.back_to_sched);
        backsched.setOnClickListener(this);
        backsched.setBackgroundColor(getResources().getColor(R.color.grey));

        // shows map
        TouchImageView img = (TouchImageView) findViewById(R.id.touchImg);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap subway = BitmapFactory.decodeResource(getResources(), R.drawable.subway, options);
        img.setImageBitmap(subway);

    }

    protected void setAnimations() {
        slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.push_left_in);
        slideLeftIn.setAnimationListener(new ScrollLeft());
        slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.push_left_out);
        slideRightIn = AnimationUtils.loadAnimation(this, R.anim.push_right_in);
        slideRightIn.setAnimationListener(new ScrollRight());
        slideRightOut = AnimationUtils.loadAnimation(this, R.anim.push_right_out);
    }

    class ScrollRight implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {
            va.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //va.showPrevious();
                }
            }, 10);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }
    }

    class ScrollLeft implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {
            va.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //va.showNext();
                }
            }, 10);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, getFlurryApiKey());
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }
}
