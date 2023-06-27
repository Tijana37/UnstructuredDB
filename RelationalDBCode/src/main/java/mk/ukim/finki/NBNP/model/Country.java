package mk.ukim.finki.NBNP.model;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "Country")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String iso_country_code;
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "productionCountries")
    private List<Movie> movies = new ArrayList<>();

    public Country() {

    }

    public Country(String iso_country_code, String name) {
        this.iso_country_code = iso_country_code;
        this.name = name;
    }

    public void setIso_country_code(String iso_country_code) {
        this.iso_country_code = iso_country_code;
    }

    public void setName(String name) {
        this.name = name;
    }
}