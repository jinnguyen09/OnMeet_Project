package com.example.bookingmeeting_be.controller.user.view;

import com.example.bookingmeeting_be.model.Users;
import com.example.bookingmeeting_be.repository.UserRepository;
import com.example.bookingmeeting_be.services.JWTService;
import com.example.bookingmeeting_be.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserViewAuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private static final String ACCESS_TOKEN_COOKIE = "ACCESS_TOKEN";
    @GetMapping("/login")
    public String login()
    {
        return "auth/login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpServletResponse response, Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String jwt = jwtService.generateToken(username);
            ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE, jwt).httpOnly(true).secure(false).path("/").sameSite("Lax").maxAge(60 * 60).build();
            response.addHeader("Set-Cookie", cookie.toString());
            return "redirect:/users/home";
        } catch (AuthenticationException ex) {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu");
            return "auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", "")
                .httpOnly(true).secure(false).path("/")
                .sameSite("Lax").maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        return "redirect:/users/home";
    }

    @GetMapping("/debug/me")
    @ResponseBody
    public Object me(Authentication authentication) {
        return authentication;
    }
    @GetMapping("/register")
    public String register()
    {
        return "auth/register";
    }
    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           @RequestParam(required = false) String fullname,
                           HttpServletResponse response,
                           Model model) {
        try {
            String e = email == null ? "" : email.trim();
            String p = password == null ? "" : password.trim();
            String cp = confirmPassword == null ? "" : confirmPassword.trim();

            if (e.isEmpty() || p.isEmpty() || cp.isEmpty()) {
                model.addAttribute("error", "Vui lòng nhập đầy đủ Email, Mật khẩu và Xác nhận mật khẩu.");
                return "auth/register";
            }

            if (p.length() < 6) {
                model.addAttribute("error", "Mật khẩu tối thiểu 6 ký tự.");
                return "auth/register";
            }

            if (!p.equals(cp)) {
                model.addAttribute("error", "Mật khẩu xác nhận không khớp.");
                return "auth/register";
            }

            Users u = new Users();
            u.setEmail(e);
            u.setPassword(p);
            if (fullname != null && !fullname.trim().isEmpty()) {
                u.setFullname(fullname.trim());
            }

            userService.register(u);

            // auto-login
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(e, p)
            );

            String jwt = jwtService.generateToken(e);
            ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", jwt)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .sameSite("Lax")
                    .maxAge(60 * 60)
                    .build();

            response.addHeader("Set-Cookie", cookie.toString());
            return "redirect:/users/home";

        } catch (AuthenticationException ex) {
            model.addAttribute("error", "Đăng ký thành công nhưng đăng nhập tự động thất bại, hãy đăng nhập lại.");
            return "redirect:/users/login";

        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage()); // Email already exists...
            return "auth/register";
        }
    }


}


