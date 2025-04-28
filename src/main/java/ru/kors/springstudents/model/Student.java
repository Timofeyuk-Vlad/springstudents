// Student.java
package ru.kors.springstudents.model;

import jakarta.persistence.*; // Используй jakarta.persistence.*
import lombok.*; // Убедись, что все нужные аннотации Lombok есть

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet; // Импортируй HashSet
import java.util.Set;    // Импортируй Set

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"requests", "events", "duties", "forumPosts", "barters"})
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    @Column(unique = true, nullable = false) // Email должен быть not null
    private String email;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true) // Добавь orphanRemoval, если нужно удалять Request при удалении из коллекции
    private Set<Request> requests = new HashSet<>();

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Duty> duties = new HashSet<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ForumPost> forumPosts = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Barter> barters = new HashSet<>();

    public int getAge() {
        if (dateOfBirth == null) {
            return 0;
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}