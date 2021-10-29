package com.devscore.digital_pharmacy.presentation.customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.util.StateMessageCallback
import com.devscore.digital_pharmacy.presentation.BaseActivity
import com.devscore.digital_pharmacy.presentation.auth.AuthActivity
import com.devscore.digital_pharmacy.presentation.session.SessionEvents
import com.devscore.digital_pharmacy.presentation.util.processQueue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_customer.*
import kotlinx.android.synthetic.main.activity_supplier.*

@AndroidEntryPoint
class CustomerActivity : BaseActivity() {

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
        setContentView(R.layout.activity_customer)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.customer_container_fragment) as NavHostFragment
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
            customer_progress_bar.visibility = View.VISIBLE
        } else {
            customer_progress_bar.visibility = View.GONE
        }
    }

}