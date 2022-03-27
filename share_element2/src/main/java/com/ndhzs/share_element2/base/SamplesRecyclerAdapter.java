package com.ndhzs.share_element2.base;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ndhzs.share_element2.R;
import com.ndhzs.share_element2.Sample;
import com.ndhzs.share_element2.TransitionHelper;
import com.ndhzs.share_element2.databinding.RowSampleBinding;
import com.ndhzs.share_element2.four.RevealActivity;
import com.ndhzs.share_element2.one.TransitionActivity1;
import com.ndhzs.share_element2.three.AnimationsActivity1;
import com.ndhzs.share_element2.two.SharedElementActivity;

import java.util.List;

/**
 * Wing_Li
 * 2016/9/14.
 */
public class SamplesRecyclerAdapter extends RecyclerView.Adapter<SamplesRecyclerAdapter.SamplesViewHolder> {

    private final Activity activity;
    private final List<Sample> samples;

    public SamplesRecyclerAdapter(Activity activity, List<Sample> samples) {
        this.activity = activity;
        this.samples = samples;
    }

    @NonNull
    @Override
    public SamplesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowSampleBinding rowSampleBinding = RowSampleBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new SamplesViewHolder(rowSampleBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(final SamplesViewHolder holder, final int position) {
        final Sample sample = samples.get(holder.getAdapterPosition());
        holder.binding.setSample(sample);
        holder.binding.sampleLayout.setOnClickListener(v -> {
            switch (holder.getAdapterPosition()) {
                case 0: { // 普通 Transition （页面切换的过渡效果）
                    transitionToActivity(TransitionActivity1.class, sample);
                    break;
                }
                case 1: {// Shared Elements Transition 共享元素转换（页面切换的过渡效果）
                    transitionToActivity(SharedElementActivity.class, holder, sample);
                    break;
                }
                case 2: {// View 的动画
                    transitionToActivity(AnimationsActivity1.class, sample);
                    break;
                }
                case 3: {
                    transitionToActivity(RevealActivity.class, holder, sample, R.string.transition_reveal1);
                    break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return samples == null ? 0 : samples.size();
    }


    public static class SamplesViewHolder extends RecyclerView.ViewHolder {
        RowSampleBinding binding;

        public SamplesViewHolder(View rootView) {
            super(rootView);
            binding = DataBindingUtil.bind(rootView);
        }
    }


    private void transitionToActivity(Class<? extends Activity> target, Sample sample) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(activity, true);
        startActivity(target, pairs, sample);
    }


    @SuppressWarnings("SameParameterValue")
    private void transitionToActivity(Class<? extends Activity> target, SamplesViewHolder viewHolder, Sample sample) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(activity, false, new
                Pair<>(viewHolder.binding.sampleIcon, activity.getString(R.string.square_blue_name)), new Pair<>
                (viewHolder.binding.sampleName, activity.getString(R.string.sample_blue_title)));
        startActivity(target, pairs, sample);
    }

    @SuppressWarnings("SameParameterValue")
    private void transitionToActivity(Class<? extends Activity> target, SamplesViewHolder viewHolder, Sample sample, int transitionName) {
        Pair<View, String>[] pair = TransitionHelper.createSafeTransitionParticipants(
                activity,
                false,
                new Pair<>(viewHolder.binding.sampleIcon, activity.getString(transitionName))
        );
        startActivity(target, pair, sample);
    }


    private void startActivity(Class<? extends Activity> target, Pair<View, String>[] pairs, Sample sample) {
        Intent i = new Intent(activity, target);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation
                (activity, pairs);
        i.putExtra("sample", sample);
        activity.startActivity(i, transitionActivityOptions.toBundle());
    }
}
