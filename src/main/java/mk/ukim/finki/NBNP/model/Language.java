package mk.ukim.finki.NBNP.model;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "Language")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String iso_lang_code;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "language")
    private List<Movie> mainLanguageMovie = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "spokenLanguages")
    private List<Movie> languageInMovies = new ArrayList<>();

    public Language() {

    }

    public Language(String name, String iso_lang_code) {
        this.name = name;
        this.iso_lang_code = iso_lang_code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIso_lang_code(String iso_lang_code) {
        this.iso_lang_code = iso_lang_code;
    }
}
