package com.devscore.digital_pharmacy.presentation.sales.orderlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.SalesOrder
import com.devscore.digital_pharmacy.business.domain.util.StateMessageCallback
import com.devscore.digital_pharmacy.presentation.sales.BaseSalesFragment
import com.devscore.digital_pharmacy.presentation.sales.SalesActivity
import com.devscore.digital_pharmacy.presentation.util.TopSpacingItemDecoration
import com.devscore.digital_pharmacy.presentation.util.processQueue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_local.*
import kotlinx.android.synthetic.main.fragment_sales_orders.*
import kotlinx.android.synthetic.main.inventory_details_dialog.*

@AndroidEntryPoint
class SalesOrdersFragment : BaseSalesFragment(),
    SalesOrderAdapter.Interaction {


    private lateinit var searchView: SearchView
    private var recyclerAdapter: SalesOrderAdapter? = null // can leak memory so need to null
    private val viewModel: SalesOrderListViewModel by viewModels()
    private lateinit var menu: Menu


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sales_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)
        initRecyclerView()
        initUIClick()
        subscribeObservers()
    }

    private fun initUIClick() {

//        localFragmentSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextChange(newText: String): Boolean {
//                // your text view here
////                textView.text = newText
//                executeNewQuery(newText)
//                return true
//            }
//
//            override fun onQueryTextSubmit(query: String): Boolean {
////                textView.text = query
//                executeNewQuery(query)
//                return true
//            }
//        })


        newSalesOrder.setOnClickListener {
            (activity as SalesActivity).navigateSalesOrderToInventoryFragment()
        }
    }

    private fun subscribeObservers(){
        viewModel.state.observe(viewLifecycleOwner, { state ->

//            uiCommunicationListener.displayProgressBar(state.isLoading)

            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object: StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(SalesOrderListEvents.OnRemoveHeadFromQueue)
                    }
                })

            recyclerAdapter?.apply {
                submitList(list = state.orderList, state.isLoading, state.isQueryExhausted)
            }
        })
    }

    private fun executeNewQuery(query: String){
        resetUI()
        viewModel.onTriggerEvent(SalesOrderListEvents.UpdateQuery(query))
        viewModel.onTriggerEvent(SalesOrderListEvents.SearchOrders)
    }

    private  fun resetUI(){
//        uiCommunicationListener.hideSoftKeyboard()
//        focusableView.requestFocus()
    }

    private fun initRecyclerView(){
        salesOrdersRvId.apply {
            layoutManager = LinearLayoutManager(this@SalesOrdersFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(0)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = SalesOrderAdapter(this@SalesOrdersFragment)
            addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    Log.d(TAG, "onScrollStateChanged: exhausted? ${viewModel.state.value?.isQueryExhausted}")
                    if (
                        lastPosition == recyclerAdapter?.itemCount?.minus(1)
                        && viewModel.state.value?.isLoading == false
                        && viewModel.state.value?.isQueryExhausted == false
                    ) {
                        Log.d(TAG, "GlobalFragment: attempting to load next page...")
                        viewModel.onTriggerEvent(SalesOrderListEvents.NextPage)
                    }
                }
            })
            adapter = recyclerAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        recyclerAdapter = null
    }

    override fun onItemSelected(position: Int, item: SalesOrder) {
        viewModel.onTriggerEvent(SalesOrderListEvents.SalesCompleted(item))
    }


    override fun restoreListPosition() {
    }

    override fun nextPage() {
//        viewModel.onTriggerEvent(LocalMedicineEvents.NewLocalMedicineSearch)
    }



    fun oderDetails(item: SalesOrder) {
//        val dialog = MaterialDialog(requireContext())
//        dialog.cancelable(false)
//        dialog.setContentView(R.layout.inventory_details_dialog)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.productDetailsBrandName.setText(item.brand_name)
//        dialog.productDetailsMenufactureName.setText(item.manufacture)
//        dialog.productDetailsCompanyName.setText(item.generic)
//        dialog.productDetailsMRPValue.setText(item.mrp.toString())
//        dialog.productDetailsCloseButton.setOnClickListener {
//            dialog.dismiss()
//        }
//        dialog.productDetailsCloseIcon.setOnClickListener {
//            dialog.dismiss()
//        }
//        dialog.show()
    }

}