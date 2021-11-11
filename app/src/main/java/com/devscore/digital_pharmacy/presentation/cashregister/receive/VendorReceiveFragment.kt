package com.devscore.digital_pharmacy.presentation.cashregister.receive

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
import com.devscore.digital_pharmacy.business.domain.models.Receive
import com.devscore.digital_pharmacy.business.domain.util.StateMessageCallback
import com.devscore.digital_pharmacy.presentation.cashregister.BaseCashRegisterFragment
import com.devscore.digital_pharmacy.presentation.util.processQueue
import kotlinx.android.synthetic.main.fragment_vendor_receive.*


class VendorReceiveFragment : BaseCashRegisterFragment() {

    private val viewModel: ReceiveViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_vendor_receive, container, false)
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
        receiveAdd.setOnClickListener {
            cacheState()
            viewModel.onTriggerEvent(ReceiveEvents.NewReceiveCreate)
        }

        receiveClear.setOnClickListener {
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
                        viewModel.onTriggerEvent(ReceiveEvents.OnRemoveHeadFromQueue)
                    }
                })
            Log.d(TAG, state.receive.toString())
        })
    }


    fun cacheState() {
        try {
            val date = receiveDate.text.toString()
            val type = receiveTypeTV.text.toString()
            val amount = receiveAmount.text.toString().toFloat()
            val balance = receiveBalance.text.toString().toFloat()
            val remark = receiveRemark1.text.toString() + " " + receiveRemark2.text.toString()

            val receive = Receive(
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
            viewModel.onTriggerEvent(ReceiveEvents.CacheState(receive))
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
        receiveDate.text.clear()
        receiveTypeTV.setText("")
        receiveAmount.text.clear()
        receiveBalance.text.clear()
        receiveRemark1.text.clear()
        receiveRemark2.text.clear()

        val receive = Receive(
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
        viewModel.onTriggerEvent(ReceiveEvents.CacheState(receive))
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