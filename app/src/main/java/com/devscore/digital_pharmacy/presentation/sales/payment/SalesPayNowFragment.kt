package com.devscore.digital_pharmacy.presentation.sales.payment

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
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.SalesCart
import com.devscore.digital_pharmacy.business.domain.models.SalesOrderMedicine
import com.devscore.digital_pharmacy.business.domain.util.StateMessageCallback
import com.devscore.digital_pharmacy.presentation.sales.BaseSalesFragment
import com.devscore.digital_pharmacy.presentation.sales.SalesActivity
import com.devscore.digital_pharmacy.presentation.sales.card.SalesCardAdapter
import com.devscore.digital_pharmacy.presentation.sales.card.SalesCardEvents
import com.devscore.digital_pharmacy.presentation.sales.card.SalesCardState
import com.devscore.digital_pharmacy.presentation.sales.card.SalesCardViewModel
import com.devscore.digital_pharmacy.presentation.util.TopSpacingItemDecoration
import com.devscore.digital_pharmacy.presentation.util.processQueue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_sales_cart.*
import kotlinx.android.synthetic.main.fragment_sales_cart.searchViewId
import kotlinx.android.synthetic.main.fragment_sales_pay_now.*
import kotlinx.android.synthetic.main.item_sales_list.*
import kotlinx.coroutines.*


@AndroidEntryPoint
class SalesPayNowFragment : BaseSalesFragment(){


    private var recyclerAdapter: SalesOrdersAdapter? = null // can leak memory so need to null
    private var recyclerItemAdapter: SalesOrderItemAdapter? = null
    private val viewModel: SalesCardViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sales_pay_now, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)
        initRecyclerView()
        initUIClick()
        subscribeObservers()
        Log.d(TAG, "SalesPayNowFragment ViewModel " + viewModel.toString())
    }

    private fun initUIClick() {
        createSalesOrder.setOnClickListener {
            viewModel.onTriggerEvent(SalesCardEvents.GenerateNewOrder)
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
                        viewModel.onTriggerEvent(SalesCardEvents.OnRemoveHeadFromQueue)
                    }
                })

            recyclerAdapter?.apply {
                submitList(list = state.salesCartList)
            }

            salesPaymentItemCount.setText("Items : " + state.order.sales_oder_medicines!!.size.toString())
            salesPaymentTotal.setText("Total : ৳" + state.order.total_amount.toString())
        })
    }

    private fun executeNewQuery(query: String){
        resetUI()
        viewModel.onTriggerEvent(SalesCardEvents.UpdateQuery(query))
        viewModel.onTriggerEvent(SalesCardEvents.GenerateNewOrder)
    }

    private  fun resetUI(){
        uiCommunicationListener.hideSoftKeyboard()
    }

    private fun initRecyclerView(){
        salesPaymentRvId.apply {
            layoutManager = LinearLayoutManager(this@SalesPayNowFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(0)
            removeItemDecoration(topSpacingDecorator)
            addItemDecoration(topSpacingDecorator)
            recyclerAdapter = SalesOrdersAdapter(context)
            adapter = recyclerAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        recyclerAdapter = null
    }




    fun backPressWarning() {
        MaterialDialog(requireContext())
            .show{
                title(R.string.are_you_sure)
                message(text = "Cart item will be dismiss")
                positiveButton(R.string.text_ok){
                    viewModel.state.value = SalesCardState()
                    findNavController().popBackStack()
                    dismiss()
                }
                negativeButton {
                    dismiss()
                }
                onDismiss {
                }
                cancelable(false)
            }
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
}