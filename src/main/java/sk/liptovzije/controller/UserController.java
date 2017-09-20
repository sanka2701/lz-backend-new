package sk.liptovzije.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IAuthenticationService authenticatorService;

    @Autowired
    private IJwtService jwtService;

    @RequestMapping(path = "/test")
    public String test(){
        System.out.println("BREKEKEBR");
        return "Hello Kurva";
    }

    @RequestMapping(path = "/test2")
    public ResponseEntity<String> test2( String str){
        System.out.println("MUHEHE got: " + str);

//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Access-Control-Allow-Origin", "*");
//        headers.add("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
//        headers.add("Access-Control-Allow-Headers", "Origin, Content-Type, X-Auth-Token");

        String message = "Hello World";

        return new ResponseEntity<>(message, /*headers,*/ HttpStatus.OK);
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
        UserDO user = userService.getByUsername(newUser.getUsername());

        if(user == null) {
            userService.saveUser(newUser.toDo());
            status = HttpStatus.OK;
            message = "status.userCreated";
        } else {
            status = HttpStatus.BAD_REQUEST;
            message = "status.usernameInUse";
        }
        return new ResponseEntity<>(message, status);
    }

    @RequestMapping(value = "/user/authenticate", method = RequestMethod.POST)
    public ResponseEntity<Response<UserDO>> login(@RequestBody UserCredentialsDTO credentials) {

        Response<UserDO> response = null;
        HttpStatus status;

        UserDO user = userService.getByUsername(credentials.getUsername());
        if (user != null && authenticatorService.validCredentials(user.getCredentials(), credentials)) {
            response = jwtService.sign(user);
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(response, status);
    }
}
