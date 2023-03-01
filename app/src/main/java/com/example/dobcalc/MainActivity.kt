package com.example.dobcalc

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    //볼 수 없는 액티비티의 UI요소를 사용해서 앱이 오작동을 일으키는 것을 방지하기 위해
    //private으로 사용.
    private var tvSelectedDate : TextView? = null
    private var tvAgeInMinutes : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnDatePicker : Button = findViewById(R.id.btnDatePicker)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        tvAgeInMinutes = findViewById(R.id.tvAgeInMinutes)
        btnDatePicker.setOnClickListener {

            clickDatePicker()
        }
    }
    //특정 클래스 안에서만 동작하도록 private으로 이용
    private fun clickDatePicker(){

        //java.util에서 달력을 가져옴
        val myCalender = Calendar.getInstance()
        //계산을 위해 달력의 년,월,일을 받음
        val year = myCalender.get(Calendar.YEAR)
        val month = myCalender.get(Calendar.MONTH)
        val day = myCalender.get(Calendar.DAY_OF_MONTH)
        //show()가 있어야 화면에 출력
        val dpd = DatePickerDialog(this,
            //view, year, month, dayOfMonth에 대한 정보를 전달
            //사용하지 않을 변수는 _로 처리해도 됨. -> view를 _로 대체 가능
            //코드를 제대로 작성했다면 주석처럼 회색이 되고 자동으로 이 함수 형식이라는 것으로
            //인지하기 때문에 삭제해도 됨.
            DatePickerDialog.OnDateSetListener{
                view, selectedYear, selectedMonth, selectedDayOfMonth->
                Toast.makeText(this,
                    //month를 0부터 계산하기 때문에 +1
                    "Year was $selectedYear, month was ${selectedMonth + 1}, day of month was $selectedDayOfMonth", Toast.LENGTH_LONG).show()
                val selectedDate = "$selectedYear/${selectedMonth+1}/$selectedDayOfMonth"

                tvSelectedDate?.text = selectedDate
                //영어로 된 simpledate형식의 날짜 객체를 생성
                val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
                //selectedDate 객체에서 사용하고 싶은 형태로 만듦(시간을 계산할 때 사용)
                val theDate = sdf.parse(selectedDate)
                //null 안전성
                //let을 사용하여 날짜 선택 여부를 확인하고 코드를 실행시킴.
                //선택되지 않으면 코드를 실행시키지 않아 오작동을 방지함.
                theDate?.let{
                    val selectedDateInMinutes = theDate.time / 60000
                    //현재 시각
                    val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))
                    currentDate?.let{
                        //분 단위로 현재 시각
                        val currentDateInMinutes = currentDate.time / 60000
                        //두 날짜 사이 간의 차이
                        val differenceInMinutes = currentDateInMinutes - selectedDateInMinutes

                        tvAgeInMinutes?.text = differenceInMinutes.toString()
                    }

                }
                //date속성은 1970년 1월 1일 자정부터 지금까지 지난 시간을
                //ms단위로 출력하기 때문에 60000으로 나눠줌
                //하이라이트되어 있는 것은 유형 안정성 부재 때문. -> 버그방지에 도움
                //val selectedDateInMinutes = theDate.time / 60000
                //현재 시각
                //val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))
                //분 단위로 현재 시각
                //val currentDateInMinutes = currentDate.time / 60000
                //두 날짜 사이 간의 차이
                //val differenceInMinutes = currentDateInMinutes - selectedDateInMinutes

                //tvAgeInMinutes?.text = differenceInMinutes.toString()


            },
            year,
            month,
            day
            )
        //DatePickerDialog를 변수로 만들어 오늘 날짜를 초과할 수 없게 설정.
        //한 시간(360만 ms) * 24 = 86400000
        dpd.datePicker.maxDate = System.currentTimeMillis() - 86400000
        dpd.show()

    }
}