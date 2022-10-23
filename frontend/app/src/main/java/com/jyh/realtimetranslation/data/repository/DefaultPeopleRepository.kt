package com.jyh.realtimetranslation.data.repository

import com.jyh.realtimetranslation.data.entity.person.PersonEntity
import com.jyh.realtimetranslation.data.network.PeopleAPIService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultPeopleRepository(
    private val peopleAPI: PeopleAPIService,
    private val ioDispatcher: CoroutineDispatcher
) : PeopleRepository {
    override suspend fun getPeople(): List<PersonEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun addPerson(userId: String, otherId: String) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePerson(userId: String, otherId: String) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }
}