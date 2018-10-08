package Server;

import com.google.gson.Gson;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.PeriodRule;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TimetableParsing {
    private static String[] WeekDays = new String[]{"Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг",
            "Пятница", "Суббота"};

    public static Calendar ReadFile(String filename) {
        Calendar calendar = null;
        try {
            var fileStream = new FileInputStream(filename);
            var builder = new CalendarBuilder();
            calendar = builder.build(fileStream);
        } catch (Exception ignored) {
        }
        return calendar;
    }
    @SuppressWarnings("fallthrough")
    public static HashMap<String, ArrayList<Subject>> CreateTimeTableDataBase(Calendar calendar) {
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
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
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
                classRoom = event.getSummary().getValue();
                if (teacher != null){
                    teacher = teacher.substring(15);
                }
            } catch (Exception ignored) {
            }
            var teachers = new ArrayList<String>();
            var rooms = new ArrayList<String>();
            teachers.add(teacher);
            rooms.add(classRoom);
            var currentSubject = new Subject(weekday, subject, dateStartTime, dateEndTime, teachers, rooms);
            var day = timetable.get(weekday);
            var addNewSubject = true;
            for (var subj: day){
                if(subj.lessonName.equals(currentSubject.lessonName)
                    && subj.lessonStartTime.equals(currentSubject.lessonStartTime))
                {
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

    public static void AddCalendarToDatabase(String pathToCalendar) {
        var parser = new TimetableParsing();
        var calendar = ReadFile(pathToCalendar);
        var timetable = parser.CreateTimeTableDataBase(calendar);

        var gson = new Gson();
        var jsonToWrite = gson.toJson(timetable);
        WriteFile(jsonToWrite);
    }
}
