package com.jsseok.proin.data.model

data class AlarmDisplayModel(
    val hour: Int, // 0~23
    val minute: Int,
    var onOff: Boolean
) {

    fun makeDataForDB(): String {
        return "$hour:$minute"
    }

    // 시, 분
    val timeText: String
        get() {
            val h = "%02d".format(if (hour < 12) hour else hour - 12)
            val m = "%02d".format(minute)

            return "$h:$m"
        }

    // AM, PM
    val ampmText: String
        get() {
            return if (hour < 12) "AM" else "PM"
        }

    // 버튼 Text
    val onOffText: String
        get(){
            return if(onOff) "OFF ALARM" else "ON ALARM"
        }
}