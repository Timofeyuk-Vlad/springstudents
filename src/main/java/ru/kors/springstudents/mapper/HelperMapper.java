package ru.kors.springstudents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ru.kors.springstudents.model.Student;
import ru.kors.springstudents.model.Request;
import ru.kors.springstudents.model.Event;
import ru.kors.springstudents.model.Duty;
import ru.kors.springstudents.model.ForumPost;
import ru.kors.springstudents.model.Barter;

import java.util.Collections;
import java.util.List;
import java.util.Set; // Используем Set
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public class HelperMapper {

    // Этот метод может остаться, если он где-то нужен
    @Named("getStudentFullName")
    public String getStudentFullName(Student student) {
        if (student == null) {
            return null;
        }
        return student.getFirstName() + " " + student.getLastName();
    }

    // --- Методы для извлечения ID из коллекций (принимают Set) ---

    @Named("requestsToRequestIds")
    public List<Long> requestsToRequestIds(Set<Request> requests) { // Принимаем Set
        if (requests == null) {
            return Collections.emptyList();
        }
        return requests.stream().map(Request::getId).collect(Collectors.toList());
    }

    @Named("eventsToEventIds")
    public List<Long> eventsToEventIds(Set<Event> events) { // Принимаем Set
        if (events == null) {
            return Collections.emptyList();
        }
        return events.stream().map(Event::getId).collect(Collectors.toList());
    }

    @Named("dutiesToDutyIds")
    public List<Long> dutiesToDutyIds(Set<Duty> duties) { // Принимаем Set
        if (duties == null) {
            return Collections.emptyList();
        }
        return duties.stream().map(Duty::getId).collect(Collectors.toList());
    }

    @Named("forumPostsToForumPostIds")
    public List<Long> forumPostsToForumPostIds(Set<ForumPost> posts) { // Принимаем Set
        if (posts == null) {
            return Collections.emptyList();
        }
        return posts.stream().map(ForumPost::getId).collect(Collectors.toList());
    }

    @Named("bartersToBarterIds")
    public List<Long> bartersToBarterIds(Set<Barter> barters) { // Принимаем Set
        if (barters == null) {
            return Collections.emptyList();
        }
        return barters.stream().map(Barter::getId).collect(Collectors.toList());
    }

    // Используется в EventMapper (принимает Set)
    @Named("studentsToStudentIds")
    public List<Long> studentsToStudentIds(Set<Student> students) { // Принимаем Set
        if (students == null) {
            return Collections.emptyList();
        }
        return students.stream().map(Student::getId).collect(Collectors.toList());
    }
}