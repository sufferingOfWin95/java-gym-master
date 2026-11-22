package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();

        if (!timetable.containsKey(dayOfWeek)) {
            timetable.put(dayOfWeek, new TreeMap<>());
        }

        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);

        if (!dayMap.containsKey(timeOfDay)) {
            dayMap.put(timeOfDay, new ArrayList<>());
        }
        List<TrainingSession> sessions = dayMap.get(timeOfDay);
        sessions.add(trainingSession);
    }

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.getOrDefault(dayOfWeek, new TreeMap<>()); // сложность O(1)
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);
        if (dayMap == null) {
            return new ArrayList<>();
        }
        return dayMap.getOrDefault(timeOfDay, new ArrayList<>()); // сложность O(log n)
    }
}
