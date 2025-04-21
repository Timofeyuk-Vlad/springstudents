package ru.kors.springstudents.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"student"}) // Исключаем student, чтобы избежать потенциальных циклов в логах
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "barters")
public class Barter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String item;
    private String description;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY) // Указываем FetchType явно
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}