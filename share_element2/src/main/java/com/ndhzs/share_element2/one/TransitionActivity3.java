package com.ndhzs.share_element2.one;

import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import androidx.databinding.DataBindingUtil;
import com.ndhzs.share_element2.R;
import com.ndhzs.share_element2.Sample;
import com.ndhzs.share_element2.base.BaseActivity;
import com.ndhzs.share_element2.databinding.ActivityTransition3Binding;

public class TransitionActivity3 extends BaseActivity {

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition3);
        bindData();
        setupWindowAnimations();
        setupLayout();
    }


    private void bindData() {
        ActivityTransition3Binding binding = DataBindingUtil.setContentView(this, R.layout.activity_transition3);
        Sample sample = (Sample) getIntent().getExtras().getSerializable(EXTRA_SAMPLE);
        type = getIntent().getExtras().getInt(EXTRA_TYPE);
        binding.setTransition3Sample(sample);
    }


    private void setupWindowAnimations() {
        Transition transition;

        if (type == TYPE_PROGRAMMATICALLY) {
            transition = buildEnterTransition();
        } else {
            transition = TransitionInflater.from(this).inflateTransition(R.transition.slide_from_bottom);
        }

        getWindow().setEnterTransition(transition);
    }


    private void setupLayout() {
        findViewById(R.id.exit_button).setOnClickListener(v -> finishAfterTransition());
    }


    private Visibility buildEnterTransition() {
        Slide slide = new Slide();
        slide.setDuration(800);
        slide.setSlideEdge(Gravity.END);
        slide.setInterpolator(new BounceInterpolator());
        return slide;
    }

}
