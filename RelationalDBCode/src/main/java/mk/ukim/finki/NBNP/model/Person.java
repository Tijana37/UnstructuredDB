package mk.ukim.finki.NBNP.model;

import javax.persistence.*;

@Table
@Entity(name = "Person")
public class Person {

    @Id
    private Long id;
    private String name;
    private Integer gender;


    public Person() {

    }

    public Person(Long id, String name, Integer gender) {
        this.id = id;
        this.name = name;
        this.gender = gender;
    }

}
