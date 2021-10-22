package com.devscore.digital_pharmacy.presentation.supplier

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.util.StateMessageCallback
import com.devscore.digital_pharmacy.presentation.BaseActivity
import com.devscore.digital_pharmacy.presentation.auth.AuthActivity
import com.devscore.digital_pharmacy.presentation.session.SessionEvents
import com.devscore.digital_pharmacy.presentation.util.processQueue
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_supplier.*

@AndroidEntryPoint
class SupplierActivity : BaseActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.supplier_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBar()

        subscribeObservers()
    }

    private fun setupActionBar() {
    }


    fun subscribeObservers() {
        sessionManager.state.observe(this) { state ->
            displayProgressBar(state.isLoading)
            processQueue(
                context = this,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        sessionManager.onTriggerEvent(SessionEvents.OnRemoveHeadFromQueue)
                    }
                })
            if (state.authToken == null || state.authToken.accountPk == -1) {
                navAuthActivity()
            }
        }
    }

    override fun expandAppBar() {
    }

    private fun navAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun displayProgressBar(isLoading: Boolean) {
        if (isLoading) {
            supplier_progress_bar.visibility = View.VISIBLE
        } else {
            supplier_progress_bar.visibility = View.GONE
        }
    }

}