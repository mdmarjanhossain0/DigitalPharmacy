package com.devscore.digital_pharmacy.presentation.supplier.createsupplier

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.business.domain.models.MedicineUnits
import com.devscore.digital_pharmacy.business.domain.models.Supplier
import com.devscore.digital_pharmacy.business.domain.util.MedicineProperties
import com.devscore.digital_pharmacy.business.domain.util.StateMessageCallback
import com.devscore.digital_pharmacy.presentation.inventory.BaseInventoryFragment
import com.devscore.digital_pharmacy.presentation.inventory.add.addmedicine.AddMedicineEvents
import com.devscore.digital_pharmacy.presentation.inventory.add.addmedicine.AddMedicineViewModel
import com.devscore.digital_pharmacy.presentation.supplier.BaseSupplierFragment
import com.devscore.digital_pharmacy.presentation.util.processQueue
import kotlinx.android.synthetic.main.add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_add_product_sub_medicine.*
import kotlinx.android.synthetic.main.fragment_create_supplier.*


class CreateSupplierFragment : BaseSupplierFragment() {

    private val viewModel: SupplierCreateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_create_supplier, container, false)
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
        createSupplier.setOnClickListener {
            cacheState()
            viewModel.onTriggerEvent(SupplierCreateEvents.NewSupplierCreate)
        }

        createSupplierClear.setOnClickListener {
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
                        viewModel.onTriggerEvent(SupplierCreateEvents.OnRemoveHeadFromQueue)
                    }
                })
            Log.d(TAG, state.supplier.toString())
        })
    }


    fun cacheState() {
        try {
            val company_name = supplierCompanyName.text.toString()
            val agent_name = suppliertName.text.toString()
            val email = supplierEmail.text.toString()
            val mobile = supplierContactNumber.text.toString()
            val whatsapp = supplierWhatsapp.text.toString()
            val facebook = supplierFacebook.text.toString()
            val imo = supplierImo.text.toString()
            val address = supplierAddressFirst.text.toString() + " " + supplierAddressSecond.text.toString()

            val supplier = Supplier(
                company_name = company_name,
                agent_name = agent_name,
                email = email,
                mobile = mobile,
                whatsapp = whatsapp,
                facebook = facebook,
                imo = imo,
                address = address
            )
            viewModel.onTriggerEvent(SupplierCreateEvents.CacheState(supplier))
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
        supplierCompanyName.text.clear()
        suppliertName.text.clear()
        supplierEmail.text.clear()
        supplierContactNumber.text.clear()
        supplierWhatsapp.text.clear()
        supplierFacebook.text.clear()
        supplierImo.text.clear()
        supplierAddressFirst.text.clear()
        supplierAddressSecond.text.clear()

        val supplier = Supplier(
            company_name = "",
            agent_name = "",
            email = "",
            mobile = "",
            whatsapp = "",
            facebook = "",
            imo = "",
            address = ""
        )
        viewModel.onTriggerEvent(SupplierCreateEvents.CacheState(supplier))
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