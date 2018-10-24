package com.clients;

import static org.junit.Assert.*;


import com.server.TimetableParsing;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;


public class TestTimetableParser {

    @Test
    public void testTimeTableParser() {
        System.out.println(new File(".").getAbsolutePath());
        var calendar = TimetableParsing.ReadFile("DataBase/calendar_fiit_202.ics");
        ComponentList wholeTimetable = calendar.getComponents(Component.VEVENT);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        var timetable = TimetableParsing.CreateTimeTableDataBase(calendar);
        var monday = timetable.get("Понедельник");
        var subj = monday.get(0);

        var firstSubject = (VEvent) wholeTimetable.get(0);
        var subject = firstSubject.getSummary().getValue();
        var dateStart = firstSubject.getStartDate().getDate();
        var dateEnd = firstSubject.getEndDate().getDate();
        var dateStartTime = timeFormat.format(dateStart);
        var dateEndTime = timeFormat.format(dateEnd);
        var weekday = TimetableParsing.DetermineDay(dateStart);

        assertEquals(subject, subj.lessonName);
        assertEquals(dateStartTime, subj.lessonStartTime);
        assertEquals(dateEndTime, subj.lessonEndTime);
        assertEquals(weekday, subj.weekday);
    }
}