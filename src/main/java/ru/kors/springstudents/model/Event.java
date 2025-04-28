package ru.kors.springstudents.model;

import jakarta.persistence.*; // Используй jakarta.persistence.*
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet; // Импортируй HashSet
import java.util.Set;    // Импортируй Set

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"students"}) // Исключаем коллекцию students
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDateTime date;

    // Меняем List<Student> на Set<Student>
    @ManyToMany(fetch = FetchType.LAZY) // Оставляем LAZY по умолчанию
    @JoinTable(
        name = "student_event", // Имя связующей таблицы
        joinColumns = @JoinColumn(name = "event_id"), // Внешний ключ на текущую таблицу (events)
        inverseJoinColumns = @JoinColumn(name = "student_id") // Внешний ключ на связанную таблицу (students)
    )
    private Set<Student> students = new HashSet<>(); // Инициализируем как HashSet
}