package com.jyh.realtimetranslation.data.repository

import com.google.firebase.database.DatabaseReference
import com.jyh.realtimetranslation.data.entity.person.PersonEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PeopleRepositoryImpl(
    private val reference: DatabaseReference,
    private val ioDispatcher: CoroutineDispatcher
): PeopleRepository {
    override suspend fun getPeople(): List<PersonEntity> = withContext(ioDispatcher){
        val list = mutableListOf<PersonEntity>()

        return@withContext list.toList()
    }

    override suspend fun addPerson(userId: String, otherId: String) {
    }

    override suspend fun deletePerson(userId: String, otherId: String) {
    }
}