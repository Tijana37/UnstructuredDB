package mk.ukim.finki.NBNP.model;

import javax.persistence.*;

@Entity
@Table
public class PersonDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Person person;

    @ManyToOne
    private Department department;

    private String job;

    public PersonDepartment() {
    }

    public PersonDepartment(Person person, Department department, String job) {
        this.person = person;
        this.department = department;
        this.job = job;
    }

}
