package org.example.myojssm.service.impl;

import org.example.myojssm.common.utils.AliyunOSSUtil;
import org.example.myojssm.common.utils.JWTUtil;
import org.example.myojssm.common.utils.ThreadLocalUtil;
import org.example.myojssm.common.utils.UniqueUsernameUtil;
import org.example.myojssm.entity.User;
import org.example.myojssm.mapper.UserMapper;
import org.example.myojssm.service.FileUploadService;
import org.example.myojssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: liaoyueyue
 * Date: 2024-03-10
 * Time: 17:02
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;

    @Override
    public String login(String account, String password) {
        // 判断用户有效和密码是否正确
        User user = userMapper.findUserByEmailOrUsername(account);
        if (user != null) {
            // 有效用户判断密码
            if (user.getPassword().equals(password)) {
                // 有效密码，登录成功，返回JWT令牌
                Map<String, Object> claims = new HashMap<>();
                claims.put("id", user.getId());
                claims.put("username", user.getUsername());
                String token = JWTUtil.genToken(claims);
                // 把 token 存储到 redis
                ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
                operations.set(user.getUsername(), token, 1, TimeUnit.HOURS);
                return token;
            }
        }
        return null;
    }

    @Override
    public String register(String email, String password, String nickname) {
        String uniqueUsername = UniqueUsernameUtil.getUsername(nickname);
        while (queryUsernameExist(uniqueUsername)) {
            uniqueUsername = UniqueUsernameUtil.getUsername(nickname);
        }
        User user = new User(null, uniqueUsername, password, nickname, email, null, null, null);
        return userMapper.insertUser(user) > 0 ? uniqueUsername : null;
    }

    @Override
    public User queryUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }

    @Override
    public User queryUserById(int id) {
        return userMapper.findUserById(id);
    }

    @Override
    public boolean queryUsernameExist(String username) {
        return userMapper.findUsernameExist(username) > 0;
    }

    @Override
    public boolean queryEmailExist(String email) {
        return userMapper.findEmailExist(email) > 0;
    }

    @Override
    public User updateUserInfo(User user) {
        User existingUser = userMapper.findUserById(user.getId());
        if (existingUser != null) {
            user.setId(getUserId());
            userMapper.updateUser(user);
            return user;
        }
        return null;
    }

    // 允许上传文件(图片)的格式
    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg", ".jpeg", ".png"};

    @Override
    public String updateAvatar(MultipartFile avatarFile) {
        // 1. 保存图片到 OSS
        String newAvatarURL = fileUploadService.uploadImage(avatarFile);
        // 2. 在 OSS 中删除用户旧头像
        User loginUser = getUserInfo();
        String oldUserPicURL = loginUser.getUserPic();
        if (oldUserPicURL != null && !oldUserPicURL.isEmpty()) {
            String oldAvatarFileName = oldUserPicURL.substring(oldUserPicURL.lastIndexOf('/') + 1);
            aliyunOSSUtil.delete("avatar", oldAvatarFileName);
        }
        // 3. 更新数据库用户头像地址, 返回新头像地址
        userMapper.updateAvatar(newAvatarURL, loginUser.getId());
        return newAvatarURL;
    }

    @Override
    public String updatePwd(String oldPwd, String newPwd, String oldToken) {
        User loginUser = getUserInfo();
        if (oldPwd.equals(newPwd)) {
            return "旧密码与新密码相同";
        }
        if (!loginUser.getPassword().equals(oldPwd)) { // 密码没有加密，这里简单判断
            return "更新失败，请检查旧密码";
        }
        if (userMapper.updatePwd(loginUser.getId(), newPwd) > 0) {
            stringRedisTemplate.delete(loginUser.getUsername()); // 删除旧 token
        }
        return null;
    }

    @Override
    public User getUserInfo() {
        return userMapper.findUserById(getUserId());
    }

    @Override
    public Integer getUserId() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        return (Integer) claims.get("id");
    }
}
