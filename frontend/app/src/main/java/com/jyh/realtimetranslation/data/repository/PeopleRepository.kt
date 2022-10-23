package com.jyh.realtimetranslation.data.repository

import com.jyh.realtimetranslation.data.entity.person.PersonEntity

interface PeopleRepository {
    suspend fun getPeople(): List<PersonEntity>

    suspend fun addPerson(userId:String, otherId:String)

    suspend fun deletePerson(userId:String, otherId:String)
}