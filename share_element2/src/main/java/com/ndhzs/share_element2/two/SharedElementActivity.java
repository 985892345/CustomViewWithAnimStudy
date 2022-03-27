package com.ndhzs.share_element2.two;

import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.ndhzs.share_element2.R;
import com.ndhzs.share_element2.Sample;
import com.ndhzs.share_element2.base.BaseActivity;
import com.ndhzs.share_element2.databinding.ActivitySharedElementBinding;

public class SharedElementActivity extends BaseActivity {

    private Sample sample;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindData();
        setupWindowAnimations();
        setupLayout();
        setupToolbar();
    }


    private void bindData() {
        ActivitySharedElementBinding elementBinding = DataBindingUtil.setContentView(this, R.layout
                .activity_shared_element);
        sample = (Sample) getIntent().getExtras().getSerializable(EXTRA_SAMPLE);
        elementBinding.setSharedSample(sample);
    }


    private void setupWindowAnimations() {
        // 我们不想定义新的 Enter Transition。
        // 只更改默认的过渡持续时间
        getWindow().getEnterTransition().setDuration(getResources().getInteger(R.integer.anim_duration_long));
    }


    private void setupLayout() {
        // 为 fragment1 定义 过渡动画
        Slide slide = new Slide(Gravity.START);
        slide.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        // 创建 fragment ，并且一定一些它的动画
        SharedElementFragment1 fragment1 = SharedElementFragment1.newInstance(sample);
        fragment1.setReenterTransition(slide);
        fragment1.setExitTransition(slide);
        // 为目标视图的布局边界的变化添加动画。
        fragment1.setSharedElementEnterTransition(new ChangeBounds());
        // 为目标视图的裁剪边界的变化添加动画。
        // fragment1.setSharedElementEnterTransition(new ChangeClipBounds());
        // 为目标视图的缩放与旋转变化添加动画。
        // fragment1.setSharedElementEnterTransition(new ChangeTransform());
        // 为目标图像的大小与缩放变化添加动画。
        // fragment1.setSharedElementEnterTransition(new ChangeImageTransform());

        getSupportFragmentManager().beginTransaction().replace(R.id.sample2_content, fragment1).commit();
    }
}
