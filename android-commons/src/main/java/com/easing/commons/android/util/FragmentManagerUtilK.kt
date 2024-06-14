package com.easing.commons.android.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * FragmentManagerUtil 工具类
 * */
class FragmentManagerUtilK(var fragmentManager: FragmentManager) {
    fun getFragmentManagers(): FragmentManager {
        return fragmentManager
    }

    fun replaceFragment(resid: Int, fragment: Fragment?) {
        if (null == fragment) return
        replaceFragment(resid, fragment, fragment.javaClass.simpleName)
    }

    fun replaceFragment(resid: Int, fragment: Fragment?, tag: String?) {
        if (null == fragment) return
        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.replace(resid, fragment, tag)
        ft.commitAllowingStateLoss()
    }

    fun addFragment(resid: Int, fragment: Fragment?) {
        if (null == fragment) return
        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.add(resid, fragment, fragment.javaClass.simpleName)
        ft.commitAllowingStateLoss()
    }

    fun showFragment(fragment: Fragment?) {
        if (null == fragment) return
        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.show(fragment)
        ft.commitAllowingStateLoss()
    }

    fun hideFragment(fragment: Fragment?) {
        if (null == fragment) return
        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.hide(fragment)
        ft.commitAllowingStateLoss()
    }

    fun removeFragment(fragment: Fragment?) {
        if (null == fragment) return
        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.remove(fragment)
        ft.commitAllowingStateLoss()
    }

    fun findFragment(tag: String?): Fragment? {
        return fragmentManager.findFragmentByTag(tag)
    }
}