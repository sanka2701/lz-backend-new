package sk.liptovzije.api;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

//@RestController
//public class ErrorApi implements ErrorController {
//
//    private static final String PATH = "/error";
//
//    @RequestMapping(value = PATH)
//    public String error(HttpServletRequest aRequest) {
//        return "Error handling";
//    }
//
//    @Override
//    public String getErrorPath() {
//        return PATH;
//    }
//}