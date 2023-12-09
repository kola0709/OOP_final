package utils

fun IsLeapYear(year:Int):Boolean{
    if(year % 400 == 0)
        return true
    else if((year % 4 == 0) && (year % 100 != 0))
        return true
    return false
}



fun GetLastDayOfMonth(year:Int, month:Int): Int{
    val result:Int  =
    when(month){
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 ->{
            if(IsLeapYear(year)){
                29
            }else{
                28
            }
        }
        else -> 0
    }
    return result
}

fun GetPassedDays(year:Int, month:Int, day:Int):Int{
    var days:Int = 0
    for(mnt in 1 until month){
        days += GetLastDayOfMonth(year, mnt)
    }
    days += day
    return days
}

fun GetWeekNum(year:Int, month:Int, day:Int):Int{
    var days:Int = 0
    for(y in 1 until year){
        for(m in 1 until 13){
            days += GetLastDayOfMonth(y, m)
        }
    }
    days += GetPassedDays(year, month, day)
    return ((days % 7) +1)% 7
}

fun GetNumToWeekStr(num:Int):String{
    val rtn:String = when(num){
        0 -> "SUN"
        1 -> "MON" // 월요일
        2 -> "TUE"
        3 -> "WED"
        4 -> "THU"
        5 -> "FRI"
        6 -> "SAT"

        else -> "ERR"
    }
    return rtn
}



fun GetNumInt(msg:String):Int{
    var ret:Int? = 0
    println(msg)
    while(true){
        ret = readLine()?.trim()?.toInt()
        if(ret != null){
            break
        }
        println("정수를 입력해주세요!")
    }
    return ret!!
}


fun GetString(msg:String):String{
    var ret:String? = ""
    println(msg)
    while(true){
        ret = readLine()?.trim()?.toString()
        if(ret != null){
            break
        }
        println("내용을 입력해주세요!")
    }
    return ret!!
}


fun ParseDateStr(dateString: String):Array<Int>{
    val dateArray:Array<Int> = dateString.split("/").map { it.toInt() }.toTypedArray()
    return dateArray
}

fun ParseTimeStr(timeString: String):Array<Int>{
    val timeArray:Array<Int> = timeString.split("/").map { it.toInt() }.toTypedArray()
    return timeArray
}

/*

fun createEventFromUserInput(type: String): Event {
    print("이벤트 $type 날짜를 입력하십시오 (yyyy/mm/dd): ")
    val dateInput = readLine()?.trim() ?: throw IllegalArgumentException("잘못된 입력입니다.")
    val dateTimeInput = parseDateTime(dateInput)

    print("이벤트 $type 시각을 입력하십시오 (hh:mm:ss): ")
    val timeInput = readLine()?.trim() ?: throw IllegalArgumentException("잘못된 입력입니다.")
    val timeArray = timeInput.split(":").map { it.toInt() }
    dateTimeInput.set(Calendar.HOUR_OF_DAY, timeArray[0])
    dateTimeInput.set(Calendar.MINUTE, timeArray[1])
    dateTimeInput.set(Calendar.SECOND, timeArray[2])

    print("이벤트 제목을 입력하십시오: ")
    val title = readLine()?.trim() ?: throw IllegalArgumentException("잘못된 입력입니다.")

    print("이벤트 내용을 입력하십시오: ")
    val content = readLine()?.trim() ?: throw IllegalArgumentException("잘못된 입력입니다.")


    return Event(
        dateTimeInput.get(Calendar.YEAR),
        dateTimeInput.get(Calendar.MONTH) + 1,
        dateTimeInput.get(Calendar.DAY_OF_MONTH),
        dateTimeInput.get(Calendar.HOUR_OF_DAY),
        dateTimeInput.get(Calendar.MINUTE),
        dateTimeInput.get(Calendar.SECOND),
        type,  //시작 또는 종료 입력
        title, //이벤트 제목 입력
        content
    )
}

fun parseDateTime(dateTimeString: String): Calendar {
    val dateTimeArray = dateTimeString.split("/").map { it.toInt() }
    val calendar = Calendar.getInstance()
    calendar.set(dateTimeArray[0], dateTimeArray[1] - 1, dateTimeArray[2])
    return calendar
}

 */