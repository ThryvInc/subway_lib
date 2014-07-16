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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: ell
 * Date: 9/29/13
 * Time: 10:23 AM
 */
public abstract class MainActivity extends Activity implements View.OnClickListener {

    protected ViewAnimator va;
    protected Animation slideLeftIn;
    protected Animation slideLeftOut;
    protected Animation slideRightIn;
    protected Animation slideRightOut;

    protected abstract String getFlurryApiKey();

    // e.g., http://developer.mbta.com/lib/rthr/blue.json
    protected abstract String getLineUrl(Line line);

    protected JSONObject fetchedData;

    protected ProgressDialog pd;

    protected final void makeRequest(Line line) {
        // TODO SubwayApplication.getRequestQueue().cancelAll(line);
        String url = getLineUrl(line);
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        fetchedData = jsonObject;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        fetchedData = null;
                        Toast.makeText(MainActivity.this,
                                "Please make sure you are connected to the internet.",
                                Toast.LENGTH_LONG).show();
                    }
                }
        );
        request.setTag(line);
        SubwayApplication.getRequestQueue().add(request);
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
}
