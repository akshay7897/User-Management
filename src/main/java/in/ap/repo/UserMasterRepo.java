package in.ap.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.ap.entity.UserMaster;

@Repository
public interface UserMasterRepo extends JpaRepository<UserMaster, Integer> {

	public UserMaster findByEmail(String email);

}
