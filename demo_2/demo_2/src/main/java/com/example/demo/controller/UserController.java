package com.example.demo.controller;

import com.example.demo.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.example.demo.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Tag(name = "user", description = "the user API")
@RequestMapping("/api/v1")//this is the base url for api.but if you don't want to use this kind of url you can avoid this line
//so this api will be accessible from http://localhost:8080/

public class UserController {

    @Autowired
    private UserServices services;
    
	@Operation(summary = "Get a list of all users", description = "Admin can fetch list of all users", tags = { "user" })
	@ApiResponses(value = { @ApiResponse(responseCode="200", description = "Info about all the users existing in database obtained", content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class))), @Content(mediaType = "application/xml",  array = @ArraySchema(schema = @Schema(implementation = User.class))) }), 
			@ApiResponse(responseCode="400", description = "Bad Request: The request could not be understood by the server.")})
    @GetMapping("/users")//you can give this any name you want and after adding this string to the end of base url you can use this
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(services.getUserList());
    }
	@Operation(summary = "Get a user by id", description = "Admin can fetch a user by id", tags = { "user" })
	@ApiResponses(value = { @ApiResponse(responseCode="200", description = "User info obtained", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)), @Content(mediaType = "application/xml", schema = @Schema(implementation = User.class)) }), 
			@ApiResponse(responseCode="400", description = "Bad Request: The request could not be understood by the server.")})
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        return ResponseEntity.ok().body(this.services.getUserById(id));
    }
	
	@Operation(summary = "Adding a user", description = "Admin can create a user entry ", tags = { "user" })
	@ApiResponses(value = { @ApiResponse(responseCode="200", description = "User added successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)), @Content(mediaType = "application/xml", schema = @Schema(implementation = User.class)) }), 
			@ApiResponse(responseCode="400", description = "Bad Request: The request could not be understood by the server.")})
    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return ResponseEntity.ok(this.services.createUser(user));
    }
	
	@Operation(summary = "Adding a list of users", description = "Admin can create a list of users ", tags = { "user" })
	@ApiResponses(value = { @ApiResponse(responseCode="200", description = "List of users added successfully", content = { @Content(mediaType = "application/json",  array = @ArraySchema(schema = @Schema(implementation = User.class))), @Content(mediaType = "application/xml",  array = @ArraySchema(schema = @Schema(implementation = User.class))) }), 
			@ApiResponse(responseCode="400", description = "Bad Request: The request could not be understood by the server.") })
    @PostMapping("/addUsers")
    public ResponseEntity<List<User>> addUsers(@RequestBody List<User> list) {
        return ResponseEntity.ok(this.services.createUserList(list));
    }
	
	@Operation(summary = "Updating info of a user", description = "Admin can update incorrect attribute info with correct one ", tags = { "user" })
	@ApiResponses(value = { @ApiResponse(responseCode="200", description = "entity updated", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)), @Content(mediaType = "application/xml", schema = @Schema(implementation = User.class)) }), 
			@ApiResponse(responseCode="400", description = "Bad Request: The request could not be understood by the server due to malformed syntax.")})
    @PutMapping("/updateUsers/")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok().body(this.services.updateUserById(user));
    }
	
	@Operation(summary = "Deleting a user entity", description = "Admin can delete the user info from the record ", tags = { "user" })
	@ApiResponses(value = { @ApiResponse(responseCode="200", description = "user deleted successfully"), 
			@ApiResponse(responseCode="400", description = "Bad Request: The request could not be understood by the server.")})
    @DeleteMapping("/deleteUsers/{id}")
    public HttpStatus deleteUser(@PathVariable int id) {
        this.services.deleteUserById(id);
        return HttpStatus.OK;
    }

}
