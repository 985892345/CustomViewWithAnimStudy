package com.ndhzs.share_element2.one;

import android.os.Bundle;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.animation.AccelerateInterpolator;
import androidx.databinding.DataBindingUtil;

import com.ndhzs.share_element2.R;
import com.ndhzs.share_element2.Sample;
import com.ndhzs.share_element2.base.BaseActivity;
import com.ndhzs.share_element2.databinding.ActivityTransition2Binding;

public class TransitionActivity2 extends BaseActivity {

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition2);
        bindData();
        setupWindowAnimations();
        setupLayout();
        setupToolbar();
    }


    private void bindData() {
        ActivityTransition2Binding transition2Binding = DataBindingUtil.setContentView(this, R.layout
                .activity_transition2);
        Sample sample = (Sample) getIntent().getExtras().getSerializable(EXTRA_SAMPLE);
        type = getIntent().getExtras().getInt(EXTRA_TYPE);
        transition2Binding.setTransition2Sample(sample);
    }


    private void setupWindowAnimations() {
        Transition transition;

        if (type == TYPE_PROGRAMMATICALLY) {
            transition = buildEnterTransition();
        } else {
            transition = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        }

        getWindow().setEnterTransition(transition);
    }


    private void setupLayout() {
        findViewById(R.id.exit_button).setOnClickListener(v -> finishAfterTransition());
    }


    private Transition buildEnterTransition() {
        Explode enterTransition = new Explode();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        // 修饰动画，定义动画的变化率
        enterTransition.setInterpolator(new AccelerateInterpolator());
        return enterTransition;
    }
}
