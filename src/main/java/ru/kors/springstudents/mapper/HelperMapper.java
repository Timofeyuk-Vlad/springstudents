package ru.kors.springstudents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ru.kors.springstudents.model.Student;
import ru.kors.springstudents.model.Event;
import ru.kors.springstudents.model.Barter;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public class HelperMapper {

    @Named("getStudentFullName")
    public String getStudentFullName(Student student) {
        if (student == null) {
            return null;
        }
        return student.getFirstName() + " " + student.getLastName();
    }

    @Named("eventsToEventIds")
    public List<Long> eventsToEventIds(Set<Event> events) {
        if (events == null) {
            return Collections.emptyList();
        }
        return events.stream().map(Event::getId).collect(Collectors.toList());
    }

    @Named("bartersToBarterIds")
    public List<Long> bartersToBarterIds(Set<Barter> barters) {
        if (barters == null) {
            return Collections.emptyList();
        }
        return barters.stream().map(Barter::getId).collect(Collectors.toList());
    }

    @Named("studentsToStudentIds")
    public List<Long> studentsToStudentIds(Set<Student> students) {
        if (students == null) {
            return Collections.emptyList();
        }
        return students.stream().map(Student::getId).collect(Collectors.toList());
    }
}