package com.example.bookingmeeting_be.services;

import com.example.bookingmeeting_be.model.Role;
import com.example.bookingmeeting_be.model.Users;
import com.example.bookingmeeting_be.model.dto.UserResponse;
import com.example.bookingmeeting_be.repository.RoleRepository;
import com.example.bookingmeeting_be.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    public Users register(Users user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Role roleUser = roleRepository
                .findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ROLE_USER");
                    return roleRepository.save(r);
                });

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Set.of(roleUser));

        return userRepository.save(user);
    }

    public String verify(Users user) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getEmail());
        }
        throw new RuntimeException("Login failed");
    }

    @Transactional
    public Page<UserResponse> listUsers(String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("userId").descending());

        Page<Users> usersPage;
        if (q == null || q.isBlank()) {
            usersPage = userRepository.findAll(pageable);
        } else {
            usersPage = userRepository.findByEmailContainingIgnoreCaseOrFullnameContainingIgnoreCase(q, q, q, pageable);
        }

        return usersPage.map(this::toDto);
    }

    private UserResponse toDto(Users user) {
        UserResponse dto = new UserResponse();
        dto.setUserId(user.getUserId());
        dto.setEmail(user.getEmail());
        dto.setFullname(user.getFullname());
        dto.setPhone(user.getPhone());
        dto.setPassword("");

        var roleNames = (user.getRole() == null) ? java.util.List.<String>of()
                : user.getRole().stream().map(Role::getName).toList();

        dto.setRoles(roleNames);
        dto.setRolesText(String.join(",", roleNames));
        return dto;
    }

    @Transactional
    public void updateUserAdmin(int userId, Set<String> roleNames, UserResponse updateData) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullname(updateData.getFullname());
        user.setPhone(updateData.getPhone());

        if (updateData.getPassword() != null && !updateData.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updateData.getPassword()));
        }

        Set<Role> newRoles = roleNames.stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + name)))
                .collect(Collectors.toSet());
        user.setRole(newRoles);

        userRepository.save(user);
    }

    public List<Users> findBookers() {return userRepository.findUsersByRoleName("ROLE_USER");}

    public List<Users> findAll(){
        return userRepository.findAll();
    }

    public Users findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    public void updateProfile(Users user, String fullname, String phone, String address,
                              String company, String jobTitle, String about,
                              MultipartFile multipartFile) throws IOException {

        if (fullname != null && !fullname.isEmpty()) user.setFullname(fullname);
        user.setPhone(phone);
        user.setAddress(address);
        user.setCompany(company);
        user.setJobTitle(jobTitle);
        user.setAbout(about);

        if (multipartFile != null && !multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            user.setAvatar(fileName);

            String uploadDir = "user-photos/" + user.getUserId();
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = multipartFile.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        }

        userRepository.save(user);
    }

    @Transactional
    public void createUserAdmin(UserResponse data, Set<String> roleNames) {
        if (userRepository.existsByEmail(data.getEmail())) {
            throw new RuntimeException("Email này đã được sử dụng!");
        }

        Users newUser = new Users();
        newUser.setFullname(data.getFullname());
        newUser.setEmail(data.getEmail());
        newUser.setPhone(data.getPhone());

        String rawPassword = (data.getPassword() != null && !data.getPassword().isEmpty())
                ? data.getPassword() : "onmeet2026";
        newUser.setPassword(passwordEncoder.encode(rawPassword));

        Set<Role> roles = roleNames.stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò: " + name)))
                .collect(Collectors.toSet());
        newUser.setRole(roles);

        userRepository.save(newUser);
    }

    public boolean changePassword(Users user, String currentPass, String newPass) {
        if (!passwordEncoder.matches(currentPass, user.getPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPass));
        userRepository.save(user);
        return true;
    }

    public Page<Users> searchColleagues(String keyword, String currentUserEmail, Pageable pageable) {
        if (StringUtils.hasText(keyword)) {
            return userRepository.searchUsers(keyword.trim(), currentUserEmail, pageable);
        }
        return userRepository.findAllExceptMe(currentUserEmail, pageable);
    }
}