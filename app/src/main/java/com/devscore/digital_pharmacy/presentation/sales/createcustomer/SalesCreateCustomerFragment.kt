package com.devscore.digital_pharmacy.presentation.sales.createcustomer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.Customer
import com.devscore.digital_pharmacy.business.domain.util.StateMessageCallback
import com.devscore.digital_pharmacy.presentation.customer.BaseCustomerFragment
import com.devscore.digital_pharmacy.presentation.customer.createcustomer.CreateCustomerEvents
import com.devscore.digital_pharmacy.presentation.customer.createcustomer.CreateCustomerViewModel
import com.devscore.digital_pharmacy.presentation.sales.BaseSalesFragment
import com.devscore.digital_pharmacy.presentation.sales.card.SalesCardEvents
import com.devscore.digital_pharmacy.presentation.sales.card.SalesCardViewModel
import com.devscore.digital_pharmacy.presentation.util.processQueue
import kotlinx.android.synthetic.main.fragment_add_customer.*
import kotlinx.android.synthetic.main.fragment_add_customer.createCustomer
import kotlinx.android.synthetic.main.fragment_add_customer.createCustomerClear
import kotlinx.android.synthetic.main.fragment_add_customer.customerAddress
import kotlinx.android.synthetic.main.fragment_add_customer.customerContactNumber
import kotlinx.android.synthetic.main.fragment_add_customer.customerDateOfBirth
import kotlinx.android.synthetic.main.fragment_add_customer.customerEmail
import kotlinx.android.synthetic.main.fragment_add_customer.customerFacebook
import kotlinx.android.synthetic.main.fragment_add_customer.customerImo
import kotlinx.android.synthetic.main.fragment_add_customer.customerName
import kotlinx.android.synthetic.main.fragment_add_customer.customerWhatsapp
import kotlinx.android.synthetic.main.fragment_sales_create_customer.*
import kotlinx.coroutines.*

class SalesCreateCustomerFragment : BaseSalesFragment() {

    private val viewModel: CreateCustomerViewModel by viewModels()
    private val shareViewModel : SalesCardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sales_create_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)
        initUIClick()
        initUI()
        subscribeObservers()
    }

    private fun initUI() {
    }

    private fun initUIClick() {
        createCustomer.setOnClickListener {
            cacheState()
            viewModel.onTriggerEvent(CreateCustomerEvents.NewCustomerCreate)
        }

        createCustomerClear.setOnClickListener {
            clearWarning()
        }


        okSelect.setOnClickListener {
            if (viewModel.state.value?.customer?.pk!! > 0) {
                shareViewModel.onTriggerEvent(SalesCardEvents.SelectCustomer(viewModel.state.value?.customer!!))
                findNavController().popBackStack()
            }
            else {
                selectWarning()
            }
        }
    }

    private fun subscribeObservers(){
        viewModel.state.observe(viewLifecycleOwner, { state ->

            uiCommunicationListener.displayProgressBar(state.isLoading)

            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object: StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(CreateCustomerEvents.OnRemoveHeadFromQueue)
                    }
                })
            Log.d(TAG, state.customer.toString())
        })
    }


    fun cacheState() {
        try {
            val name = customerName.text.toString()
            val email = customerEmail.text.toString()
            val mobile = customerContactNumber.text.toString()
            val whatsapp = customerWhatsapp.text.toString()
            val facebook = customerFacebook.text.toString()
            val imo = customerImo.text.toString()
            val date_of_birth = customerDateOfBirth.text.toString()
            val address = customerAddress.text.toString()

            val customer = Customer(
                name = name,
                email = email,
                mobile = mobile,
                whatsapp = whatsapp,
                facebook = facebook,
                imo = imo,
                address = address,
                date_of_birth = date_of_birth,
            )
            viewModel.onTriggerEvent(CreateCustomerEvents.CacheState(customer))
        }
        catch (e : Exception) {
            MaterialDialog(requireContext())
                .show{
                    title(R.string.text_info)
                    message(text = "Something is wrong")
                    onDismiss {
                    }
                    cancelable(true)
                }
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
    }


    fun clearForm() {
        customerName.text.clear()
        customerEmail.text.clear()
        customerContactNumber.text.clear()
        customerWhatsapp.text.clear()
        customerFacebook.text.clear()
        customerImo.text.clear()
        customerAddress.text.clear()
        customerDateOfBirth.text.clear()

        val customer = Customer(
            name = "",
            email = "",
            mobile = "",
            whatsapp = "",
            facebook = "",
            imo = "",
            address = "",
            date_of_birth = "",
            total_balance = 0,
            due_balance = 0
        )
        viewModel.onTriggerEvent(CreateCustomerEvents.CacheState(customer))
    }



    fun clearWarning() {
        MaterialDialog(requireContext())
            .show{
                title(R.string.are_you_sure)
                message(text = "Are you sure to clear the form")
                positiveButton(R.string.text_ok){
                    clearForm()
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

    private fun selectWarning() {
        val dialog = MaterialDialog(requireContext())
            .show{
                title(R.string.Warning)
                message(text = "Customer not available Now...")
                negativeButton {
                    dismiss()
                }
                onDismiss {
                }
                cancelable(true)
            }
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            withContext(Dispatchers.Main) {
                dialog.dismiss()
            }
        }
    }

}