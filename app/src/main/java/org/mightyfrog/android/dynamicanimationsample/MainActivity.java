package org.mightyfrog.android.dynamicanimationsample;

/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;

/**
 * https://twitter.com/crafty/status/842055117323026432
 * https://developer.android.com/reference/android/support/animation/package-summary.html
 *
 * @author modified by Shigehiro Soejima
 */
public class MainActivity extends AppCompatActivity {
    private float mStiffness = SpringForce.STIFFNESS_MEDIUM;
    private float mDamping = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY;

    private float mDownX;
    private float mDownY;
    private VelocityTracker mVelocityTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVelocityTracker = VelocityTracker.obtain();

        final ImageView droid = findViewById(R.id.droid);
        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            private final Rect mRect = new Rect();

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDownX = event.getX();
                        mDownY = event.getY();
                        mVelocityTracker.addMovement(event);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float x = event.getX();
                        float y = event.getY();
                        droid.getHitRect(mRect);
                        if (mRect.contains((int) x, (int) y)) {
                            droid.setTranslationX(x - mDownX);
                            droid.setTranslationY(y - mDownY);
                            mVelocityTracker.addMovement(event);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        // fall-thru
                    case MotionEvent.ACTION_CANCEL:
                        mVelocityTracker.computeCurrentVelocity(1000);
                        if (droid.getTranslationX() != 0) {
                            SpringAnimation animX = new SpringAnimation(droid, SpringAnimation.TRANSLATION_X, 0);
                            animX.getSpring().setStiffness(getStiffness());
                            animX.getSpring().setDampingRatio(getDamping());
                            animX.setStartVelocity(mVelocityTracker.getXVelocity());
                            animX.start();
                        }
                        if (droid.getTranslationY() != 0) {
                            SpringAnimation animY = new SpringAnimation(droid, SpringAnimation.TRANSLATION_Y, 0);
                            animY.getSpring().setStiffness(getStiffness());
                            animY.getSpring().setDampingRatio(getDamping());
                            animY.setStartVelocity(mVelocityTracker.getYVelocity());
                            animY.start();
                        }
                        mVelocityTracker.clear();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.stiffness_high).setChecked(mStiffness == SpringForce.STIFFNESS_HIGH);
        menu.findItem(R.id.stiffness_medium).setChecked(mStiffness == SpringForce.STIFFNESS_MEDIUM);
        menu.findItem(R.id.stiffness_low).setChecked(mStiffness == SpringForce.STIFFNESS_LOW);
        menu.findItem(R.id.stiffness_very_low).setChecked(mStiffness == SpringForce.STIFFNESS_VERY_LOW);

        menu.findItem(R.id.damping_ratio_high_bouncy).setChecked(mDamping == SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        menu.findItem(R.id.damping_ratio_medium_bouncy).setChecked(mDamping == SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY);
        menu.findItem(R.id.damping_ratio_low_bouncy).setChecked(mDamping == SpringForce.DAMPING_RATIO_LOW_BOUNCY);
        menu.findItem(R.id.damping_ratio_no_bouncy).setChecked(mDamping == SpringForce.DAMPING_RATIO_NO_BOUNCY);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.stiffness_high:
                mStiffness = SpringForce.STIFFNESS_HIGH;
                break;
            case R.id.stiffness_medium:
                mStiffness = SpringForce.STIFFNESS_MEDIUM;
                break;
            case R.id.stiffness_low:
                mStiffness = SpringForce.STIFFNESS_LOW;
                break;
            case R.id.stiffness_very_low:
                mStiffness = SpringForce.STIFFNESS_VERY_LOW;
                break;
            case R.id.damping_ratio_high_bouncy:
                mDamping = SpringForce.DAMPING_RATIO_HIGH_BOUNCY;
                break;
            case R.id.damping_ratio_medium_bouncy:
                mDamping = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY;
                break;
            case R.id.damping_ratio_low_bouncy:
                mDamping = SpringForce.DAMPING_RATIO_LOW_BOUNCY;
                break;
            case R.id.damping_ratio_no_bouncy:
                mDamping = SpringForce.DAMPING_RATIO_NO_BOUNCY;
                break;
        }

        invalidateOptionsMenu();

        reset();

        return super.onOptionsItemSelected(item);
    }

    private void reset() {
        mDownX = 0f;
        mDownY = 0f;
        mVelocityTracker.clear();
    }

    private float getStiffness() {
        return mStiffness;
    }

    private float getDamping() {
        return mDamping;
    }
}