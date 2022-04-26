package mk.ukim.finki.wpproject.web.controller.AddAdControllers;

import mk.ukim.finki.wpproject.model.Category;
import mk.ukim.finki.wpproject.model.City;
import mk.ukim.finki.wpproject.model.ads.realEstates.RealEstateAd;
import mk.ukim.finki.wpproject.model.enums.AdType;
import mk.ukim.finki.wpproject.model.enums.Condition;
import mk.ukim.finki.wpproject.model.exceptions.AdNotFoundException;
import mk.ukim.finki.wpproject.service.CategoryService;
import mk.ukim.finki.wpproject.service.CityService;
import mk.ukim.finki.wpproject.service.RealEstateAdService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/RealEstateAd")
public class RealEstateAdController {

    private final CategoryService categoryService;
    private final RealEstateAdService realEstateAdService;
    private final CityService cityService;

    public RealEstateAdController(CategoryService categoryService, RealEstateAdService realEstateAdService, CityService cityService) {
        this.categoryService = categoryService;
        this.realEstateAdService = realEstateAdService;
        this.cityService = cityService;
    }

    @GetMapping("/{id}")
    public String showRealEstateAd(@PathVariable Long id, Model model){

        RealEstateAd realEstateAd = this.realEstateAdService.findById(id).orElseThrow(() -> new AdNotFoundException(id));
        model.addAttribute("ad", realEstateAd);
        model.addAttribute("bodyContent", "showAdsTemplates/showRealEstateAd");
        return "master";
    }

    @GetMapping("/add-form")
    public String AddRealEstateAdPage(Model model) {

        Category category = this.categoryService.findCategoryByName("Real Estate");
        model.addAttribute("category", category);
        model.addAttribute("bodyContent", "adAdsTemplates/addRealEstateAd");
        return "master";

    }

    @GetMapping("/add-form/{categoryId}")
    public String AddApartmentAdPage(@PathVariable Long categoryId, Model model) {

        if (this.categoryService.findById(categoryId).isPresent()){

            Category category = this.categoryService.findById(categoryId).get();
            List<City> cityList = this.cityService.findAll();
            List<AdType> adTypeList = Arrays.asList(AdType.values());
            List<Condition> conditionList = Arrays.asList(Condition.values());

            model.addAttribute("category_1",category);
            model.addAttribute("cityList", cityList);
            model.addAttribute("adTypeList",adTypeList);
            model.addAttribute("conditionList",conditionList);

            model.addAttribute("bodyContent", "addAdsTemplates/addRealEstateAd");
            return "master";
        }
        return "redirect:/add?error=YouHaveNotSelectedCategory";
    }

    @PostMapping("/add")
    public String saveRealEstateAd(
            @RequestParam(required = false) Long id,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam boolean isExchangePossible,
            @RequestParam boolean isDeliveryPossible,
            @RequestParam Double price,
            @RequestParam String cityName, //todo
            @RequestParam AdType type,
            @RequestParam Condition condition,
            @RequestParam Long categoryId, //todo
            @RequestParam(required = false) Long userId, //todo
            @RequestParam int quadrature
    ) {
        if (id != null) {
            this.realEstateAdService.edit(id, title, description, isExchangePossible, isDeliveryPossible, price,
                    cityName, type, condition, categoryId, quadrature);
        } else {
            this.realEstateAdService.save(title, description, isExchangePossible, isDeliveryPossible, price,
                    cityName, type, condition, categoryId, userId, quadrature);
        }
        return "redirect:/ads";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteRealEstateAd(@PathVariable Long id) {
        this.realEstateAdService.deleteById(id);
        return "redirect:/ads";
    }

    @GetMapping("/edit-form/{id}")
    public String editRealEstateAd(@PathVariable Long id, Model model) {
        if (this.realEstateAdService.findById(id).isPresent()) {
            RealEstateAd realEstateAd = this.realEstateAdService.findById(id).get();
            List<Category> categories = this.categoryService.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("realEstateAd", realEstateAd);
            model.addAttribute("bodyContent", "adsTemplates/addRealEstateAd");
            return "master";
        }
        return "redirect:/ads?error=AdNotFound";
    }
}
