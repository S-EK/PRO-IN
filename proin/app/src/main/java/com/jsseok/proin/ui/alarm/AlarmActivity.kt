package com.jsseok.proin.ui.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.jsseok.proin.R
import com.jsseok.proin.data.model.AlarmDisplayModel
import com.jsseok.proin.databinding.TimePickBinding
import java.util.*

// Alarm Setting
class AlarmActivity : AppCompatActivity() {
    // AlarmActivity 해당하는 TimePick 바인딩 : 레이아웃 관리
    private lateinit var binding: TimePickBinding

    // onCreate function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TimePickBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initOnOffButton()
        initChangeAlarmTimeButton()

        // 저장된 데이터 가져오기
        val model = fetchDataFromSharedPreferences()
        // Rendering
        renderView(model)
    }


    private fun fetchDataFromSharedPreferences(): AlarmDisplayModel {
        val sharedPreferences = getSharedPreferences(M_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

        // DB 에서 데이터 가져오기
        val timeDBValue = sharedPreferences.getString(M_ALARM_KEY, "09:30") ?: "09:30"
        val onOffDBValue = sharedPreferences.getBoolean(M_ONOFF_KEY, false)

        // 가져온 Data Split
        val alarmData = timeDBValue.split(":")
        val alarmModel = AlarmDisplayModel(alarmData[0].toInt(), alarmData[1].toInt(), onOffDBValue)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            M_ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )

        // 상태 : 알림 꺼짐 & 데이터 켜짐
        if ((pendingIntent == null) and alarmModel.onOff) {
            alarmModel.onOff = false

        // 상태 : 알림 켜짐 & 데이터 꺼짐
        } else if ((pendingIntent != null) and alarmModel.onOff.not()) {
            pendingIntent.cancel()  // 알림 취소
        }
        return alarmModel
    }

    private fun initChangeAlarmTimeButton() {

        val changeAlarmButton = binding.changeAlarmTimeButton
        changeAlarmButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            // TimePickDialog -> 시간 설정
            TimePickerDialog(this, { picker, hour, minute ->

                // 데이터를 저장
                val model = saveAlarmModel(hour, minute, false)
                // Rendering
                renderView(model)
                cancelAlarm()   // 기존 알림 삭제

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
                .show()

        }
    }

    // 알림 온오프 버튼
    private fun initOnOffButton() {
        val onOffButton = findViewById<Button>(R.id.onOffButton)
        onOffButton.setOnClickListener {
            val model =
                it.tag as? AlarmDisplayModel ?: return@setOnClickListener// 형변환 실패 : null
            val newModel = saveAlarmModel(model.hour, model.minute, model.onOff.not()) // on off 스위칭
            renderView(newModel)

            if (newModel.onOff) {
                // 온 -> 알람을 등록
                val calender = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, newModel.hour)
                    set(Calendar.MINUTE, newModel.minute)
                    // 지나간 시간의 경우 다음날 알람으로 울리도록
                    if (before(Calendar.getInstance())) {
                        add(Calendar.DATE, 1) // 하루 더하기
                    }
                }

                //알람 매니저 가져오기.
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val intent = Intent(this, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    M_ALARM_REQUEST_CODE,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                ) // 있으면 새로 만든거로 업데이트

                // 정시 반복
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calender.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )

                // 알림 설정 완료 Toast msg 출력
                Toast.makeText(this, "Set Alarm Successfully!", Toast.LENGTH_SHORT).show()
            } else {
                // 알림 취소 후, 취소 완료 Toast msg 출력
                cancelAlarm()
                Toast.makeText(this, "Cancel Alarm Successfully!", Toast.LENGTH_SHORT).show()
            }


        }
    }

    // 최초 실행 | 시간 재설정에 맞춰 Rendering
    private fun renderView(model: AlarmDisplayModel) {
        findViewById<TextView>(R.id.ampmTextView).apply {
            text = model.ampmText
        }
        findViewById<TextView>(R.id.timeTextView).apply {
            text = model.timeText
        }
        findViewById<Button>(R.id.onOffButton).apply {
            text = model.onOffText
            tag = model
        }
    }

    // 기존 알림 삭제
    private fun cancelAlarm() {
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            M_ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )

        pendingIntent?.cancel() // 삭제
    }

    // DB에 time data 저장
    private fun saveAlarmModel(hour: Int, minute: Int, onOff: Boolean): AlarmDisplayModel {
        val model = AlarmDisplayModel(
            hour = hour,
            minute = minute,
            onOff = onOff
        )

        // DB 파일 생성
        val sharedPreferences = getSharedPreferences(M_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

        with(sharedPreferences.edit()) {    // 값 저장
            putString(M_ALARM_KEY, model.makeDataForDB())
            putBoolean(M_ONOFF_KEY, model.onOff)
            commit()
        }

        return model
    }

    // 상수
    companion object {
        private const val M_SHARED_PREFERENCE_NAME = "time"
        private const val M_ALARM_KEY = "alarm"
        private const val M_ONOFF_KEY = "onOff"
        private val M_ALARM_REQUEST_CODE = 1000
    }
}