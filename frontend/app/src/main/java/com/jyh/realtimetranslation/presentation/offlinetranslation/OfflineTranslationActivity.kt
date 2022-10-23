package com.jyh.realtimetranslation.presentation.offlinetranslation

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.jyh.realtimetranslation.RealtimeTranslationApplication
import com.jyh.realtimetranslation.data.room.offline.OfflineMessageItem
import com.jyh.realtimetranslation.data.room.offline.OfflineRoomItem
import com.jyh.realtimetranslation.databinding.ActivityOffllineTranslationBinding
import com.jyh.realtimetranslation.presentation.BaseActivity
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList

internal class OfflineTranslationActivity() :
    BaseActivity<OfflineTranslationViewModel, ActivityOffllineTranslationBinding>() {

    private var messageList = mutableListOf<OfflineMessageItem>()
    private val adapter = OfflineMessageAdapter()
    private var offlineRoomId = 0L
    private var myLang = "ko"
    private var otherLang = "en"

    // stt
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognitionListener: RecognitionListener

    private val langList = listOf(
        Pair("한국어", "ko"),
        Pair("일본어", "ja"),
        Pair("영어", "en"),
        Pair("중국어 간체", "zh-CN"),
        Pair("중국어 번체", "zh-TW"),
        Pair("베트남어", "vi"),
        Pair("태국어", "th"),
        Pair("스페인어", "es"),
        Pair("프랑스어", "fr"),
        Pair("인도네시아어", "id"),
        Pair("러시아어", "ru"),
        Pair("독일어", "de"),
        Pair("이탈리아어", "it")
    )

    override val viewModel by inject<OfflineTranslationViewModel>()

    override fun getViewBinding() = ActivityOffllineTranslationBinding.inflate(layoutInflater)

    override fun observeData() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        offlineRoomId = intent.getLongExtra("roomId", 0L)
        if(intent.getBooleanExtra("isNew",false)) addRoom(offlineRoomId)
        initViews()
    }

    private fun addRoom(offlineRoomId: Long) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                (application as RealtimeTranslationApplication).database.offlineRoomDao()
                    .insertAll(OfflineRoomItem(offlineRoomId, offlineRoomId, ""))
            }
        }
    }

    // 번역 기능 기초 완성
    @SuppressLint("ClickableViewAccessibility", "NotifyDataSetChanged", "SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    private fun initViews() = with(binding) {
        val date = Date(offlineRoomId)
        dateTextView.text = "${date.year-100}.${"%02d".format(date.month+1)}.${"%02d".format(date.date)}"
        startedTimeTextView.text = "${"%02d".format(date.hours)}:${"%02d".format(date.minutes)} Started"

        // 언어 스피너 초기화
        initSpinner()

        // back button
        backButton.setOnClickListener {
            finish()
        }

        // 메세지 정보 불러오기
        messageRecyclerView.layoutManager = LinearLayoutManager(this@OfflineTranslationActivity)
        GlobalScope.launch {
            messageList.addAll(
                (application as RealtimeTranslationApplication).database.offlineMessageDao()
                    .getAll().filter { it.offlineRoomId == offlineRoomId })
            runOnUiThread {
                messageRecyclerView.adapter = adapter
                adapter.submitList(messageList)
            }
        }

        // 내가 말하기
        mySpeechImageButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    setStt(true)
                }
            }
            true
        }

        // 상대방이 말하기
        otherSpeechImageButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    setStt(false)
                }
            }
            true
        }
        adapter.submitList(messageList)
        adapter.notifyDataSetChanged()
        messageRecyclerView.scrollToPosition(messageList.lastIndex)
    }

    private fun initSpinner() {
        val myLangAdapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_dropdown_item,
            langList.map { it.first + " " + it.second })
        val otherLangAdapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_dropdown_item,
            langList.map { it.first + " " + it.second })
        binding.myLangSpinner.adapter = myLangAdapter
        binding.otherLangSpinner.adapter = otherLangAdapter
        binding.otherLangSpinner.setSelection(2)
        binding.myLangSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                myLang = langList[position].second
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        binding.otherLangSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    id: Long
                ) {
                    otherLang = langList[position].second
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }

    private fun setStt(isSending: Boolean) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, if (isSending) myLang else otherLang)
        setListener(
            if (isSending) myLang else otherLang,
            if (isSending) otherLang else myLang,
            isSending
        )
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this@OfflineTranslationActivity)
        speechRecognizer.setRecognitionListener(recognitionListener)
        speechRecognizer.startListening(intent)
    }

    private fun setListener(fromLang: String, toLang: String, isSending: Boolean) {
        recognitionListener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Toast.makeText(applicationContext, "시작", Toast.LENGTH_SHORT).show()
            }

            override fun onBeginningOfSpeech() {

            }

            override fun onRmsChanged(rmsdB: Float) {
            }

            override fun onBufferReceived(buffer: ByteArray?) {
            }

            override fun onEndOfSpeech() {
            }

            override fun onError(error: Int) {
                var message: String
                when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> message = "오디오 에러"
                    SpeechRecognizer.ERROR_CLIENT -> message = "클라이언트 에러"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> message = "퍼미션 없음"
                    SpeechRecognizer.ERROR_NETWORK -> message = "네트워크 에러"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> message = "네트워크 타임아웃"
                    SpeechRecognizer.ERROR_NO_MATCH -> message = "찾을 수 없음"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> message = "RECOGNIZER가 바쁨"
                    SpeechRecognizer.ERROR_SERVER -> message = "서버가 이상함"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> message = "말하는 시간초과"
                    else -> message = "알 수 없는 오류"
                }
                Toast.makeText(applicationContext, "에러 발생 $message", Toast.LENGTH_SHORT).show()
            }

            // stt 실행 결과 동작
            @SuppressLint("NotifyDataSetChanged")
            override fun onResults(results: Bundle?) {
                var matches: ArrayList<String> =
                    results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) as ArrayList<String>
                for (i in 0 until matches.size) {
                    val originalText = matches[i]
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            val papago = Papago()
                            val resultWord =
                                if (myLang == otherLang) originalText else papago.getTranslation(
                                    originalText,
                                    fromLang,
                                    toLang
                                )
                            val offlineMessage = OfflineMessageItem(
                                System.currentTimeMillis(),
                                offlineRoomId,
                                isSending,
                                originalText,
                                resultWord
                            )
                            (application as RealtimeTranslationApplication).database.offlineMessageDao()
                                .insertAll(offlineMessage)
                            (application as RealtimeTranslationApplication).database.offlineRoomDao()
                                .insertAll(
                                    OfflineRoomItem(
                                        offlineRoomId,
                                        System.currentTimeMillis(),
                                        if (isSending) originalText else resultWord
                                    )
                                )
                            runOnUiThread {
                                // db에 입력
                                viewModel.addMessage(offlineMessage)
                                messageList.add(offlineMessage)
                                adapter.submitList(messageList)
                                adapter.notifyDataSetChanged()
                                binding.messageRecyclerView.scrollToPosition(messageList.lastIndex)
                            }
                        }
                    }
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
            }

        }
    }
}
