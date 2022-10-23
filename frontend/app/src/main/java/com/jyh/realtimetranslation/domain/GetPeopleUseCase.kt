package com.jyh.realtimetranslation.domain

import com.jyh.realtimetranslation.data.entity.person.PersonEntity
import com.jyh.realtimetranslation.data.repository.PeopleRepository

internal class GetPeopleUseCase(
    private val peopleRepository: PeopleRepository
) : UseCase {
    suspend operator fun invoke(): List<PersonEntity> {
        return peopleRepository.getPeople()
    }
}