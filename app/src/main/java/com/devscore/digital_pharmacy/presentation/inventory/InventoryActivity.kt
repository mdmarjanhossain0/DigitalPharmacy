package com.devscore.digital_pharmacy.presentation.inventory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.devscore.digital_pharmacy.MainActivity
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.util.StateMessageCallback
import com.devscore.digital_pharmacy.presentation.BaseActivity
import com.devscore.digital_pharmacy.presentation.auth.AuthActivity
import com.devscore.digital_pharmacy.presentation.session.SessionEvents
import com.devscore.digital_pharmacy.presentation.util.processQueue
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_container.backImage
import kotlinx.android.synthetic.main.activity_inventory.*

@AndroidEntryPoint
class InventoryActivity : BaseActivity(), View.OnClickListener {

    private lateinit var navigationView: NavigationView
    var drawerLayout: DrawerLayout? = null


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    override fun displayProgressBar(isLoading: Boolean) {
        if(isLoading){
            inventory_progress_bar.visibility = View.VISIBLE
        }
        else{
            inventory_progress_bar.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        setUpUI()
        initUIClick()

        navigationView = NavigationView(this)
        onSetNavigationDrawerEvents()
        subscribeObservers()

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

    private fun navAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun initUIClick() {
        backImage.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setUpUI() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.inventory_fragment_container) as NavHostFragment
        navController = navHostFragment.navController
    }


    private fun onSetNavigationDrawerEvents() {
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        val menuImg: ImageView = findViewById(R.id.menuImgId)
        val closeImg: ImageView = findViewById(R.id.productDetailsCloseIcon)
        val navDashboardTv: TextView = findViewById(R.id.navDashboardTvId)
        val navInventoryTv: TextView = findViewById(R.id.navInventoryTvId)
        val navSalesTv: TextView = findViewById(R.id.navSalesTvId)

        menuImg.setOnClickListener(this)
        closeImg.setOnClickListener(this)
        navDashboardTv.setOnClickListener(this)
        navInventoryTv.setOnClickListener(this)
        navSalesTv.setOnClickListener(this)


    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.menuImgId -> {
                drawerLayout!!.openDrawer(navigationView, true)
            }
            R.id.productDetailsCloseIcon -> {
                drawerLayout!!.closeDrawer(navigationView, true)
            }
            R.id.navDashboardTvId -> {
                drawerLayout!!.closeDrawer(navigationView, true)
                this.startActivity(Intent(this, MainActivity::class.java))
            }

            R.id.navInventoryTvId -> {
                drawerLayout!!.closeDrawer(navigationView, true)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerId, InventoryFragment()).commit()
            }

            R.id.navSalesTvId -> {
                drawerLayout!!.closeDrawer(navigationView, true)
            }

            else -> {
                drawerLayout!!.closeDrawer(GravityCompat.START, true)
            }
        }
    }

    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(navigationView)) {
            drawerLayout!!.closeDrawer(navigationView, true)
        } else {
            super.onBackPressed()
        }
    }

    override fun expandAppBar() {
    }


    fun navigateGlobalFragmentToAddMedicineContainerFragment(id : Int? = -1){
        Log.d(TAG, "Inventory Activity Id " + id.toString())
//            val data = bundleOf("id" to id)
//            navController.navigate(R.id.action_inventoryFragment_to_inventoryAddProductFragment, data)
        val direction = InventoryFragmentDirections.actionInventoryFragmentToInventoryAddProductFragment()
        direction.id = id!!
        navController.navigate(direction)
    }

    fun navigateLocalFragmentToReturnFragment(){
        navController.navigate(R.id.action_inventoryFragment_to_return1Fragment)
    }

    fun navigateLocalFragmentToMedicineDetailEditFragment(){
        navController.navigate(R.id.action_inventoryFragment_to_inventoryProductDetailsEditFragment)
    }


    fun navigateAddMedicineFragmentToInventoryFragment(){
        onBackPressed()
    }

    fun navigateMedicineDetailsEditToInventoryFragment(){
        onBackPressed()
    }

    fun navigateLocalFragmentToDispensingFragment(id : Int){
        navController.navigate(R.id.action_inventoryFragment_to_dispensingFragment)
    }


}