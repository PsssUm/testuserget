package com.psssum.docs.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.transition.Fade
import com.psssum.docs.R
import com.psssum.docs.models.VKDoc
import com.psssum.docs.ui.customViews.MoreMenuFactory
import com.psssum.docs.ui.fragments.DetailFragment
import com.psssum.docs.ui.fragments.DocsFragment
import com.psssum.market.utils.L
import com.skydoves.powermenu.kotlin.powerMenu
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope


class MainActivity : AppCompatActivity() {

    private var docsFragment : DocsFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkLogin()
    }

    private fun checkLogin(){
        if (!VK.isLoggedIn()) {
            VK.login(this, arrayListOf(VKScope.DOCS))
        } else {
            setUpFragments()
        }
    }

    private fun setUpFragments(){
        L.d("setUpFragments")
        docsFragment = DocsFragment.newInstance()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.fragmentPlaceholder, docsFragment!!)
        transaction.commitAllowingStateLoss()
    }
    private fun replaceFragment(fragment: Fragment, animation : Int){
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.setCustomAnimations(animation, 0)
        transaction.add(R.id.fragmentPlaceholder, fragment)
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
    }


    fun setDetailFragment(vkDoc: VKDoc){
        val detailFragment = DetailFragment.newInstance()
        detailFragment.vkDoc = vkDoc
        replaceFragment(detailFragment, R.anim.enter_from_right)
    }

    fun removeDocFragment(){
        supportFragmentManager.popBackStack()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                setUpFragments()
            }

            override fun onLoginFailed(errorCode: Int) {
                checkLogin()
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
