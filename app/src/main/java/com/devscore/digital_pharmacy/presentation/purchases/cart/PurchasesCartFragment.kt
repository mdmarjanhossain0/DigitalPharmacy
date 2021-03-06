package com.devscore.digital_pharmacy.presentation.purchases.cart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.MedicineUnits
import com.devscore.digital_pharmacy.business.domain.models.PurchasesCart
import com.devscore.digital_pharmacy.business.domain.models.SalesCart
import com.devscore.digital_pharmacy.business.domain.util.StateMessageCallback
import com.devscore.digital_pharmacy.presentation.purchases.BasePurchasesFragment
import com.devscore.digital_pharmacy.presentation.sales.BaseSalesFragment
import com.devscore.digital_pharmacy.presentation.sales.SalesActivity
import com.devscore.digital_pharmacy.presentation.sales.card.SalesCardAdapter
import com.devscore.digital_pharmacy.presentation.sales.card.SalesCardEvents
import com.devscore.digital_pharmacy.presentation.sales.card.SalesCardState
import com.devscore.digital_pharmacy.presentation.sales.card.SalesCardViewModel
import com.devscore.digital_pharmacy.presentation.util.TopSpacingItemDecoration
import com.devscore.digital_pharmacy.presentation.util.processQueue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_purchase_new_order.*
import kotlinx.android.synthetic.main.fragment_sales_cart.*
import kotlinx.android.synthetic.main.fragment_sales_cart.searchViewId
import kotlinx.coroutines.*


@AndroidEntryPoint
class PurchasesCartFragment : BasePurchasesFragment(),
    PurchasesCartAdapter.Interaction {


    private var recyclerAdapter: PurchasesCartAdapter? = null // can leak memory so need to null
    private val viewModel: PurchasesCartViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_purchase_new_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)
        initRecyclerView()
        initUIClick()
        subscribeObservers()
        Log.d(TAG, "SalesCartFragment ViewModel " + viewModel.toString())
    }

    private fun initUIClick() {

        purchasesCartGenerate.setOnClickListener {
            if (viewModel.state.value?.purchasesCartList?.size!! < 1) {
                notItemAvailableInCart()
                return@setOnClickListener
            }
            findNavController().navigate(R.id.action_purchasesCartFragment_to_purchasesPaymentFragment)
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
                        viewModel.onTriggerEvent(PurchasesCartEvents.OnRemoveHeadFromQueue)
                    }
                })

            recyclerAdapter?.apply {
                submitList(list = state.purchasesCartList, state.isLoading, state.isQueryExhausted)
            }

            purchasesCartItemCount.setText("Items : " + state.purchasesCartList.size.toString())
            purchasesCartTotalAmount.setText("Total : ???" + state.totalAmount.toString())

            for (item in state.purchasesCartList) {
                Log.d(TAG, "Quantity " + item.quantity)
            }
        })
    }

    private fun executeNewQuery(query: String){
        resetUI()
        viewModel.onTriggerEvent(PurchasesCartEvents.UpdateQuery(query))
        viewModel.onTriggerEvent(PurchasesCartEvents.GenerateNewOrder)
    }

    private  fun resetUI(){
//        uiCommunicationListener.hideSoftKeyboard()
//        focusableView.requestFocus()
    }

    private fun initRecyclerView(){
        purchaseNewOrderRvId.apply {
            layoutManager = LinearLayoutManager(this@PurchasesCartFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(0)
            removeItemDecoration(topSpacingDecorator)
            addItemDecoration(topSpacingDecorator)
            recyclerAdapter = PurchasesCartAdapter(this@PurchasesCartFragment)
            adapter = recyclerAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        recyclerAdapter = null
    }

    override fun onItemSelected(position: Int, item: PurchasesCart) {
        oderDetails(item)
    }

    override fun onChangeUnit(position: Int, item: PurchasesCart, unit: MedicineUnits, quantity : Int) {
        viewModel.onTriggerEvent(PurchasesCartEvents.ChangeUnit(item, unit, quantity!!))
    }


    override fun restoreListPosition() {
    }

    override fun nextPage() {
//        viewModel.onTriggerEvent(LocalMedicineEvents.NewLocalMedicineSearch)
    }



    fun oderDetails(item: PurchasesCart) {
    }



    fun notItemAvailableInCart() {
        val dialog = MaterialDialog(requireContext())
            .show {
                title(R.string.Warning)
                message(text = "No item available in cart")
                negativeButton {
                    dismiss()
                }
                onDismiss {
                }
                cancelable(false)
            }


        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            withContext(Dispatchers.Main){
                if (dialog == null) {
                    return@withContext
                }
                dialog.dismiss()
            }
        }
    }


    override fun alertDialog(item : PurchasesCart, message : String) {
        val dialog = MaterialDialog(requireContext())
            .show {
                title(R.string.Warning)
                message(text = item.medicine?.brand_name + "..." + message)
                negativeButton {
                    dismiss()
                }
                onDismiss {
                }
                cancelable(false)
            }


        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            withContext(Dispatchers.Main){
                if (dialog == null) {
                    return@withContext
                }
                dialog.dismiss()
            }
        }
    }
}