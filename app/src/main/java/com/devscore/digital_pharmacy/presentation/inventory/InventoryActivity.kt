package com.devscore.digital_pharmacy.presentation.inventory

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.devscore.digital_pharmacy.MainActivity
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.presentation.BaseActivity
import com.devscore.digital_pharmacy.sales.SalesFragment
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InventoryActivity : BaseActivity(), View.OnClickListener {

    private lateinit var navigationView: NavigationView
    var drawerLayout: DrawerLayout? = null


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    override fun displayProgressBar(isLoading: Boolean) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        setUpUI()

        navigationView = NavigationView(this)
        onSetNavigationDrawerEvents()

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
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerId, SalesFragment()).commit()
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


    fun navigateGlobalFragmentToAddMedicineContainerFragment(){
        navController.navigate(R.id.action_inventoryFragment_to_inventoryAddProductFragment)
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

    fun navigateLocalFragmentToDisposingFragment(){
        navController.navigate(R.id.action_inventoryFragment_to_dispensingFragment)
    }


}