package in.ap.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import in.ap.binding.User;
import in.ap.entity.UserMaster;
import in.ap.repo.UserMasterRepo;
import in.ap.service.UserMasterService;

@Service
public class UserMasterServiceImpl implements UserMasterService {

	@Autowired
	private UserMasterRepo userMasterRepo;

	@Override
	public boolean saveUser(User user) {

		UserMaster userMaster = new UserMaster();
		BeanUtils.copyProperties(user, userMaster);
		userMaster.setPassword(getRandomString());
		userMaster.setActiveStatus("in-Active");
		userMasterRepo.save(userMaster);

		// todo email logic

		return userMaster.getUserId() != null;
	}

	@Override
	public User getUserByID(Integer id) {

		Optional<UserMaster> entity = userMasterRepo.findById(id);
		if (entity != null) {
			User user = new User();
			BeanUtils.copyProperties(entity, user);
			return user;
		}
		return null;
	}

	@Override
	public List<User> getAllUsers() {

		List<UserMaster> entity = userMasterRepo.findAll();
		List<User> users = new ArrayList<>();

		for (UserMaster u : entity) {
			User user = new User();
			BeanUtils.copyProperties(u, users);
			users.add(user);
		}

		return users;

	}

	@Override
	public boolean deleteUserById(Integer id) {
		try {
		userMasterRepo.deleteById(id);
		return true;
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		return false;
	}

	@Override
	public String login(String email, String password) {
		
		UserMaster userMaster=new UserMaster();
		userMaster.setEmail(email);
		userMaster.setPassword(password);
		
		Example<UserMaster> of = Example.of(userMaster);
		List<UserMaster> users = userMasterRepo.findAll(of);
		if(!users.isEmpty()) {
			UserMaster userMaster2 = users.get(0);
			
			return "login success";
		}else {
			return "invaid credintial";
		}
		
	}

	@Override
	public boolean changeStatus(Integer id, String status) {
		return false;
	}

	@Override
	public String forgotPassword(String email) {
		
		UserMaster user = userMasterRepo.findByEmail(email);
		
		//send user.getemail and user.getpassword to registered email.
		
		return "password send to ur registered mail id";
	}

	private String getRandomString() {

		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		return generatedString;

	}

}
