package com.github.lucascalheiros.common_test.data

import com.github.lucascalheiros.common.model.interfaces.BookLibAccount

object MockBookLibAccountData {
    val bookLibAccount1 = object : BookLibAccount {
        override val id: String
            get() = "TEST_ID"
        override val name: String
            get() = "Mocked Test Name"
        override val photoUrl: String
            get() = "mockedtesturl"
        override val email: String
            get() = "test@test.com"
    }
}