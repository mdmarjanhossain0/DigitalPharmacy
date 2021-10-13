package com.devscore.digital_pharmacy.presentation.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.codingwithmitch.openapi.presentation.util.processQueue
import com.devscore.digital_pharmacy.R
import com.devscore.digital_pharmacy.business.domain.util.StateMessageCallback
import com.devscore.digital_pharmacy.presentation.auth.BaseAuthFragment
import kotlinx.android.synthetic.main.fragment_launcher.*
import kotlinx.android.synthetic.main.fragment_launcher.loginBtnId
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseAuthFragment() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_login,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        loginBtnId.setOnClickListener {
            cacheState()
            login()
        }
    }

    fun subscribeObservers(){
        viewModel.state.observe(viewLifecycleOwner) { state ->
            uiCommunicationListener.displayProgressBar(state.isLoading)
            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(LoginEvents.OnRemoveHeadFromQueue)
                    }
                })
        }
    }

    private fun login(){
        viewModel.onTriggerEvent(
            LoginEvents.Login(
            email = phoneNumberEtvId.text.toString(),
            password = otpEtvId.text.toString()
        ))
    }

    private fun cacheState(){
        viewModel.onTriggerEvent(LoginEvents.OnUpdateEmail(phoneNumberEtvId.text.toString()))
        viewModel.onTriggerEvent(LoginEvents.OnUpdatePassword(otpEtvId.text.toString()))
    }

    override fun onPause() {
        super.onPause()
        cacheState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}