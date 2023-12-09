package mainapp

import utils.*
import data.Event
import java.util.*
import java.io.File
import com.google.gson.*


var isRunning = true


fun main() {
    var sess1: Control = Control()
    val filePath = "event.txt"
    sess1.LoadCalendar(filePath)

    while (isRunning) {
        // 메뉴 출력
        print("메뉴 번호를 선택하세요 (1: 종료, 2: 달력조회, 3: 이벤트 추가, 4: 제목으로 이벤트 조회 5: 내용으로 이벤트 조회): ")
        // 사용자 입력 받기
        val choice = readLine()?.trim()?.toIntOrNull() ?: continue
        // 메뉴 처리
        when (choice) {
            1 -> {
                sess1.SaveCalendar(filePath)
                println("프로그램을 종료합니다.")
                isRunning = false
            }

            2 -> {
                // 달력 조회
                val year = GetNumInt("년도를 입력하세요: ")
                val month = GetNumInt("월을 입력하세요: ")
                print(sess1.ShowCalenderMonth(year, month))
            }

            3 -> {
                println("이벤트를 추가합니다.")

                println("이벤트 시작 시점:")
                val startDate = ParseDateStr(GetString("년/월/일 을 입력하세요(yyyy/mm/dd):"))
                val startTime = ParseTimeStr(GetString("시/분/초 를 입력하세요(hh/mm/ss):"))
                /*
                val startYear = GetNumInt("년도를 입력하세요: ")
                val startMonth = GetNumInt("월을 입력하세요: ")
                val startDay = GetNumInt("일을 입력하세요: ")
                val startHour = GetNumInt("시간을 입력하세요: ")
                val startMinute = GetNumInt("분을 입력하세요: ")
                val startSecond = GetNumInt("초를 입력하세요: ")

                 */
                val startType = GetString("종류를 입력하세요:")
                val startTitle = GetString("제목을 입력하세요:")
                val startContent = GetString("내용을 입력하세요:")

                println("이벤트 종료 시점:")
                val endDate = ParseDateStr(GetString("년/월/일 을 입력하세요(yyyy/mm/dd):"))
                val endTime = ParseTimeStr(GetString("시/분/초 를 입력하세요(hh/mm/ss):"))
                /*
                val endYear = GetNumInt("년도를 입력하세요: ")
                val endMonth = GetNumInt("월을 입력하세요: ")
                val endDay = GetNumInt("일을 입력하세요: ")
                val endHour = GetNumInt("시간을 입력하세요: ")
                val endMinute = GetNumInt("분을 입력하세요: ")
                val endSecond = GetNumInt("초를 입력하세요: ")
                */
                val endType = GetString("종류를 입력하세요:")
                val endTitle = GetString("제목을 입력하세요:")
                val endContent = GetString("내용을 입력하세요:")

                sess1.current_calendar.AddEvent(startDate[0], startDate[1], startDate[2], startTime[0], startTime[1], startTime[2], startType, startTitle, startContent)
                sess1.current_calendar.AddEvent(endDate[0], endDate[1], endDate[2], endTime[0], endTime[1], endTime[2], endType, endTitle, endContent)



                //sess1.current_calendar.AddEvent(startYear, startMonth, startDay, startHour, startMinute, startSecond, startType, startTitle, startContent)
                //sess1.current_calendar.AddEvent(endYear, endMonth, endDay, endHour, endMinute, endSecond, endType, endTitle, endContent)

                println("이벤트가 추가되었습니다.")
            }

            4 -> {
                searchEventByTitle(sess1)
            }

            5 -> {
                searchEventByContent(sess1)
            }

            6-> {
                sess1.current_calendar.EditEvent()
                sess1.SaveCalendar(filePath)
            }
        }
    }

}

// 이벤트를 제목으로 찾는 함수
fun searchEventByTitle(sess1: Control) {
    println("이벤트를 제목으로 검색합니다.")
    val searchingInput = GetString("이벤트 제목을 입력하십시오 : ")

    val foundEvents = sess1.current_calendar.getEvents().filter { it.GetContent()[1] == searchingInput }

    printFoundEvents(foundEvents)
}

// 이벤트를 내용으로 찾는 함수
fun searchEventByContent(sess1: Control) {
    println("이벤트를 내용으로 검색합니다.")
    val searchingInput = GetString("이벤트 내용을 입력하십시오 : ")

    val foundEvents = sess1.current_calendar.getEvents().filter { it.GetContent()[2] == searchingInput }

    printFoundEvents(foundEvents)
}

// 검색된 이벤트 출력 함수
fun printFoundEvents(foundEvents: List<Event>) {
    if (foundEvents.isNotEmpty()) {
        println("검색된 이벤트:")
        for (event in foundEvents) {
            val whenArray = event.GetWhen()
            val formattedDate = "${whenArray[0]}년 ${whenArray[1]}월 ${whenArray[2]}일 ${whenArray[3]}시 ${whenArray[4]}분 ${whenArray[5]}초"
            println("제목: ${event.GetContent()[1]}, 내용: ${event.GetContent()[2]}, 날짜: $formattedDate")
        }
    } else {
        println("일치하는 이벤트가 없습니다.")
    }
}

/*
data class EventInfo(val date: String, val time: String, val title: String, val content: String)

fun parseEventInfo(eventString: String): EventInfo {
    val eventData = eventString.split(",")
    val eventDate = "${eventData.getOrNull(0)?.substringAfter(":")?.trim('"')}/${eventData.getOrNull(1)?.substringAfter(":")?.trim('"')}/${eventData.getOrNull(2)?.substringAfter(":")?.trim('"')}"

    // 시간 부분을 12:0:0에서 12:00:00으로 포맷팅
    val eventTimeList = (3 until 6).map { eventData.getOrNull(it)?.substringAfter(":")?.trim('"')?.padStart(2, '0') ?: "00" }
    val eventTime = eventTimeList.joinToString(":")

    val eventTitle = eventData.getOrNull(7)?.substringAfter(":")?.trim('"') ?: ""
    val eventContent = eventData.getOrNull(8)?.substringAfter(":")?.substringBeforeLast("\"")?.trim('"') ?: ""

    return EventInfo(eventDate, eventTime, eventTitle, eventContent)
}

 */