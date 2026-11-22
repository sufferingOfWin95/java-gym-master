package ru.yandex.practicum.gym;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        assertEquals(0, timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        TreeMap<TimeOfDay, List<TrainingSession>> thursday =
                timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);

        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        assertEquals(0, timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size());
        assertEquals(2, thursday.size());
        assertEquals(13, thursday.firstKey().getHours());
        assertEquals(20, thursday.lastKey().getHours());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> result =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        assertEquals(1, result.size());
        assertEquals(singleTrainingSession, result.getFirst());
        assertEquals(0,
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)).size());
    }

    @Test
    void testTwoSessionsAtSameTimeBothReturned() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group1 = new Group("Акробатика для детей", Age.CHILD, 60);
        Group group2 = new Group("Плаванье для взрослых", Age.ADULT, 60);
        TrainingSession firstTrainingSession = new TrainingSession(group1, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession secondTrainingSession = new TrainingSession(group2, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(firstTrainingSession);
        timetable.addNewTrainingSession(secondTrainingSession);

        List<TrainingSession> result =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        assertEquals(2, result.size());
        assertTrue(result.contains(firstTrainingSession));
        assertTrue(result.contains(secondTrainingSession));
    }

    @Test
    void testEmptyTimetableReturnsNoSessions() {
        Timetable timetable = new Timetable();

        List<TrainingSession> sessions =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));

        assertTrue(sessions.isEmpty());
        assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).isEmpty());
    }

    @Test
    void testSessionsSortedByMinutesWithinSameHour() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для детей", Age.CHILD, 15);
        TrainingSession firstTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(12, 30));
        TrainingSession secondTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(12, 50));
        TrainingSession thirdTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(12, 0));

        timetable.addNewTrainingSession(firstTrainingSession);
        timetable.addNewTrainingSession(secondTrainingSession);
        timetable.addNewTrainingSession(thirdTrainingSession);

        TreeMap<TimeOfDay, List<TrainingSession>> monday =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        assertEquals(0, monday.firstKey().getMinutes());
        assertEquals(50, monday.lastKey().getMinutes());
    }

    @Test
    void testGetCountByCoachesSortedDescending() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Семёнов", "Валерий", "Сергеевич");
        Group group = new Group("Акробатика для детей", Age.CHILD, 15);
        TrainingSession firstTrainingSession = new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(12, 30));
        TrainingSession secondTrainingSession = new TrainingSession(group, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(13, 30));
        TrainingSession thirdTrainingSession = new TrainingSession(group, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(14, 30));
        TrainingSession fourthTrainingSession = new TrainingSession(group, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(15, 30));

        timetable.addNewTrainingSession(firstTrainingSession);
        timetable.addNewTrainingSession(secondTrainingSession);
        timetable.addNewTrainingSession(thirdTrainingSession);
        timetable.addNewTrainingSession(fourthTrainingSession);

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        assertEquals(2, result.size());
        assertEquals("Семёнов", result.get(0).getCoach().getSurname());
        assertEquals(3, result.get(0).getCount());
        assertEquals("Васильев", result.get(1).getCoach().getSurname());
        assertEquals(1, result.get(1).getCount());
    }

    @Test
    void testGetCountByCoachesEmptyTimetable() {
        Timetable timetable = new Timetable();

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetCountByCoachesSameCoachDifferentObjects() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для детей", Age.CHILD, 15);
        TrainingSession firstTrainingSession = new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(12, 30));
        TrainingSession secondTrainingSession = new TrainingSession(group, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(13, 30));

        timetable.addNewTrainingSession(firstTrainingSession);
        timetable.addNewTrainingSession(secondTrainingSession);

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getCount());
    }
}
