package webtech_semester_4.webtech.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FoodBookController {

    @GetMapping(path = "/")
    public ModelAndView showFoodBook() {
        return new ModelAndView("foodbook");
    }


}
