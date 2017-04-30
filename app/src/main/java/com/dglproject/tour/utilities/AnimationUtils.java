package com.dglproject.tour.utilities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import com.dglproject.tour.R;

/**
 * Created by Tortuvshin Byambaa on 3/30/2017.
 */
public class AnimationUtils {

    public static final int ANIMATION_DURATION_SHORTEST = 150;
    public static final int ANIMATION_DURATION_SHORT = 250;
    public static final int ANIMATION_DURATION_MEDIUM = 400;
    public static final int ANIMATION_DURATION_LONG = 800;

    @TargetApi(21)
    public static void circleRevealView(View view, int duration) {

        int cx = view.getWidth();
        int cy = view.getHeight() / 2;


        float finalRadius = (float) Math.hypot(cx, cy);


        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

        if (duration > 0) {
            anim.setDuration(duration);
        }
        else {
            anim.setDuration(ANIMATION_DURATION_SHORT);
        }

        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    @TargetApi(21)
    public static void circleRevealView(View view) {
        circleRevealView(view,ANIMATION_DURATION_SHORT);
    }

    @TargetApi(21)
    public static void circleHideView(final View view, AnimatorListenerAdapter listenerAdapter) {
        circleHideView(view,ANIMATION_DURATION_SHORT,listenerAdapter);
    }

    @TargetApi(21)
    public static void circleHideView(final View view, int duration, AnimatorListenerAdapter listenerAdapter) {

        int cx = view.getWidth();
        int cy = view.getHeight() / 2;


        float initialRadius = (float) Math.hypot(cx, cy);


        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

        anim.addListener(listenerAdapter);

        if (duration > 0) {
            anim.setDuration(duration);
        }
        else {
            anim.setDuration(ANIMATION_DURATION_SHORT);
        }

//        anim.setStartDelay(200);

        anim.start();
    }

    public static void fadeInView(View view) {
        fadeInView(view, ANIMATION_DURATION_SHORTEST);
    }

    public static void fadeInView(View view, int duration) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0f);

        ViewCompat.animate(view).alpha(1f).setDuration(duration).setListener(null);
    }


    public static void fadeOutView(View view) {
        fadeOutView(view, ANIMATION_DURATION_SHORTEST);
    }

    public static void fadeOutView(final View view, int duration) {
        ViewCompat.animate(view).alpha(0f).setDuration(duration).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                view.setDrawingCacheEnabled(true);
            }

            @Override
            public void onAnimationEnd(View view) {
                view.setVisibility(View.GONE);
                view.setAlpha(1f);
                view.setDrawingCacheEnabled(false);
            }

            @Override
            public void onAnimationCancel(View view) { }
        });
    }

    public static void crossFadeViews(View showView, View hideView) {
        crossFadeViews(showView, hideView, ANIMATION_DURATION_SHORT);
    }

    public static void crossFadeViews(View showView, final View hideView, int duration) {
        fadeInView(showView, duration);
        fadeOutView(hideView, duration);
    }

    /**
     * Author: Tortuvshin Byambaa.
     * Project: DglTour
     * URL: https://www.github.com/tortuvshin
     */
    public static class BottomNavigationBehavior<V extends View> extends VerticalScrollingBehavior<V> {
        private static final Interpolator INTERPOLATOR = new LinearOutSlowInInterpolator();
        private int mTabLayoutId;
        private boolean hidden = false;
        private ViewPropertyAnimatorCompat mTranslationAnimator;
        private TabLayout mTabLayout;
        private View mTabsHolder;

        public BottomNavigationBehavior() {
            super();
        }

        public BottomNavigationBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.BottomNavigationBehavior_Params);
            mTabLayoutId = a.getResourceId(R.styleable.BottomNavigationBehavior_Params_tabLayoutId, View.NO_ID);
            a.recycle();
        }

        @Override
        public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
            boolean layoutChild = super.onLayoutChild(parent, child, layoutDirection);
            if (mTabLayout == null && mTabLayoutId != View.NO_ID) {
                mTabLayout = findTabLayout(child);
                getTabsHolder();
            }
            return layoutChild;
        }

        private TabLayout findTabLayout(View child) {
            if (mTabLayoutId == 0) return null;
            return (TabLayout) child.findViewById(mTabLayoutId);
        }

        @Override
        public void onNestedVerticalOverScroll(CoordinatorLayout coordinatorLayout, V child, @ScrollDirection int direction, int currentOverScroll, int totalOverScroll) {
        }

        @Override
        public void onDirectionNestedPreScroll(CoordinatorLayout coordinatorLayout, V child, View target, int dx, int dy, int[] consumed, @ScrollDirection int scrollDirection) {
            handleDirection(child, scrollDirection);
        }

        private void handleDirection(V child, int scrollDirection) {
            if (scrollDirection == ScrollDirection.SCROLL_DIRECTION_DOWN && hidden) {
                hidden = false;
                animateOffset(child, 0);
            } else if (scrollDirection == ScrollDirection.SCROLL_DIRECTION_UP && !hidden) {
                hidden = true;
                animateOffset(child, child.getHeight());
            }
        }

        @Override
        protected boolean onNestedDirectionFling(CoordinatorLayout coordinatorLayout, V child, View target, float velocityX, float velocityY, @ScrollDirection int scrollDirection) {
            handleDirection(child, scrollDirection);
            return true;
        }

        private void animateOffset(final V child, final int offset) {
            ensureOrCancelAnimator(child);
            mTranslationAnimator.translationY(offset).start();
            animateTabsHolder(offset);
        }

        private void animateTabsHolder(int offset) {
            if (mTabsHolder != null) {
                offset = offset > 0 ? 0 : 1;
                ViewCompat.animate(mTabsHolder).alpha(offset).setDuration(200).start();
            }
        }


        private void ensureOrCancelAnimator(V child) {
            if (mTranslationAnimator == null) {
                mTranslationAnimator = ViewCompat.animate(child);
                mTranslationAnimator.setDuration(100);
                mTranslationAnimator.setInterpolator(INTERPOLATOR);
            } else {
                mTranslationAnimator.cancel();
            }
        }

        private void getTabsHolder() {
            if (mTabLayout != null) {
                mTabsHolder = mTabLayout.getChildAt(0);
            }
        }

        public static <V extends View> BottomNavigationBehavior<V> from(V view) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (!(params instanceof CoordinatorLayout.LayoutParams)) {
                throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
            }
            CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params)
                    .getBehavior();
            if (!(behavior instanceof BottomNavigationBehavior)) {
                throw new IllegalArgumentException(
                        "The view is not associated with ottomNavigationBehavior");
            }
            return (BottomNavigationBehavior<V>) behavior;
        }

        public void setTabLayoutId(@IdRes int tabId) {
            this.mTabLayoutId = tabId;
        }
    }
}
