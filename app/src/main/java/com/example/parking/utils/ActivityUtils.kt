package com.example.parking.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.parking.R

object ActivityUtils {
    fun addFragmentToBackStack(fragmentManager: FragmentManager, containerId: Int, fragment: Fragment, backStackTag: String? = null) {
        val transaction = fragmentManager.beginTransaction()
        transaction.add(containerId, fragment).addToBackStack(backStackTag)
        transaction.commit()
    }

    fun replaceFragment(fragmentManager: FragmentManager, containerId: Int, fragment: Fragment, backStackTag: String?, animationType: AnimationType = AnimationType.NONE) {
        val transaction = fragmentManager.beginTransaction()

        when (animationType) {
            AnimationType.FLIP -> {
                transaction.setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out, R.animator.card_flip_left_in, R.animator.card_flip_left_out)
            }
            else -> {
                // no action
            }
        }
        if (!backStackTag.isNullOrEmpty()) {
            transaction.addToBackStack(backStackTag)
        }
        transaction.replace(containerId, fragment).commit()
    }
}

enum class AnimationType {
    NONE, FLIP;
}
