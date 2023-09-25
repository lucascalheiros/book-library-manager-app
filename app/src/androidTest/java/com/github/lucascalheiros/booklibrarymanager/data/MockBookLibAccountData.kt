package com.github.lucascalheiros.booklibrarymanager.data

import com.github.lucascalheiros.data_authentication.domain.model.BookLibAccount

object MockBookLibAccountData {
    val bookLibAccount1 = object :
        BookLibAccount {
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