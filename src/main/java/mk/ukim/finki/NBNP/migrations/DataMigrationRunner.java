package mk.ukim.finki.NBNP.migrations;

import mk.ukim.finki.NBNP.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataMigrationRunner implements CommandLineRunner {

    private final CompanyService companyService;
    private final CountryService countryService;
    private final GenreService genreService;
    private final LanguageService languageService;
    private final UsersService usersService;
    private final KeywordsService keywordsService;
    private final CollectionService collectionService;
    private final DepartmentService departmentService;
    private final PersonService personService;
    private final MovieService movieService;
    private final RatingService ratingService;
    private final CreditsService creditsService;
    private final MovieKeywordService movieKeywordService;
    private final PersonDepartmentService personDepartmentService;

    public DataMigrationRunner(CompanyService companyService, CountryService countryService, GenreService genreService, LanguageService languageService, UsersService usersService, KeywordsService keywordsService, CollectionService collectionService, DepartmentService departmentService, PersonService personService, MovieService movieService, RatingService ratingService, CreditsService creditsService, MovieKeywordService movieKeywordService, PersonDepartmentService personDepartmentService) {
        this.companyService = companyService;
        this.countryService = countryService;
        this.genreService = genreService;
        this.languageService = languageService;
        this.usersService = usersService;
        this.keywordsService = keywordsService;
        this.collectionService = collectionService;
        this.departmentService = departmentService;
        this.personService = personService;
        this.movieService = movieService;
        this.ratingService = ratingService;
        this.creditsService = creditsService;
        this.movieKeywordService = movieKeywordService;
        this.personDepartmentService = personDepartmentService;
    }


    @Override
    public void run(String... args) throws Exception {
        // Call the data migration service method here
//        companyService.migrateData();
//        countryService.migrateData();
//        genreService.migrateData();
//        languageService.migrateData();
//        collectionService.migrateData();
//        movieService.migrateData();
//        departmentService.migrateData();
//        personService.migrateData();
//        personDepartmentService.migrateData();
//        usersService.migrateData();
//        ratingService.migrateData();
//        keywordsService.migrateData();
//        movieKeywordService.migrateData();
//        creditsService.migrateData();
    }
}