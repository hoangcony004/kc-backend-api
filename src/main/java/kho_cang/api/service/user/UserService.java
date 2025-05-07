package kho_cang.api.service.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kho_cang.api.entiy.system.SysUser;
import kho_cang.api.repository.user.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SysUser createUser(SysUser user) {
        return userRepository.save(user);
    }

    public List<SysUser> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<SysUser> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public SysUser updateUser(Long id, SysUser userDetails) {
        Optional<SysUser> user = userRepository.findById(id);
        if (user.isPresent()) {
            SysUser updatedUser = user.get();
            updatedUser.setUsername(userDetails.getUsername());
            updatedUser.setPassword(userDetails.getPassword());
            updatedUser.setEmail(userDetails.getEmail());
            return userRepository.save(updatedUser);
        }
        return null;
    }

    public boolean deleteUser(Long id) {
        Optional<SysUser> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
