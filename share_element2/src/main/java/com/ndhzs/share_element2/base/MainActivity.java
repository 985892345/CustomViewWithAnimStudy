package com.ndhzs.share_element2.base;

import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ndhzs.share_element2.R;
import com.ndhzs.share_element2.Sample;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 项目来之于：https://github.com/Wing-Li/Material-Animations-CN
 */
public class MainActivity extends AppCompatActivity {

    private List<Sample> samples;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupWindowAnimations(1);
        setupSamples();
        setupToolbar();
        setupLayout();
    }

    @SuppressWarnings("SameParameterValue")
    private void setupWindowAnimations(int type) {

        Transition transition;
        switch (type) {
            case 0:
                // 侧滑动画
                Slide slide = new Slide();
                slide.setSlideEdge(Gravity.START);
                slide.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                transition = slide;
                break;
            case 1:
                // 爆炸效果的动画
                Explode explode = new Explode();
                explode.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                transition = explode;
                break;
            default:
                // 渐变动画
                Fade fade = new Fade();
                fade.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                transition = fade;
        }

        // 这两个方法在 TransitionActivity1 详解
        getWindow().setReenterTransition(transition);
        getWindow().setExitTransition(transition);
    }

    private void setupSamples() {
        samples = Arrays.asList(new Sample(ContextCompat.getColor(this, R.color.sample_red), "Transitions"), //
                new Sample(ContextCompat.getColor(this, R.color.sample_blue), "Shared Elements"), //
                new Sample(ContextCompat.getColor(this, R.color.sample_green), "View animations"),//
                new Sample(ContextCompat.getColor(this, R.color.sample_yellow), "Circular Reveal Animation"));
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    private void setupLayout() {
        RecyclerView recyclerView = findViewById(R.id.sample_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SamplesRecyclerAdapter samplesRecyclerAdapter = new SamplesRecyclerAdapter(this, samples);
        recyclerView.setAdapter(samplesRecyclerAdapter);
    }

}
