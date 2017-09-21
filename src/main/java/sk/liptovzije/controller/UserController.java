package sk.liptovzije.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.liptovzije.model.DO.UserDO;
import sk.liptovzije.model.DTO.UserCredentialsDTO;
import sk.liptovzije.model.DTO.UserDTO;
import sk.liptovzije.model.Response;
import sk.liptovzije.service.IAuthenticationService;
import sk.liptovzije.service.IJwtService;
import sk.liptovzije.service.IUserService;

@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IAuthenticationService authenticatorService;

    @Autowired
    private IJwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(path = "/test")
    public @ResponseBody String test(){
        log.debug("Reached testing endpoint");
        return "Hello World";
    }

//    @RequestMapping(value= "/**", method=RequestMethod.OPTIONS)
//    public void corsHeaders(HttpServletResponse response) {
//        System.out.println("POJEBANY CORS!!!");
//
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with");
//        response.addHeader("Access-Control-Max-Age", "3600");
//    }


    @RequestMapping(path = "/user/create" , method= RequestMethod.POST)
    public ResponseEntity<String> register(@RequestBody UserDTO newUser){
        HttpStatus status;
        String message;

        log.debug("Attemting to register new user: ", newUser);

        UserDO user = userService.getByUsername(newUser.getUsername());

        log.debug("User creation result: " + (user != null ? "CREATED" : "USER ALREADY EXISTS"));

        if(user != null) {
            status = HttpStatus.BAD_REQUEST;
            message = "status.usernameInUse";
        } else {
            userService.saveUser(newUser.toDo());
            status = HttpStatus.OK;
            message = "status.userCreated";
        }
        return new ResponseEntity<>(message, status);
    }

    @RequestMapping(value = "/user/authenticate", method = RequestMethod.POST)
    public ResponseEntity<Response<?>> login(@RequestBody UserCredentialsDTO credentials) {
        Response<Object> response = new Response<>();
        HttpStatus status;

        UserDO user = userService.getByUsername(credentials.getUsername());

        if(user == null) {
            response.setData("user_not_found");
            status =  HttpStatus.BAD_REQUEST;
        } else if (!authenticatorService.validateCredentials(user.getCredentials(), credentials)) {
            response.setData("password_invalid");
            status = HttpStatus.UNAUTHORIZED;
        } else {
            response.setJwt(jwtService.sign(user));
            status = HttpStatus.OK;
        }

//        if (user != null && authenticatorService.validateCredentials(user.getCredentials(), credentials)) {
//            response.setJwt(jwtService.sign(user));
//            status = HttpStatus.OK;
//        } else {
//            status = HttpStatus.UNAUTHORIZED;
//        }

        return new ResponseEntity<>(response, status);
    }
}
