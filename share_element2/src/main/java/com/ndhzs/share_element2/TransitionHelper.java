package com.ndhzs.share_element2;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Wing_Li
 * 2016/9/14.
 */
public class TransitionHelper {
    /**
     * 创建所需的活动过渡期间避免与系统UI小过渡的参与者。
     *
     * @param activity         The activity used as start for the transition.
     * @param includeStatusBar 如果是错误的，状态栏将不会被添加为过渡参与者
     * @return All transition participants.
     */
    @SafeVarargs
    public static Pair<View, String>[] createSafeTransitionParticipants(@NonNull Activity activity, boolean
            includeStatusBar, @Nullable Pair<View, String>... otherParticipants) {
        // Avoid system UI glitches as described here:
        // https://plus.google.com/+AlexLockwood/posts/RPtwZ5nNebb
        View decor = activity.getWindow().getDecorView();
        View statusBar = null;
        if (includeStatusBar) {
            statusBar = decor.findViewById(android.R.id.statusBarBackground);
        }
        View navBar = decor.findViewById(android.R.id.navigationBarBackground);

        // 创建一对过渡参与者。
        List<Pair<View, String>> participants = new ArrayList<>(3);
        addNonNullViewToTransitionParticipants(statusBar, participants);
        addNonNullViewToTransitionParticipants(navBar, participants);
        // only add transition participants if there's at least one none-null element
        // 只有添加过渡参与者，如果至少有一个非空元素
        if (otherParticipants != null && !(otherParticipants.length == 1 && otherParticipants[0] == null)) {
            participants.addAll(Arrays.asList(otherParticipants));
        }
        return participants.toArray(new Pair[participants.size()]);
    }

    private static void addNonNullViewToTransitionParticipants(View view, List<Pair<View, String>> participants) {
        if (view == null) {
            return;
        }
        participants.add(new Pair<>(view, view.getTransitionName()));
    }

}
