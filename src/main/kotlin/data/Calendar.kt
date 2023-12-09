package data
import utils.*
import java.io.File
import com.google.gson.*

class Calendar {
    private var events:ArrayList<Event>

    constructor(){
        events = arrayListOf()
    }
    fun AddEvent(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int, type: String, title: String, content: String) {
        events.add(Event(year, month, day, hour, minute, second, type, title, content))
    }

    fun EditEvent(
        year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int,
        year2: Int, month2: Int, day2: Int, hour2: Int, minute2: Int, second2: Int,
    ) {
        val evnt: Event? = FindEvent2(year, month, day, hour, minute, second)
        if (evnt != null) {
            evnt.SetWhen(year2, month2, day2, hour2, minute2, second2)
            SaveCalendarToFile()
        }
    }

    fun DeleteEvent(year:Int, month:Int, day:Int, hour: Int, minute:Int, second: Int){
        var evnt:Event? = FindEvent2(year, month, day, hour, minute, second)
        if(evnt !== null){
            events.remove(evnt)
        }
    }
    fun FindEvent1(year:Int, month:Int, day:Int):ArrayList<Event>{
        var rtn: ArrayList<Event> = arrayListOf()
        for (evnt in events){
            val check_query = arrayOf(year, month, day)
            val dates = evnt.GetWhen()
            val check_target = arrayOf(dates[0], dates[1], dates[2])
            if(
                (check_query[0] == dates[0]) &&
                (check_query[1] == dates[1]) &&
                (check_query[2] == dates[2])
                ){
                rtn.add(evnt)
            }
        }
        return rtn
    }
    fun FindEvent2(year:Int, month:Int, day:Int, hour: Int, minute:Int, second: Int, eventType: String = "", eventTitle: String = "", eventContent: String = ""):Event?{
        var rtn: Event? = null
        for (evnt in events){
            val check_query = arrayOf(year, month, day, hour, minute, second)
            val contentArray = evnt.GetContent()
            if (
                check_query.contentEquals(evnt.GetWhen()) &&
                (eventType.isEmpty() || eventType == contentArray[0]) &&
                (eventTitle.isEmpty() || eventTitle == contentArray[1]) &&
                (eventContent.isEmpty() || eventContent == contentArray[2])
            ) {
                return evnt
            }
        }
        return null
    }

    fun PutCalendar(year:Int, month:Int):String{
        // Variable:
        var result:String = ""

        // 초기 연산:
        val firstDayOfMonth = GetWeekNum(year, month, 1) // 첫 날의 요일을 구함 (1: 일요일, 2: 월요일, ..., 7: 토요일) /TEST
        val lastDayOfMonth = GetLastDayOfMonth(year, month) // 해당 월의 마지막 날짜를 구함 /TEST


        // 표 헤더 출력
        result += "일\t\t월\t\t화\t\t수\t\t목\t\t금\t\t토\n"

        // 첫 번째 주의 날짜까지의 빈 칸 출력
        for(i in 1 until firstDayOfMonth)
            result += "\t\t" // 각 날짜를 두 칸으로 늘림
        // 날짜 & 이벤트 개수 출력하기:
        for(day in 1..lastDayOfMonth){
            // 날짜 출력 (빈 날짜에는 빈 칸 출력)
            val eventCount = FindEvent1(year, month, day).size // 해당 날짜의 이벤트 수 /TEST
            result += "${day}${if (eventCount > 0) "\t(${eventCount})" else "\t"}\t"
            // 줄바꿈 처리 (7일마다)
            if ((firstDayOfMonth + day - 1) % 7 == 0) {
                result += "\n"
            }
        }
        result += "\n"

        return result
    }

    fun getEvents(): List<Event> {
        return events
    }

    fun SaveCalendarToFile() {
        try {
            val gson = Gson()
            val jsonString = gson.toJson(this)
            File("event.txt").writeText(jsonString)
        } catch (e: Exception) {
            println("Error saving calendar to file: ${e.message}")
        }
    }

    fun EditEvent() {
        // 사용자에게 수정할 이벤트 정보 입력 받기
        val startDate = ParseDateStr(GetString("수정할 이벤트의 년/월/일을 입력하세요(yyyy/mm/dd):"))
        val startTime = ParseTimeStr(GetString("수정할 이벤트의 시/분/초를 입력하세요(hh/mm/ss):"))
        val eventType = GetString("수정할 이벤트의 종류를 입력하세요:")
        val eventTitle = GetString("수정할 이벤트의 제목을 입력하세요:")
        val eventContent = GetString("수정할 이벤트의 내용을 입력하세요:")

        // 이벤트 찾기
        val eventToEdit = FindEvent2(startDate[0], startDate[1], startDate[2], startTime[0], startTime[1], startTime[2], eventType, eventTitle, eventContent)

        // 찾은 이벤트가 있으면 수정 진행
        if (eventToEdit != null) {
            // 사용자에게 수  정할 내용 입력 받기
            val newStartDate = ParseDateStr(GetString("새로운 년/월/일을 입력하세요(yyyy/mm/dd):"))
            val newStartTime = ParseTimeStr(GetString("새로운 시/분/초를 입력하세요(hh/mm/ss):"))
            val newEventType = GetString("새로운 종류를 입력하세요:")
            val newEventTitle = GetString("새로운 제목을 입력하세요:")
            val newEventContent = GetString("새로운 내용을 입력하세요:")

            // 이벤트 수정
            eventToEdit.SetWhen(newStartDate[0], newStartDate[1], newStartDate[2], newStartTime[0], newStartTime[1], newStartTime[2])
            eventToEdit.SetContent(newEventType, newEventTitle, newEventContent)

            println("이벤트가 성공적으로 수정되었습니다.")
            SaveCalendarToFile()
        } else {
            println("일치하는 이벤트를 찾지 못했습니다.")
        }
    }
}