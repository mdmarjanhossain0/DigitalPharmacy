package com.devscore.digital_pharmacy.presentation.customer.customerlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.Customer
import com.devscore.digital_pharmacy.business.domain.models.Supplier
import com.devscore.digital_pharmacy.business.domain.util.StateMessageCallback
import com.devscore.digital_pharmacy.presentation.customer.BaseCustomerFragment
import com.devscore.digital_pharmacy.presentation.inventory.InventoryActivity
import com.devscore.digital_pharmacy.presentation.supplier.BaseSupplierFragment
import com.devscore.digital_pharmacy.presentation.supplier.supplierlist.SupplierEvents
import com.devscore.digital_pharmacy.presentation.supplier.supplierlist.SupplierListAdapter
import com.devscore.digital_pharmacy.presentation.supplier.supplierlist.SupplierListViewModel
import com.devscore.digital_pharmacy.presentation.util.TopSpacingItemDecoration
import com.devscore.digital_pharmacy.presentation.util.processQueue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_customers_list.*
import kotlinx.android.synthetic.main.fragment_supplier_list.*
import kotlinx.android.synthetic.main.fragment_supplier_list.searchViewId

@AndroidEntryPoint
class CustomersListFragment : BaseCustomerFragment(),
    CustomerListAdapter.Interaction{


    private var recyclerAdapter: CustomerListAdapter? = null // can leak memory so need to null
    private val viewModel: CustomerListVIewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_customers_list, container, false)
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

        searchViewId.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                // your text view here
//                textView.text = newText
                executeNewQuery(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
//                textView.text = query
                executeNewQuery(query)
                return true
            }
        })


        supplierFloatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_customersListFragment_to_addCustomerFragment)
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
                        viewModel.onTriggerEvent(CustomerListEvents.OnRemoveHeadFromQueue)
                    }
                })

            recyclerAdapter?.apply {
                submitList(list = state.customerList)
            }
        })
    }

    private fun executeNewQuery(query: String){
        resetUI()
        viewModel.onTriggerEvent(CustomerListEvents.UpdateQuery(query))
        viewModel.onTriggerEvent(CustomerListEvents.NewSearchCustomer)
    }

    private  fun resetUI(){
//        uiCommunicationListener.hideSoftKeyboard()
//        focusableView.requestFocus()
    }

    private fun initRecyclerView(){
        customerRvId.apply {
            layoutManager = LinearLayoutManager(this@CustomersListFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(0)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = CustomerListAdapter(this@CustomersListFragment)
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
                        viewModel.onTriggerEvent(CustomerListEvents.NextPage)
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

    override fun onItemSelected(position: Int, item: Customer) {
        findNavController().navigate(R.id.action_customersListFragment_to_customerPreviousOrdersFragment)
    }

    override fun onItemReturnSelected(position: Int, item: Customer) {
    }

    override fun onItemDeleteSelected(position: Int, item: Customer) {
    }

    override fun restoreListPosition() {
    }

    override fun nextPage() {
//        viewModel.onTriggerEvent(LocalMedicineEvents.NewLocalMedicineSearch)
    }


}