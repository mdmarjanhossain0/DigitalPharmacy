package com.devscore.digital_pharmacy.presentation.auth.register

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
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : BaseAuthFragment() {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerBtnId.setOnClickListener {
            register()
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            uiCommunicationListener.displayProgressBar(state.isLoading)
            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(RegisterEvents.OnRemoveHeadFromQueue)
                    }
                })
        }
    }

    private fun setRegisterFields(
        email: String,
        username: String,
        password: String,
        confirmPassword: String,
        businessName : String,
        mobile : String,
        license_key : String,
        address : String
    ){
        emailEtvId.setText(email)
        userNameEtvId.setText(username)
        passwordEtv.setText(password)
        password2Etv.setText(confirmPassword)
//        businessNameEtv.setText(businessName)
        phoneNumberEtvId.setText(mobile)
        pharmacyNumberEtvId.setText(license_key)
        addressLineEtvId1.setText(address)
    }

    private fun cacheState(){
        viewModel.onTriggerEvent(RegisterEvents.OnUpdateEmail(emailEtvId.text.toString()))
        viewModel.onTriggerEvent(RegisterEvents.OnUpdateUsername(userNameEtvId.text.toString()))
        viewModel.onTriggerEvent(RegisterEvents.OnUpdatePassword(passwordEtv.text.toString()))
        viewModel.onTriggerEvent(RegisterEvents.OnUpdateConfirmPassword(password2Etv.text.toString()))
//        viewModel.onTriggerEvent(RegisterEvents.OnUpdateBusinessName(""))
        viewModel.onTriggerEvent(RegisterEvents.OnUpdateMobile(phoneNumberEtvId.text.toString()))
        viewModel.onTriggerEvent(RegisterEvents.OnUpdateLicenseKey(pharmacyNumberEtvId.text.toString()))
        viewModel.onTriggerEvent(RegisterEvents.OnUpdateAddress(addressLineEtvId1.text.toString()))

    }

    private fun register() {
        cacheState()
        viewModel.onTriggerEvent(RegisterEvents.Register(
            email = emailEtvId.text.toString(),
            username = userNameEtvId.text.toString(),
            password = passwordEtv.text.toString(),
            confirmPassword = password2Etv.text.toString(),
//            business_name = "",
            mobile = phoneNumberEtvId.text.toString(),
            license_key = pharmacyNumberEtvId.text.toString(),
            address = addressLineEtvId1.text.toString()
        ))
    }

    override fun onPause() {
        super.onPause()
        cacheState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}