import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun main() {
    var isRunning = true

    while (isRunning) {
        // 메뉴 출력
        print("메뉴 번호를 선택하세요 (1: 종료, 2: 달력조회, 3: 이벤트 추가, 4: 이벤트 조회): ")

        // 사용자 입력 받기
        val choice = readLine()?.trim()?.toIntOrNull() ?: continue

        // 메뉴 처리
        when (choice) {
            1 -> {
                println("프로그램을 종료합니다.")
                isRunning = false
            }
            2 -> {
                // 달력 조회
                print("년도를 입력하세요: ")
                val year = readLine()?.trim()?.toInt() ?: return

                print("월을 입력하세요: ")
                val month = readLine()?.trim()?.toInt() ?: return

                // 이벤트 날짜 목록 생성 (임의의 날짜로 예시)
                val startDates = readStartDatesFromFile()

                printCalendar(year, month, startDates)
            }
            3 -> {
                // 이벤트 추가
                addEvent()
            }
            4 -> {
                // 이벤트 조회
                println("이벤트 조회를 시작합니다.")

                // 조회할 날짜 입력 받기
                print("조회할 날짜를 입력하세요 (yyyy/MM/dd): ")
                val queryDateString = readLine()?.trim() ?: continue
                val queryDate = parseDateString(queryDateString)

                // 이벤트 조회 및 출력
                searchAndPrintEvents(queryDate)
            }

            else -> {
                println("잘못된 메뉴 번호입니다. 다시 선택해주세요.")
            }
        }

        // 메뉴 처리 이후 줄바꿈을 포함한 메뉴 재출력
        println()
    }
}

fun addEvent() {
    println("이벤트 추가를 시작합니다.")

    // 입력 받기
    print("이벤트 시작 날짜를 입력하세요 (yyyy/MM/dd): ")
    val startDateString = readLine()?.trim() ?: return
    val startDate = parseDateString(startDateString)

    print("이벤트 시작 시간을 입력하세요 (HH:mm:ss): ")
    val startTimeString = readLine()?.trim() ?: return
    val startTime = parseTimeString(startTimeString)

    print("이벤트 종료 날짜를 입력하세요 (yyyy/MM/dd): ")
    val endDateString = readLine()?.trim() ?: return
    val endDate = parseDateString(endDateString)

    print("이벤트 종료 시간을 입력하세요 (HH:mm:ss): ")
    val endTimeString = readLine()?.trim() ?: return
    val endTime = parseTimeString(endTimeString)

    print("이벤트 제목을 입력하세요: ")
    val title = readLine()?.trim() ?: return

    print("이벤트 세부사항을 입력하세요: ")
    val details = readLine()?.trim() ?: return

    // 이벤트 정보를 파일에 추가
    val filename = "events.txt"
    File(filename).appendText("${formatDateString(startDate)} ${formatTimeString(startTime)} - " +
            "${formatDateString(endDate)} ${formatTimeString(endTime)}: $title - $details\n")

    println("이벤트가 성공적으로 추가되었습니다.")
}


fun parseDateString(dateString: String): Date {
    val format = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    return format.parse(dateString) ?: Date()
}

fun parseTimeString(timeString: String): Date {
    val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return format.parse(timeString) ?: Date()
}

fun formatDateString(date: Date): String {
    val format = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    return format.format(date)
}

fun formatTimeString(time: Date): String {
    val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return format.format(time)
}

fun printCalendar(year: Int, month: Int, eventDates: List<Date>) {
    // 달력의 시작일을 구함
    val calendar = Calendar.getInstance()
    calendar.set(year, month - 1, 1)

    // 첫 날의 요일을 구함 (1: 일요일, 2: 월요일, ..., 7: 토요일)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    // 해당 월의 마지막 날짜를 구함
    val lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    // 표 헤더 출력
    println("일\t\t월\t\t화\t\t수\t\t목\t\t금\t\t토")

    // 첫 번째 주의 날짜까지의 빈 칸 출력
    for (i in 1 until firstDayOfWeek) {
        print("\t\t") // 각 날짜를 두 칸으로 늘림
    }

    // 날짜 및 이벤트 개수 출력
    for (day in 1..lastDayOfMonth) {
        val currentDate = Calendar.getInstance()
        currentDate.set(year, month - 1, day)

        // 이벤트 개수 확인
        val eventCount = eventDates.count { isSameDay(it, currentDate.time) }

        // 날짜 출력 (빈 날짜에는 빈 칸 출력)
        print("${day}${if (eventCount > 0) "\t(${eventCount})" else "\t"}\t")

        // 줄바꿈 처리 (7일마다)
        if ((firstDayOfWeek + day - 1) % 7 == 0) {
            println()
        }
    }
}

fun searchAndPrintEvents(queryDate: Date) {
    val filename = "events.txt"
    val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())

    File(filename).forEachLine { line ->
        val eventInfo = line.split(" ")

        if (eventInfo.size >= 8) { // 최소한의 길이를 확인하여 유효한 데이터로 간주
            val startDateString = "${eventInfo[0]} ${eventInfo[1]}"
            val endDateString = "${eventInfo[3]} ${eventInfo[4]}"
            val startDate = format.parse(startDateString)
            val endDate = format.parse(endDateString)

            // 이벤트의 시작 날짜와 종료 날짜가 쿼리 날짜를 포함하는 경우
            if (isSameDay(startDate, queryDate) || isSameDay(endDate, queryDate)) {
                // 수정된 부분 시작
                val title = eventInfo[5]
                val details = eventInfo.subList(7, eventInfo.size).joinToString(" ")

                println("[종료시각] : ${format.format(endDate)}")
                println("[이벤트 제목] : $title")
                println("[이벤트 내용] : $details")
                println()
                // 수정된 부분 끝
            }
        }
    }
}

fun readStartDatesFromFile(): List<Date> {
    val filename = "events.txt"
    val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
    val startDates = mutableListOf<Date>()

    File(filename).forEachLine { line ->
        val eventInfo = line.split(" ")

        if (eventInfo.size >= 8) { // 최소한의 길이를 확인하여 유효한 데이터로 간주
            val startDateString = "${eventInfo[0]} ${eventInfo[1]}"
            val startDate = format.parse(startDateString)

            startDates.add(startDate)
        }
    }

    return startDates
}

fun isSameDay(date1: Date, date2: Date): Boolean {
    val cal1 = Calendar.getInstance()
    val cal2 = Calendar.getInstance()
    cal1.time = date1
    cal2.time = date2

    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
            cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
}