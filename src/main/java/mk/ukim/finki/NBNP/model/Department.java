package mk.ukim.finki.NBNP.model;

import javax.persistence.*;

@Entity
@Table(name = "Department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Department() {

    }

    public Department(String name) {
        this.name = name;
    }
}
