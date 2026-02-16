package com.example.bookingmeeting_be.controller.admin.view;

import com.example.bookingmeeting_be.services.JWTService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminViewAuthController {
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private static final String ACCESS_TOKEN_COOKIE = "ACCESS_TOKEN";
    @GetMapping("/login")
    public String login()
    {
        return "auth/admin-login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletResponse response,
                        Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String jwt = jwtService.generateToken(username);
            ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE, jwt).httpOnly(true).secure(false).path("/").sameSite("Lax").maxAge(60 * 60).build();
            response.addHeader("Set-Cookie", cookie.toString());
            return "redirect:/admin/home";
        } catch (AuthenticationException ex) {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu");
            return "auth/admin-login";
        }
    }
    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE, "").httpOnly(true).secure(false).path("/").sameSite("Lax").maxAge(0).build();
        response.addHeader("Set-Cookie", cookie.toString());
        return "redirect:/admin/login";
    }
}
