package kho_cang.api.service.user;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kho_cang.api.entiy.system.SysUser;
import kho_cang.api.repository.user.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SysUser createUser(SysUser user) {
        try {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo người dùng: " + e.getMessage(), e);
        }
    }

    public Page<SysUser> postUsersWithSearch_Paging(String strKey, Pageable pageable) {
        try {
            if (strKey == null || strKey.isEmpty()) {
                Pageable sortedPageable = PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by(Sort.Direction.DESC, "createdAt"));
                return userRepository.findAll(sortedPageable);
            } else {
                return userRepository.searchAllFields(strKey, pageable);
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách người dùng: " + e.getMessage(), e);
        }
    }

    public Optional<SysUser> getUserById(Long id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm kiếm người dùng với id " + id + ": " + e.getMessage(), e);
        }
    }

    public SysUser updateUser(Long id, SysUser userDetails) {
        try {
            Optional<SysUser> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()) {
                throw new RuntimeException("User không tồn tại với id = " + id);
            }

            SysUser user = userOptional.get();
            boolean isChanged = false;

            isChanged |= updateIfChanged(user::getFullname, user::setFullname, userDetails.getFullname());
            isChanged |= updateIfChanged(user::getEmail, user::setEmail, userDetails.getEmail());
            isChanged |= updateIfChanged(user::getPhone, user::setPhone, userDetails.getPhone());
            isChanged |= updateIfChanged(user::getGender, user::setGender, userDetails.getGender());
            isChanged |= updateIfChanged(user::getAddress, user::setAddress, userDetails.getAddress());
            isChanged |= updateIfChanged(user::getAvatarUrl, user::setAvatarUrl, userDetails.getAvatarUrl());
            isChanged |= updateIfChanged(user::getStatus, user::setStatus, userDetails.getStatus());
            isChanged |= updateIfChanged(user::getUnitcode, user::setUnitcode, userDetails.getUnitcode());

            // Password xử lý riêng vì cần mã hoá
            if (!userDetails.getPassword().isBlank() &&
                    !passwordEncoder.matches(userDetails.getPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
                isChanged = true;
            }

            if (isChanged) {
                user.setUpdatedAt(LocalDateTime.now());
                return userRepository.save(user);
            } else {
                return user;
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật người dùng với id " + id + ": " + e.getMessage(), e);
        }
    }

    // Helper method
    private <T> boolean updateIfChanged(Supplier<T> getter, Consumer<T> setter, T newValue) {
        T currentValue = getter.get();
        if (!Objects.equals(currentValue, newValue)) {
            setter.accept(newValue);
            return true;
        }
        return false;
    }

    public boolean deleteUser(Long id) {
        try {
            Optional<SysUser> user = userRepository.findById(id);
            if (user.isPresent()) {
                userRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa người dùng với id " + id + ": " + e.getMessage(), e);
        }
    }

}
