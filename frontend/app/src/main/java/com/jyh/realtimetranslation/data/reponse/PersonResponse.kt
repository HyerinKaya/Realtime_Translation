package com.jyh.realtimetranslation.data.reponse

import com.jyh.realtimetranslation.data.entity.person.PersonEntity

data class PersonResponse(
    val id:String,
    var name:String,
    var personImageUrl:String,
    var language:String
){
    fun toEntity():PersonEntity = PersonEntity(id, name, personImageUrl, language)
}