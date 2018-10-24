package com.server;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.PeriodRule;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

public class TimetableParsing {
    private static String[] WeekDays = new String[]{"Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг",
            "Пятница", "Суббота"};

    public static net.fortuna.ical4j.model.Calendar ReadFile(String filename) {
        net.fortuna.ical4j.model.Calendar calendar = null;
        try {
            var fileStream = new FileInputStream(filename);
            var builder = new CalendarBuilder();
            calendar = builder.build(fileStream);
        } catch (Exception ignored) {
        }
        return calendar;
    }

    @SuppressWarnings("fallthrough")
    public static HashMap<String, ArrayList<Subject>> CreateTimeTableDataBase(net.fortuna.ical4j.model.Calendar calendar) {
        var timetable = new HashMap<String, ArrayList<Subject>>();
        for (String day : WeekDays) {
            timetable.put(day, new ArrayList<>());
        }
        ComponentList wholeTimetable = calendar.getComponents(Component.VEVENT);
        var firstSubject = (VEvent) wholeTimetable.get(0);
        var startDate = firstSubject.getStartDate().getDate();
        Period period = new Period(new DateTime(startDate.getTime()), new Dur(7, 0, 0, 0));
        Filter filter = new Filter(new PeriodRule((period)));

        Collection timetableParsed = filter.filter(calendar.getComponents(Component.VEVENT));

        for (Object elem : timetableParsed) {
            VEvent event = (VEvent) elem;
            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            var subject = event.getSummary().getValue();
            var dateStart = event.getStartDate().getDate();
            var dateEnd = event.getEndDate().getDate();
            var dateStartTime = timeFormat.format(dateStart);
            var dateEndTime = timeFormat.format(dateEnd);
            var weekday = DetermineDay(dateStart);
            String teacher = null;
            String classRoom = null;
            try {
                teacher = event.getDescription().getValue();
                classRoom = event.getLocation().getValue();
                if (teacher != null) {
                    teacher = teacher.substring(15);
                }

            } catch (Exception ignored) {
            }
            var teachers = new ArrayList<String>();
            var rooms = new ArrayList<String>();
            if (teacher != null)
                teachers.add(teacher);
            if (classRoom != null)
                rooms.add(classRoom);
            var currentSubject = new Subject(weekday, subject, dateStartTime, dateEndTime, teachers, rooms);
            var day = timetable.get(weekday);
            var addNewSubject = true;
            for (var subj : day) {
                if (subj.lessonName.equals(currentSubject.lessonName)
                        && subj.lessonStartTime.equals(currentSubject.lessonStartTime)) {
                    subj.teachers.add(teacher);
                    subj.rooms.add(classRoom);
                    addNewSubject = false;
                }
            }
            if (addNewSubject)
                day.add(currentSubject);
        }
        return timetable;
    }

    public static String DetermineDay(Date date) {
        var dateParser = java.util.Calendar.getInstance();
        dateParser.setTime(date);
        return WeekDays[dateParser.get(java.util.Calendar.DAY_OF_WEEK) - 1];
    }

    public static void main(String[] args) {
//        var parser = new TimetableParsing();
//        var calendar = ReadFile("./QuizBot/DataBase/calendar.ics");
//        var timetable = parser.CreateTimeTableDataBase(calendar);
//        for (var lesson:timetable.get("Вторник")
//             ) {
//            System.out.println(lesson.lessonName
//                    +" " +lesson.teachers + " " + lesson.lessonStartTime+"-" + lesson.lessonEndTime
//            );
//
//        }
        AddCalendarToDatabase("./QuizBot/DataBase/calendar.ics");
//        var calendarStr = TimetableParsing.ReadFile("./QuizBot/DataBase/calendar.ics");
//        var cal = TimetableParsing.CreateTimeTableDataBase(calendarStr);
//        var calOnDate = cal.get("Четверг").stream()
//                .map(subject -> subject.lessonName)
//                .collect(toList());
//        System.out.println(calOnDate);
    }

    private static void WriteFile(String textToWrite) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("./QuizBot/DataBase/Calendar.json", "UTF-8");
            writer.println(textToWrite);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static net.fortuna.ical4j.model.Calendar getTimetableFromUrfuApi(Integer id){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        var date = dateFormat.format(c.getTime());

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(String.format("https://urfu.ru/api/schedule/groups/calendar/%d/%s/", id, date))
                .get()
                .build();
        net.fortuna.ical4j.model.Calendar calendar = null;
        try {
            Response response = client.newCall(request).execute();
            var body = response.body();
            var builder = new CalendarBuilder();
            calendar = builder.build(new StringReader(body.string()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public static void AddCalendarToDatabase(String pathToCalendar) {
        var parser = new TimetableParsing();
        net.fortuna.ical4j.model.Calendar calendar = ReadFile(pathToCalendar);
        var timetable = parser.CreateTimeTableDataBase(calendar);

        var gson = new Gson();
        var jsonToWrite = gson.toJson(timetable);
        WriteFile(jsonToWrite);
    }
}
