package com.example.petong.classattendance;

import android.os.AsyncTask;
import android.util.Log;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;

import java.util.Map;

public class CheckDateTimeTask extends AsyncTask<Void, Void, CourseLec> {

    MainActivity.CheckDateTimeInterface callback;
    Map<String, Object> courseList;


    public CheckDateTimeTask(Map<String, Object> courseList, MainActivity.CheckDateTimeInterface callback) {
        this.callback = callback;
        this.courseList = courseList;
    }

    @Override
    protected CourseLec doInBackground(Void... voids) {
        LocalDate date = LocalDate.now();
        LocalTime timeNow;
        String day = setDay(date.getDayOfWeek().toString());
        while (true) {
            timeNow = LocalTime.now();

            for (String key : courseList.keySet()){
                CourseLec mCourse = (CourseLec) courseList.get(key);
                String[] courseDay = splitDay(mCourse.getDay());
                String[] time = mCourse.getTime().split(" - ");
                LocalTime startTime = toLocalTime(time[0]);
                LocalTime endTime = toLocalTime(time[1]);
                Log.d("Dayd", startTime + ", " + endTime);
                //Log.d("Dayd", day + " " + courseDay[0]);

                if (day.equals(courseDay[0]) || day.equals(courseDay[1])){              //get time to split start end time class and check with time now
                    if (startTime.isBefore(timeNow) && endTime.isAfter(timeNow)){
                        Log.d("Dayd", mCourse.getTitle() + " " + mCourse.getDay() + " " + mCourse.getTime());


                        return mCourse;
                    }
                    //Log.d("Dayd", mCourse.getTitle() + " " + mCourse.getDay());
                }
            }
        }
    }

    @Override
    protected void onPostExecute(CourseLec course) {
        super.onPostExecute(course);
        callback.onDateTimeMatch(course);
    }


    private String[] splitDay(String day){
        String[] newDay = new String[2];
        if(countUpperCase(day) > 1) {
            newDay = day.split("(?<=.)(?=\\p{Lu})");
        } else {
            newDay[0] = day;
            newDay[1] = null;
        }
        //Log.d("Dayd", newDay[0] + " " + newDay[1]);
        return newDay;
    }
    private int countUpperCase(String string) {
        int upper = 0;
        for(int i = 0; i < string.length(); i++){
            if(Character.isUpperCase(string.charAt(i))){
                upper++;
            }
        }
        //Log.d("Dayd", String.valueOf(upper));
        return upper;
    }
    private LocalTime toLocalTime(String time){
        String[] newTime = new String[2];
        newTime[0] = time.substring(0, 2);
        newTime[1] = time.substring(2);

        try {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("H:m")
                    .toFormatter();
            LocalTime mTime = LocalTime.parse(newTime[0] + ":" + newTime[1], formatter);
            //Log.d("Dayd", mTime.toString());

            return mTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String setDay(String day) {
        String newDay = null;
        switch (day) {
            case "MONDAY":
                newDay = "M";
                break;
            case "TUESDAY":
                newDay = "Tu";
                break;
            case "WEDNESDAY":
                newDay = "Wed";
                break;
            case "THURSDAY":
                newDay = "Th";
                break;
            case "FRIDAY":
                newDay = "F";
                break;
            case "SATURDAY":
                newDay = "Sa";
                break;
            case "SUNDAY":
                newDay = "Su";
                break;
        }
        return newDay;
    }
}
