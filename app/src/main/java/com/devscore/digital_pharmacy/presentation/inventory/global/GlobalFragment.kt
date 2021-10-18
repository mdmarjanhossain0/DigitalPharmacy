package com.devscore.digital_pharmacy.presentation.inventory.global

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.models.GlobalMedicine
import com.devscore.digital_pharmacy.business.domain.util.StateMessageCallback
import com.devscore.digital_pharmacy.presentation.inventory.BaseInventoryFragment
import com.devscore.digital_pharmacy.presentation.inventory.InventoryActivity
import com.devscore.digital_pharmacy.presentation.util.TopSpacingItemDecoration
import com.devscore.digital_pharmacy.presentation.util.processQueue
import kotlinx.android.synthetic.main.fragment_global.*

class GlobalFragment : BaseInventoryFragment(),
    GlobalAdapter.Interaction {

    private lateinit var searchView: SearchView
    private var recyclerAdapter: GlobalAdapter? = null // can leak memory so need to null
    private val viewModel: GlobalViewModel by viewModels()
    private lateinit var menu: Menu


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_global, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)
        initRecyclerView()
        subscribeObservers()
    }

    private fun subscribeObservers(){
        viewModel.state.observe(viewLifecycleOwner, { state ->

            uiCommunicationListener.displayProgressBar(state.isLoading)

            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object: StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(GlobalEvents.OnRemoveHeadFromQueue)
                    }
                })

            recyclerAdapter?.apply {
                submitList(blogList = state.globalMedicineList)
            }
        })
    }

    private fun executeNewQuery(query: String){
        resetUI()
    }

    private  fun resetUI(){
        uiCommunicationListener.hideSoftKeyboard()
//        focusableView.requestFocus()
    }

    private fun initRecyclerView(){
        globalRvId.apply {
            layoutManager = LinearLayoutManager(this@GlobalFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = GlobalAdapter(this@GlobalFragment)
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
                        viewModel.onTriggerEvent(GlobalEvents.NewMedicineSearch)
                    }
                }
            })
            adapter = recyclerAdapter
        }
    }

    override fun onItemSelected(position: Int, item: GlobalMedicine) {
    }

    override fun restoreListPosition() {
    }

    override fun nextPage() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerAdapter = null
    }
}