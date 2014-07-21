package com.rndapp.subway_lib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewAnimator;

/**
 * Created with IntelliJ IDEA.
 * User: ell
 * Date: 9/29/13
 * Time: 10:23 AM
 */
public class MainActivity extends Activity implements View.OnClickListener {
    protected ViewAnimator va;
    protected Animation slideLeftIn;
    protected Animation slideLeftOut;
    protected Animation slideRightIn;
    protected Animation slideRightOut;

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

    @Override
    public void onClick(View v) {
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
