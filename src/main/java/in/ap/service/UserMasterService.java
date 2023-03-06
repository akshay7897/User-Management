package in.ap.service;

import java.util.List;

import in.ap.binding.ActiveAccount;
import in.ap.binding.Login;
import in.ap.binding.User;

public interface UserMasterService {
	
	public boolean saveUser(User user); //
	
	public User getUserByID(Integer id);//
	
	public List<User> getAllUsers();//
	
	public boolean activeUserStatus(ActiveAccount activeAccount);//
	
	public boolean deleteUserById(Integer id);//
	
	public String login(Login login);//
	
	public boolean changeStatus(Integer id,String status);
	
	public String forgotPassword(String email);
	

}
