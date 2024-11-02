package org.example.myojssm.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.example.myojssm.common.Result;
import org.example.myojssm.entity.User;
import org.example.myojssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: liaoyueyue
 * Date: 2024-03-10
 * Time: 11:47
 */
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<String> login(@NotBlank @Pattern(regexp = "^\\S{2,25}$") String account, @NotBlank @Pattern(regexp = "^\\S{6,16}$") String password, @NotBlank @Pattern(regexp = "^\\S{4,6}$") String captcha) {
        // 1.判断图像验证码 --这里暂时做简单判断
        if (captcha.equals(" ")) {
            return Result.error("验证码错误");
        }
        // 2.执行用户登录
        String jwt = userService.login(account, password);
        if (jwt != null) {
            return Result.success(jwt);
        } else {
            return Result.error("登录失败，请检查账号或者密码");
        }
    }

    @PostMapping("/register")
    public Result<String> register(@NotBlank @Email String email, @NotBlank @Pattern(regexp = "^\\S{4,6}$") String vercode, @NotBlank @Pattern(regexp = "^\\S{6,16}$") String password, @NotBlank @Pattern(regexp = "^\\S{2,16}$") String nickname) {
        // 1.判断邮箱是否存在
        boolean isEmailExist = userService.queryEmailExist(email);
        if (isEmailExist) {
            return Result.error("电子邮件存在");
        }
        // 2.判断邮箱验证码 --这里暂时做简单判断
        if (vercode.equals(" ")) {
            return Result.error("验证码错误");
        }
        // 3.尝试创建用户， 如果创建成功则返回用户名
        String registerUsername = userService.register(email, password, nickname);
        if (registerUsername != null) {
            return Result.success(registerUsername);
        } else {
            return Result.error("用户名存在");
        }
    }

    @GetMapping("/userinfo")
    public Result<User> getUserInfo() {
        User userInfo = userService.getUserInfo();
        if (userInfo != null) {
            return Result.success(userInfo);
        }
        return Result.error("未找到用户");
    }

    @PutMapping("/update-info")
    public Result<Void> updateUserInfo(@RequestBody @Validated(User.Update.class) User user) {
        User newUser = userService.updateUserInfo(user);
        if (newUser != null) {
            return Result.success();
        } else {
            return Result.error("未找到用户");
        }
    }

    @PostMapping("/update-avatar")
    public Result<String> updateAvatar(@NotNull @RequestParam("avatar") MultipartFile avatarFile) {
        String newAvatar = userService.updateAvatar(avatarFile);
        if (newAvatar != null) {
            return Result.success(newAvatar);
        } else {
            return Result.error("头像更新失败");
        }
    }

    @PatchMapping("/update-pwd")
    public Result<Void> updatePwd(@NotBlank @Pattern(regexp = "^\\S{6,16}$") String oldPwd, @NotBlank @Pattern(regexp = "^\\S{6,16}$") String newPwd, @RequestHeader("Authorization") String oldToken) {
        String errorMsg = userService.updatePwd(oldPwd, newPwd, oldToken);
        if (errorMsg == null) {
            return Result.success();
        } else {
            return Result.error(errorMsg);
        }
    }
}
