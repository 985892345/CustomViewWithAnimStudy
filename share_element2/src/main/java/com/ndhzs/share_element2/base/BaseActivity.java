package com.ndhzs.share_element2.base;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import com.ndhzs.share_element2.R;
import com.ndhzs.share_element2.TransitionHelper;

import java.util.Objects;

/**
 * Wing_Li
 * 2016/9/14.
 */
public class BaseActivity extends AppCompatActivity {
    /**
     * 圆球显示属性
     */
    protected static final String EXTRA_SAMPLE = "sample";
    /**
     * 动画类型
     */
    protected static final String EXTRA_TYPE = "type";
    /**
     * 代码 定义动画
     */
    protected static final int TYPE_PROGRAMMATICALLY = 0;
    /**
     * Xml 定义动画
     */
    protected static final int TYPE_XML = 1;

    protected void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    protected void transitionTo(Intent i) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                pairs);
        startActivity(i, transitionActivityOptions.toBundle());
    }
}
