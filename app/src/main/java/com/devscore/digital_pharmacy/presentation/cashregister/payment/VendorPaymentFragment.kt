package com.devscore.digital_pharmacy.presentation.cashregister.payment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.Payment
import com.devscore.digital_pharmacy.business.domain.util.StateMessageCallback
import com.devscore.digital_pharmacy.presentation.cashregister.BaseCashRegisterFragment
import com.devscore.digital_pharmacy.presentation.util.processQueue
import kotlinx.android.synthetic.main.fragment_vendor_payment.*


class VendorPaymentFragment : BaseCashRegisterFragment() {

    private val viewModel: PaymentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_vendor_payment, container, false)
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
        paymentAdd.setOnClickListener {
            cacheState()
            viewModel.onTriggerEvent(PaymentEvents.NewPaymentCreate)
        }

        paymentClear.setOnClickListener {
            clearWarning()
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
                        viewModel.onTriggerEvent(PaymentEvents.OnRemoveHeadFromQueue)
                    }
                })
            Log.d(TAG, state.payment.toString())
        })
    }


    fun cacheState() {
        try {
            val date = paymentDate.text.toString()
            val type = paymentTypeTV.text.toString()
            val amount = paymentAmount.text.toString().toFloat()
            val balance = paymentBalance.text.toString().toFloat()
            val remark = paymentRemark1.text.toString() + " " + paymentRemark2.text.toString()

            val payment = Payment(
                pk = -1,
                room_id = -1,
                date = date,
                customer = -1,
                vendor = -1,
                type = type,
                amount = amount,
                balance = balance,
                remarks = remark,
                created_at = "",
                updated_at = "",
                customer_name = "",
                vendor_name = ""
            )
            viewModel.onTriggerEvent(PaymentEvents.CacheState(payment))
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
        paymentDate.text.clear()
        paymentTypeTV.setText("")
        paymentAmount.text.clear()
        paymentBalance.text.clear()
        paymentRemark1.text.clear()
        paymentRemark2.text.clear()

        val payment = Payment(
            pk = -1,
            room_id = -1,
            date = "",
            customer = -1,
            vendor = -1,
            type = "",
            amount = 0f,
            balance = 0f,
            remarks = "",
            created_at = "",
            updated_at = "",
            customer_name = "",
            vendor_name = ""
        )
        viewModel.onTriggerEvent(PaymentEvents.CacheState(payment))
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

}