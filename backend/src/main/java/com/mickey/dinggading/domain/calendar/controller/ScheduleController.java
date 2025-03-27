package com.mickey.dinggading.domain.calendar.controller;

import com.mickey.dinggading.api.CalendarApi;
import com.mickey.dinggading.model.CreateEventRequest;
import com.mickey.dinggading.model.UpdateEventRequest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public class ScheduleController implements CalendarApi {

    @Override
    public ResponseEntity<?> createEvent(Long calendarId, CreateEventRequest createEventRequest) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteEvent(Long calendarId, Long eventId) {
        return null;
    }

    @Override
    public ResponseEntity<?> getCalendar(Long calendarId, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateEvent(Long calendarId, Long eventId, UpdateEventRequest updateEventRequest) {
        return null;
    }
}
