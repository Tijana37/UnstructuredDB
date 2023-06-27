package mk.ukim.finki.NBNP.model;

import java.io.Serializable;

public class DepartmentId implements Serializable {
    private String name;

    private String jobName;

    // default constructor


    public DepartmentId() {
    }

    public DepartmentId(String name, String jobName) {
        this.name = name;
        this.jobName = jobName;
    }

    // equals() and hashCode()
}