package in.ap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import in.ap.binding.ActiveAccount;
import in.ap.binding.Login;
import in.ap.binding.User;
import in.ap.service.UserMasterService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class UserRestController {
	
	@Autowired
	private UserMasterService service;

	@PostMapping("/save")
	public ResponseEntity<String> saveUser(@RequestBody User user){
		
		boolean saveUser = service.saveUser(user);
		if(saveUser) {
			return new ResponseEntity<>("User Saved",HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>("User Not Saved",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	// using @responseStatusAnnotation 
	
	@PostMapping("/activate")
	@ResponseStatus(HttpStatus.CREATED)
	public String activeUser(@RequestBody ActiveAccount activeAccount){
		
		boolean isActive = service.activeUserStatus(activeAccount);
		if(isActive) {
			return "User activated";
		}else {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"User Not Activated");
		}
		
	}
	
	@GetMapping("/users")
	@ResponseStatus(HttpStatus.OK)
	public List<User> getAllUser(){
		
		List<User> users = service.getAllUsers();
		return users;
		
	}
	
	@GetMapping("/user/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public User getUser(@PathVariable Integer userId) {
		
		return service.getUserByID(userId);
		
	}
	
	@DeleteMapping("user/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public String deleteUser(@PathVariable Integer userId) {
		boolean isDeleted = service.deleteUserById(userId);
		if(isDeleted) {
			return "User Deleted Succefully..";
		}else {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"User Not Deleted"); 
		}
	}
	
	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public String doLogin(@RequestBody Login login) {
		
		String log = service.login(login);
		return log;
	}
	
	@PutMapping("/updateStatus/{userId}/{Status}")
	@ResponseStatus(HttpStatus.OK)
	public String changeStatus(@PathVariable Integer userId,@PathVariable String Status) {
		
		boolean isStatusChanged = service.changeStatus(userId, Status);
		
		if(isStatusChanged) {
			return "Status is Updated";
		}else {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Status is not updated");
		}
		
	}
	
	@GetMapping("/forgotpassword/{email}")
	@ResponseStatus(HttpStatus.OK)
	public String forgotPasword(@PathVariable String email) {
		
		String forgotPassword = service.forgotPassword(email);
		return forgotPassword; 
		
	}
	
	
	
}
