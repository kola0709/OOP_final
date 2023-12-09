package mainapp

import utils.*
import data.Calendar
import java.io.File
import com.google.gson.*
import data.Event

class Control {
    lateinit var current_calendar:Calendar

    constructor(){
        current_calendar = Calendar()
    }
    constructor(fname:String){
        LoadCalendar(fname)
    }
    fun SaveCalendar(fname: String) {
        val gson = Gson()
        File(fname).writeText(gson.toJson(current_calendar))
    }
    fun LoadCalendar(fname:String){
        if(!File(fname).exists()){
            return
        }
        val jsonString = File(fname).readText()
        current_calendar = Gson().fromJson(jsonString, Calendar::class.java)
    }
    fun CheckEvent(){

    }
    fun AddEvent(year:Int, month:Int, day:Int, hour: Int, minute:Int, second: Int, type:String, title:String, content:String) {
        current_calendar.AddEvent(year, month, day, hour, minute, second, type, title, content)
    }

    fun DeleteEvent(){

    }
    fun ShowCalenderMonth(year:Int, month:Int):String{
        return current_calendar.PutCalendar(year, month)
    }

    fun EditEvent() {

    }
}