package com.easing.commons.android.util;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 *
 */

public class FragmentManagerUtil {

    private FragmentManager fragmentManager;

    public FragmentManagerUtil(FragmentManager fm) {
        this.fragmentManager = fm;
    }

    public FragmentTransaction getBeginTransaction(){
        return fragmentManager.beginTransaction();
    }
    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void replaceFragment(int resid, Fragment fragment) {
        if (null == fragment) return;
        replaceFragment(resid, fragment, fragment.getClass().getSimpleName());
    }

    public void replaceFragment(int resid, Fragment fragment, String tag) {
        if (null == fragment) return;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(resid, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    public void addFragment(int resid, Fragment fragment) {
        if (null == fragment) return;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(resid, fragment, fragment.getClass().getSimpleName());
        ft.commitAllowingStateLoss();
    }

    public void showFragment(Fragment fragment) {
        if (null == fragment) return;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }

    public void hideFragment(Fragment fragment) {
        if (null == fragment) return;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.hide(fragment);
        ft.commitAllowingStateLoss();
    }

    public void removeFragment(Fragment fragment) {
        if (null == fragment) return;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }

    @Nullable
    public Fragment findFragment(String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }
}
