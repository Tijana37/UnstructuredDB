package mk.ukim.finki.NBNP.model;


import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "Movies")
public class Movie {
    @Id
    private Long id;
    private String title;
    private String original_title;
    private Float budget;
    private Boolean adult;
    private String imdb_ID;
    @Column(columnDefinition = "text")
    private String overview;
    private Float popularity;
    private String poster_path;
    private Date release_date;
    private Float revenue;
    private Float runtime;
    private String status;
    @Column(columnDefinition = "text")
    private String tagline;
    private Integer vote_count;
    private Float vote_average;

    // relationships

    @ManyToOne
    private Collection collection;

    @ManyToOne
    private Language language;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Genre> genres = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Language> spokenLanguages = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Company> productionCompanies = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Country> productionCountries = new ArrayList<>();

    public Movie() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setBudget(Float budget) {
        this.budget = budget;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public void setImdb_ID(String imdb_ID) {
        this.imdb_ID = imdb_ID;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    public void setRevenue(Float revenue) {
        this.revenue = revenue;
    }

    public void setRuntime(Float runtime) {
        this.runtime = runtime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setVote_count(Integer vote_count) {
        this.vote_count = vote_count;
    }

    public void setVote_average(Float vote_average) {
        this.vote_average = vote_average;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public void setSpokenLanguages(List<Language> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public void setProductionCompanies(List<Company> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public void setProductionCountries(List<Country> productionCountries) {
        this.productionCountries = productionCountries;
    }
}

