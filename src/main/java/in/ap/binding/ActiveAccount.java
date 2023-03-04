package in.ap.binding;

import lombok.Data;

@Data
public class ActiveAccount {
	
	private String email;
	private String oldPassword;
	private String newPassword;
	private String confirmPassword;
		

}
