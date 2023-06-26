package mk.ukim.finki.wpproject.model;

import lombok.AllArgsConstructor;

import java.util.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer gender;

    public User() {

    }
}
