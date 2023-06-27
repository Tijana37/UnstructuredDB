package mk.ukim.finki.NBNP.model;

import javax.persistence.*;

@Entity
@Table(name = "Users")
public class Users {

    @Id
    private Long id;
    private String username;

    public Users() {

    }

    public Users(Long id, String username) {
        this.id = id;
        this.username = username;
    }

}
