package com.jyh.realtimetranslation.presentation.people

import com.jyh.realtimetranslation.data.entity.person.PersonEntity

sealed class PeopleState{
    object UnInitialized: PeopleState()

    object Loading: PeopleState()

    data class Success(
        val people: List<PersonEntity>
    ): PeopleState()

    object Error: PeopleState()
}