package com.jyh.realtimetranslation.presentation.offlinetranslation

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class Papago {
    fun getTranslation(word: String?, source: String?, target: String?): String {
        val clientId = "Z7TaXQBLJjoLDU8MqTG0"
        val clientSecret = "Nd55BcjMGx"
        try {
            val text: String = URLEncoder.encode(word, "UTF-8") //word
            val wordSource: String = URLEncoder.encode(source, "UTF-8")
            val wordTarget: String = URLEncoder.encode(target, "UTF-8")
            val apiURL = "https://openapi.naver.com/v1/papago/n2mt"
            val url = URL(apiURL)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            "POST".also { con.requestMethod = it }
            con.setRequestProperty("X-Naver-Client-Id", clientId)
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret)
            // post request
            val postParams = "source=$wordSource&target=$wordTarget&text=$text"
            con.doOutput = true
            val wr = DataOutputStream(con.outputStream)
            wr.writeBytes(postParams)
            wr.flush()
            wr.close()
            val responseCode: Int = con.responseCode
            val br: BufferedReader
            if (responseCode == 200) { // 정상 호출
                br = BufferedReader(InputStreamReader(con.inputStream))
            } else {  // 에러 발생
                br = BufferedReader(InputStreamReader(con.errorStream))
            }
            var inputLine: String?
            val response = StringBuffer()
            while (br.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            br.close()
            var s = response.toString()
            s = s.split("\"".toRegex()).toTypedArray()[15]
            return s
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "0"
    }
}