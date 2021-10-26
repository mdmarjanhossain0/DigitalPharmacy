package com.devscore.digital_pharmacy.presentation.inventory.local

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.afollestad.materialdialogs.MaterialDialog
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.datasource.network.inventory.InventoryApiService
import com.devscore.digital_pharmacy.business.domain.models.LocalMedicine
import com.devscore.digital_pharmacy.business.domain.util.StateMessageCallback
import com.devscore.digital_pharmacy.presentation.inventory.BaseInventoryFragment
import com.devscore.digital_pharmacy.presentation.inventory.InventoryActivity
import com.devscore.digital_pharmacy.presentation.util.TopSpacingItemDecoration
import com.devscore.digital_pharmacy.presentation.util.processQueue
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.*
import kotlinx.android.synthetic.main.add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_global.*
import kotlinx.android.synthetic.main.fragment_local.*
import kotlinx.android.synthetic.main.inventory_details_dialog.*
import javax.inject.Inject
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers.io
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class LocalFragment : BaseInventoryFragment(),
    LocalAdapter.Interaction {


    @Inject
    lateinit var inventoryApiService: InventoryApiService


    private lateinit var searchView: SearchView
    private var recyclerAdapter: LocalAdapter? = null // can leak memory so need to null
    private val viewModel: LocalMedicineViewModel by viewModels()
    private val disposables = CompositeDisposable()
    private val timeSinceLastRequest: Long = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_local, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)
        initRecyclerView()
        initUIClick()
        bouncingSearch()
        subscribeObservers()
    }

    private fun initUIClick() {
/*        CoroutineScope(IO).launch {
            val result = inventoryApiService.addMedicine(
                "Token 0c58549b616cba8e1f39a4ed1c86b019b52ea764",
                AddMedicine(
                    brand_name = "Paracitamol7",
                    sku = null,
                    dar_number = null,
                    mr_number = null,
                    generic = null,
                    indication = null,
                    symptom = null,
                    strength = null,
                    description = null,
                    mrp = null,
                    purchases_price = null,
                    discount = null,
                    is_percent_discount = false,
                    manufacture = null,
                    kind = null,
                    form = null,
                    remaining_quantity = null,
                    damage_quantity = null,
                    rack_number = null,
                    units = listOf<MedicineUnits>()
                )
            )
            Log.d(TAG, result.toString())
        }*/

//        localFragmentSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextChange(newText: String): Boolean {
//                executeNewQuery(newText)
//                return true
//            }
//
//            override fun onQueryTextSubmit(query: String): Boolean {
//                executeNewQuery(query)
//                return true
//            }
//        })


        localFragmentFloatingActionButton.setOnClickListener {
            (activity as InventoryActivity).navigateGlobalFragmentToAddMedicineContainerFragment()
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
                        viewModel.onTriggerEvent(LocalMedicineEvents.OnRemoveHeadFromQueue)
                    }
                })

            recyclerAdapter?.apply {
                submitList(medicineList = state.localMedicineList)
            }
        })
    }

    private fun executeNewQuery(query: String){
        resetUI()
        viewModel.onTriggerEvent(LocalMedicineEvents.UpdateQuery(query))
        viewModel.onTriggerEvent(LocalMedicineEvents.NewLocalMedicineSearch)
    }

    private  fun resetUI(){
//        uiCommunicationListener.hideSoftKeyboard()
//        focusableView.requestFocus()
    }

    private fun initRecyclerView(){
        localRvId.apply {
            layoutManager = LinearLayoutManager(this@LocalFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(0)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = LocalAdapter(this@LocalFragment)
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
                        viewModel.onTriggerEvent(LocalMedicineEvents.NextPage)
                    }
                }
            })
            adapter = recyclerAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        recyclerAdapter = null
        disposables.dispose()
    }

    override fun onItemSelected(position: Int, item: LocalMedicine) {
        localMedicineDetails(item)
    }

    override fun onItemReturnSelected(position: Int, item: LocalMedicine) {
        (activity as InventoryActivity).navigateLocalFragmentToReturnFragment()
    }

    override fun onItemDeleteSelected(position: Int, item: LocalMedicine) {
        (activity as InventoryActivity).navigateLocalFragmentToDispensingFragment(item.id!!)
    }

    override fun restoreListPosition() {
    }

    override fun nextPage() {
//        viewModel.onTriggerEvent(LocalMedicineEvents.NewLocalMedicineSearch)
    }



    fun localMedicineDetails(item: LocalMedicine) {
        val dialog = MaterialDialog(requireContext())
        dialog.cancelable(false)
        dialog.setContentView(R.layout.inventory_details_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.productDetailsBrandName.setText(item.brand_name)
        dialog.productDetailsMenufactureName.setText(item.manufacture)
        dialog.productDetailsCompanyName.setText(item.generic)
        dialog.productDetailsMRPValue.setText(item.mrp.toString())
        dialog.productDetailsCloseButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.productDetailsCloseIcon.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    fun bouncingSearch() {
        val searchQueryObservable = Observable.create(object : ObservableOnSubscribe<String>{
            override fun subscribe(emitter: ObservableEmitter<String>) {
                localFragmentSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(newText: String): Boolean {
                        if (!emitter.isDisposed) {
                            emitter.onNext(newText)
                        }
                        return true
                    }

                    override fun onQueryTextSubmit(query: String): Boolean {
                        executeNewQuery(query)
                        return true
                    }
                })
            }
        })
            .debounce(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(io())


        searchQueryObservable.subscribe(
            object : Observer<String>{
                override fun onSubscribe(d: Disposable) {
                    disposables.add(d)
                }

                override fun onNext(t: String) {
                    Log.d(TAG, t.toString())
                    CoroutineScope(Dispatchers.Main).launch {
                        executeNewQuery(t)
                    }
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }

            }
        )
    }

}