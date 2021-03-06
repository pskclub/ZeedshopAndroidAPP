package com.boodabest.core

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.boodabest.hideKeyboard
import com.boodabest.repositories.AppViewModel
import com.boodabest.repositories.auth.AuthViewModel
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseActivity(@LayoutRes contentLayoutId: Int) : AppCompatActivity(contentLayoutId),
    HasSupportFragmentInjector {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    protected val auth: AuthViewModel by viewModels {
        viewModelFactory
    }

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.onReceiveAppData()
    }

    private fun onReceiveAppData() {
        val app: AppViewModel by viewModels {
            viewModelFactory
        }

        app.title.observe(this, Observer {
            supportActionBar?.title = it
        })

        app.backAble.observe(this, Observer {
            supportActionBar?.setDisplayHomeAsUpEnabled(it)
        })
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}