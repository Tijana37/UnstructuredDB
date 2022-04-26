package mk.ukim.finki.wpproject.web.controller.AddAdControllers;

import mk.ukim.finki.wpproject.model.Category;
import mk.ukim.finki.wpproject.model.City;
import mk.ukim.finki.wpproject.model.ads.BookAd;
import mk.ukim.finki.wpproject.model.enums.AdType;
import mk.ukim.finki.wpproject.model.enums.Condition;
import mk.ukim.finki.wpproject.model.enums.Genre;
import mk.ukim.finki.wpproject.model.exceptions.AdNotFoundException;
import mk.ukim.finki.wpproject.service.BookAdService;
import mk.ukim.finki.wpproject.service.CategoryService;
import mk.ukim.finki.wpproject.service.CityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("/BookAd")
public class BookAdController {

    private final CategoryService categoryService;
    private final BookAdService bookAdService;
    private final CityService cityService;

    public BookAdController(CategoryService categoryService, BookAdService bookAdService, CityService cityService) {
        this.categoryService = categoryService;
        this.bookAdService = bookAdService;
        this.cityService = cityService;
    }

    @GetMapping("/{id}")
    public String showBookAd(@PathVariable Long id, Model model){

        BookAd bookAd = this.bookAdService.findById(id).orElseThrow(() -> new AdNotFoundException(id));
        model.addAttribute("ad", bookAd);
        model.addAttribute("bodyContent", "showAdsTemplates/showBookAd");
        return "master";
    }

    @GetMapping("/add-form")
    public String AddBookAdPage(Model model) {

        Category category = this.categoryService.findCategoryByName("Book");
        model.addAttribute("category", category);
        model.addAttribute("bodyContent", "adAdsTemplates/addBookAd");
        return "master";

    }

    @GetMapping("/add-form/{categoryId}")
    public String AddApartmentAdPage(@PathVariable Long categoryId, Model model) {

        if (this.categoryService.findById(categoryId).isPresent()){
            Category category = this.categoryService.findById(categoryId).get();
            List<City> cityList = this.cityService.findAll();
            List<AdType> adTypeList = Arrays.asList(AdType.values());
            List<Condition> conditionList = Arrays.asList(Condition.values());
            List<Genre> genreList = Arrays.asList(Genre.values());

            model.addAttribute("category_1", category);
            model.addAttribute("cityList", cityList);
            model.addAttribute("adTypeList",adTypeList);
            model.addAttribute("conditionList",conditionList);
            model.addAttribute("genreList",genreList);

            model.addAttribute("bodyContent", "addAdsTemplates/addBookAd");
            return "master";
        }
        return "redirect:/add?error=YouHaveNotSelectedCategory";
    }

    @PostMapping("/add")
    public String saveBookAd(
            @RequestParam(required = false) Long id,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam boolean isExchangePossible,
            @RequestParam boolean isDeliveryPossible,
            @RequestParam Double price,
            @RequestParam String cityId,
            @RequestParam AdType type,
            @RequestParam Condition condition,
            @RequestParam Long categoryId,
            @RequestParam(required = false) Long userId, //todo
            @RequestParam String author,
            @RequestParam int yearMade,
            @RequestParam int numPages,
            @RequestParam Genre genre
    ) {
        if (id != null) {
            this.bookAdService.edit(id, title, description, isExchangePossible, isDeliveryPossible,
                    price, cityId, type, condition, categoryId, author, yearMade, numPages, genre);
        } else {
            this.bookAdService.save(title, description, isExchangePossible, isDeliveryPossible,
                    price, cityId, type, condition, categoryId, userId, author, yearMade, numPages, genre);
        }
        return "redirect:/ads";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBookAd(@PathVariable Long id) {
        this.bookAdService.deleteById(id);
        return "redirect:/ads";
    }

    @GetMapping("/edit-form/{id}")
    public String editBookAd(@PathVariable Long id, Model model) {
        if (this.bookAdService.findById(id).isPresent()) {
            BookAd bookAd = this.bookAdService.findById(id).get();
            List<Category> categories = this.categoryService.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("bookAd", bookAd);
            model.addAttribute("bodyContent", "adsTemplates/addBookAd");
            return "master";
        }
        return "redirect:/ads?error=AdNotFound";
    }
}
