package com.devscore.digital_pharmacy.presentation.auth.register

import com.devscore.digital_pharmacy.business.domain.util.Queue
import com.devscore.digital_pharmacy.business.domain.util.StateMessage


data class RegisterState(
    val isLoading: Boolean = false,
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
