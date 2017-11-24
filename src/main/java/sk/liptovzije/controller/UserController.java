package sk.liptovzije.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.liptovzije.model.DO.UserCredentialsDO;
import sk.liptovzije.model.DO.UserDO;
import sk.liptovzije.model.DTO.UserCredentialsDTO;
import sk.liptovzije.model.DTO.UserDTO;
import sk.liptovzije.model.Response;
import sk.liptovzije.service.IAuthenticationService;
import sk.liptovzije.service.ICredentialService;
import sk.liptovzije.service.IJwtService;
import sk.liptovzije.service.IUserService;

@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ICredentialService credentialService;

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

    @RequestMapping(path = "/user/create" , method= RequestMethod.POST)
    public ResponseEntity<String> register(@RequestBody UserDTO newUser){
        HttpStatus status;
        String message;

        log.debug("Attemting to register new user: ", newUser);

        UserCredentialsDO userCredentials = credentialService.getByUsername(newUser.getUsername());

        log.debug("User creation result: " + (userCredentials != null ? "USER ALREADY EXISTS" : "USER WILL BE CREATED"));

        if(userCredentials != null) {
            status = HttpStatus.BAD_REQUEST;
            message = "status.usernameInUse";
        } else {
            Long id = userService.saveUser(newUser.toDo());
            credentialService.save(new UserCredentialsDO(id, newUser.getUsername(), newUser.getPassword()));
            status = HttpStatus.OK;
            message = "status.userCreated";
        }
        return new ResponseEntity<>(message, status);
    }

    @RequestMapping(path = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getSingleUser(@PathVariable Long id){
        UserDTO user = new UserDTO(userService.getById(id));

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

//    @RequestMapping(value = "/user/authenticate", method = RequestMethod.POST)
//    public ResponseEntity<Response<UserDTO>> login(@RequestBody UserCredentialsDTO credentials) {
//        Response<UserDTO> response = new Response<>();
//        HttpStatus status;
//
//        UserCredentialsDO loadedCredentials = credentialService.getByUsername(credentials.getUsername());
//
//        if(loadedCredentials == null) {
//            response.setMessage("status.userNotFound");
//            status =  HttpStatus.BAD_REQUEST;
//        } else if (!authenticatorService.validateCredentials(loadedCredentials, credentials)) {
//            response.setMessage("status.passwordInvalid");
//            status = HttpStatus.UNAUTHORIZED;
//        } else {
//            UserDO user = userService.getById(loadedCredentials.getUserId());
//            response.setJwt(jwtService.sign(user));
//            response.setData(new UserDTO(user));
//            status = HttpStatus.OK;
//        }
//
//        return new ResponseEntity<>(response, status);
//    }
}
