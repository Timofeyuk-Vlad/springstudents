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
import java.util.stream.Collectors;

@Mapper(componentModel = "spring") // Чтобы его можно было внедрить в другие мапперы
public class HelperMapper {

    @Named("getStudentFullName")
    public String getStudentFullName(Student student) {
        if (student == null) {
            return null;
        }
        return student.getFirstName() + " " + student.getLastName();
    }

    // --- Методы для извлечения ID из коллекций ---

    @Named("requestsToRequestIds")
    public List<Long> requestsToRequestIds(List<Request> requests) {
        if (requests == null) {
            return Collections.emptyList();
        }
        return requests.stream().map(Request::getId).collect(Collectors.toList());
    }

    @Named("eventsToEventIds")
    public List<Long> eventsToEventIds(List<Event> events) {
        if (events == null) {
            return Collections.emptyList();
        }
        return events.stream().map(Event::getId).collect(Collectors.toList());
    }

    @Named("dutiesToDutyIds")
    public List<Long> dutiesToDutyIds(List<Duty> duties) {
        if (duties == null) {
            return Collections.emptyList();
        }
        return duties.stream().map(Duty::getId).collect(Collectors.toList());
    }

    @Named("forumPostsToForumPostIds")
    public List<Long> forumPostsToForumPostIds(List<ForumPost> posts) {
        if (posts == null) {
            return Collections.emptyList();
        }
        return posts.stream().map(ForumPost::getId).collect(Collectors.toList());
    }

    @Named("bartersToBarterIds")
    public List<Long> bartersToBarterIds(List<Barter> barters) {
        if (barters == null) {
            return Collections.emptyList();
        }
        return barters.stream().map(Barter::getId).collect(Collectors.toList());
    }

    @Named("studentsToStudentIds")
    public List<Long> studentsToStudentIds(List<Student> students) {
        if (students == null) {
            return Collections.emptyList();
        }
        return students.stream().map(Student::getId).collect(Collectors.toList());
    }
}