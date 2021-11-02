package com.devscore.digital_pharmacy.presentation.inventory.add.addmedicine

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.GlobalMedicine
import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.business.domain.models.MedicineUnits
import com.devscore.digital_pharmacy.business.domain.util.MedicineProperties.Companion.OTHER
import com.devscore.digital_pharmacy.business.domain.util.StateMessageCallback
import com.devscore.digital_pharmacy.presentation.inventory.BaseInventoryFragment
import com.devscore.digital_pharmacy.presentation.inventory.add.InventoryAddProductFragmentArgs
import com.devscore.digital_pharmacy.presentation.util.processQueue
import kotlinx.android.synthetic.main.add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_add_product_sub_medicine.*
import kotlinx.android.synthetic.main.fragment_local.*
import kotlinx.android.synthetic.main.item_dispensing.view.*


class AddProductSubMedicineFragment : BaseInventoryFragment() {

    private val viewModel: AddMedicineViewModel by viewModels()


    private lateinit var unitName : String
    private var unitCount : Int = 0


    private val unitList = ArrayList<MedicineUnits>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_product_sub_medicine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)
        initUIClick()
        initUI()
        getData()
        subscribeObservers()
    }

    private fun getData() {
        val id = arguments?.getInt("id", -1)
        Log.d(TAG, arguments.toString())
        Log.d(TAG, "id " + id.toString())
        if (id != -1) {
            viewModel.onTriggerEvent(AddMedicineEvents.UpdateId(id!!))
            viewModel.onTriggerEvent(AddMedicineEvents.FetchData)
        }
    }

    private fun initUI() {
//        val kindList = ArrayList<String>()
//        kindList.add("Human")
//        kindList.add("Veterinary")
//        val kindAdapter = ArrayAdapter(
//            requireContext(),
//            android.R.layout.simple_spinner_item,
//            kindList
//        )
//
//        kindAdapter.setDropDownViewResource(
//            android.R.layout
//                .simple_spinner_dropdown_item
//        )
//
//        kindET.setAdapter(kindAdapter)
//
//        val formList = ArrayList<String>()
//        formList.add("TABLET")
//        formList.add("LIQUITE")
//        formList.add("OTHER")
//        val formAdapter = ArrayAdapter(
//            requireContext(),
//            android.R.layout.simple_spinner_item,
//            formList
//        )
//
//        formAdapter.setDropDownViewResource(
//            android.R.layout
//                .simple_spinner_dropdown_item
//        )
//
//        formET.setAdapter(formAdapter)

    }

    private fun initUIClick() {
        addNewMeasureUnit.setOnClickListener {
            addMeasuringUnit()
        }

        minimumStockIncrementTV.setOnClickListener {
            val value = minimumStockCount.text.toString().toInt() + 1
            minimumStockCount.setText(value.toString())
        }

        minimumStockDecrementTV.setOnClickListener {
            val value = minimumStockCount.text.toString().toInt() - 1
            minimumStockCount.setText(value.toString())
        }


        addMedicineAdd.setOnClickListener {
            cacheState()
            viewModel.onTriggerEvent(AddMedicineEvents.NewAddMedicine)
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
                        viewModel.onTriggerEvent(AddMedicineEvents.OnRemoveHeadFromQueue)
                    }
                })
            Log.d(TAG, state.medicine.toString())



            if (state.globalMedicine != null) {
                updateField(state.globalMedicine)
            }

        })
    }

    private fun updateField(globalMedicine : GlobalMedicine?) {
        brand_nameET.setText(globalMedicine?.brand_name.toString())
        if (globalMedicine?.darNumber != null) {
            dar_mr_NumberET.setText(globalMedicine?.darNumber)
        }

        if (globalMedicine?.manufacture != null) {
            manufactureET.setText(globalMedicine.manufacture.toString())
        }

        if (globalMedicine?.generic != null) {
            genericET.setText(globalMedicine?.generic.toString())
        }

        kindET.setText("Human")
        formET.setText("TABLET")

        if (globalMedicine?.strength != null) {
            strengthET.setText(globalMedicine?.strength.toString())
        }
    }


    fun cacheState() {
        try {
            val brand_name = brand_nameET.text.toString()
            val dar_number = dar_mr_NumberET.text.toString()
            val manufacture = manufactureET.text.toString()
            val generic = genericET.text.toString()
//        val kind = kindET.text.toString()
            val kind = "Human"
//        val form = formET.text.toString()
            val form = "TABLET"
            val strength = strengthET.text.toString()
//        val salesUnit = salesUnit.text.toString()
//        val purchasesUnit = symtomORPurchasesUnit.text.toString()
            val mrp = mrpET.text.toString().toFloat()
            val purchases_price = purchases_price.text.toString().toFloat()
            val minimum_stock = minimumStockCount.text.toString().toInt()

            val local_medicine = LocalMedicine(
                id = -1,
                brand_name = brand_name,
                sku = null,
                dar_number = dar_number,
                manufacture = manufacture,
                generic = generic,
                indication = null,
                symptom = null,
                description = null,
                kind = kind,
                form = form,
                strength = strength,
                mrp = mrp,
                purchases_price = purchases_price,
                units = unitList
            )
            viewModel.onTriggerEvent(AddMedicineEvents.CacheState(local_medicine))
        }
        catch (e : Exception) {
            MaterialDialog(requireContext())
                .show{
                    title(R.string.text_info)
                    message(text = "Some thing is wrong")
                    onDismiss {
                    }
                    cancelable(true)
                }
        }

    }



    override fun onDestroyView() {
        cacheState()
        super.onDestroyView()
    }


    fun addMeasuringUnit() {
        val dialog = MaterialDialog(requireContext())
        dialog.cancelable(false)
        dialog.setContentView(R.layout.add_product_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var count = dialog.unitCount.text.toString().toInt()
        dialog.unitCountIncrementTV.setOnClickListener {
            count = count + 1
            dialog.unitCount.setText(count.toString())
        }

        dialog.unitCountDecrementTV.setOnClickListener {
            count = count - 1
            dialog.unitCount.setText(count.toString())
        }
        dialog.addMedicineClear.setOnClickListener {
            dialog.dismiss()
        }
        dialog.addMedicineUnitAdd.setOnClickListener {
            val unitName = dialog.unitNameET.text.toString()
            if (unitName == null || count < 1) {
                Toast.makeText(context, "Something is wrong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            this.unitName = unitName
            this.unitCount = count


            unitList.add(
                MedicineUnits(
                    id = -1,
                    name = unitName,
                    quantity = unitCount,
                    type = OTHER
                )
            )
            updateSpinner()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun updateSpinner() {
        val newList = ArrayList<String>()
        for (a in unitList) {
            newList.add(a.name)
        }
        val kindAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            newList
        )

        kindAdapter.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        )

        salesUnit.setAdapter(kindAdapter)
        symtomORPurchasesUnit.setAdapter(kindAdapter)
    }

}