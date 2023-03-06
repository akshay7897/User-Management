package in.ap.serviceimpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import in.ap.binding.ActiveAccount;
import in.ap.binding.Login;
import in.ap.binding.User;
import in.ap.constants.AppConstants;
import in.ap.entity.UserMaster;
import in.ap.repo.UserMasterRepo;
import in.ap.service.UserMasterService;
import in.ap.util.EmailUtil;

@Service
public class UserMasterServiceImpl implements UserMasterService {

	@Autowired
	private UserMasterRepo userMasterRepo;

	@Autowired
	private EmailUtil emailUtil;

	Logger logger = LoggerFactory.getLogger(UserMasterServiceImpl.class);

	@Override
	public boolean saveUser(User user) {

		logger.info("save method execution started....");

		UserMaster userMaster = new UserMaster();
		BeanUtils.copyProperties(user, userMaster);
		userMaster.setPassword(getRandomString());
		userMaster.setActiveStatus(AppConstants.STATUS);
		userMasterRepo.save(userMaster);

		// todo email logic

		String subject = "Your user registration completed.";
		String filename = "BODY.txt";
		emailUtil.sendEmail(userMaster.getEmail(), subject,
				getBody(userMaster.getName(), userMaster.getPassword(), filename));

		return userMaster.getUserId() != null;
	}

	@Override
	public User getUserByID(Integer id) {

		Optional<UserMaster> entity = userMasterRepo.findById(id);
		if (entity.isPresent()) {
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
		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;
	}

	@Override
	public boolean activeUserStatus(ActiveAccount activeAccount) {

		UserMaster userMaster = new UserMaster();
		userMaster.setEmail(activeAccount.getEmail());
		userMaster.setPassword(activeAccount.getOldPassword());

		Example<UserMaster> of = Example.of(userMaster);

		List<UserMaster> findAll = userMasterRepo.findAll(of);
		if (!findAll.isEmpty()) {
			UserMaster userMaster2 = findAll.get(0);
			if (activeAccount.getNewPassword() == activeAccount.getConfirmPassword()) {
				userMaster2.setPassword(activeAccount.getConfirmPassword());
			}
			userMaster2.setActiveStatus("Active");

			userMasterRepo.save(userMaster2);
			return true;
		} else {

			return false;
		}
	}

	@Override
	public String login(Login login) {

		UserMaster userMaster = new UserMaster();
		userMaster.setEmail(login.getEmail());
		userMaster.setPassword(login.getPassword());

		Example<UserMaster> of = Example.of(userMaster);
		List<UserMaster> users = userMasterRepo.findAll(of);

		if (!users.isEmpty()) {
			UserMaster userMaster2 = users.get(0);

			if (userMaster2.getActiveStatus().equals("Active")) {
				return "login success";
			} else {
				return "account is not activated";
			}
		} else {
			return "invaid credintial";
		}

	}

	@Override
	public boolean changeStatus(Integer id, String status) {

		Optional<UserMaster> entity = userMasterRepo.findById(id);

		if (entity.isPresent()) {
			UserMaster userMaster = entity.get();
			userMaster.setActiveStatus(status);
			userMasterRepo.save(userMaster);
			return true;
		}
		return false;

	}

	@Override
	public String forgotPassword(String email) {

		UserMaster user = userMasterRepo.findByEmail(email);

		if (user == null) {
			return "enter valid mail id";
		}
		String subject = "Regarding forgot password";
		String filename = "forgotpassword.txt";

		boolean sendEmail = emailUtil.sendEmail(email, subject, getBody(user.getName(), user.getPassword(), filename));
		if (sendEmail) {
			return "password send to ur registered mail id";
		}
		return null;

	}

	private String getRandomString() {

		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 6;
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		return generatedString;

	}

	private String getBody(String fullname, String tempPassword, String filename) {
		String url = "";
		String mailBody = null;

		try {
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			StringBuffer stringBuffer = new StringBuffer();

			String line = bufferedReader.readLine();

			while (line != null) {
				stringBuffer.append(line);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			mailBody = stringBuffer.toString();
			mailBody.replace("{Fullname}", fullname);
			mailBody.replace("{tempPassword}", tempPassword);
			mailBody.replace("{url}", url);
			mailBody.replace("{pwd}", tempPassword);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mailBody;

	}

}
