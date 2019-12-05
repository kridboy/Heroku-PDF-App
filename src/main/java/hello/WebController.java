package hello;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


@Controller
public class WebController implements WebMvcConfigurer {
    public static final String APPLICATION_PDF = "application/pdf";
    byte[] file;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/results").setViewName("results");
    }

    @GetMapping("/")
    public String showForm(DataForm dataForm) {
        return "data_template";
    }

    @PostMapping("/")
    public String checkFilledData(@Valid DataForm dataForm, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "data_template";
        }
        file = dataForm.fillData();
        return "redirect:/results";
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET, produces = APPLICATION_PDF)
    public @ResponseBody
    void downloadA(HttpServletResponse response) throws IOException {

        InputStream in = new ByteArrayInputStream(file);
        response.setContentType(APPLICATION_PDF);
        response.setHeader("Content-Disposition", "attachment; filename=" + "test.pdf");
        response.setHeader("Content-Length", String.valueOf(file.length));
        FileCopyUtils.copy(in, response.getOutputStream());
    }

}
