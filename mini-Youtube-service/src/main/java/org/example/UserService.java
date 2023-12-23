package org.example;

import com.alibaba.fastjson.JSONObject;
import org.example.auth.UserAuthService;
import org.example.entity.FollowingGroup;
import org.example.entity.User;
import org.example.entity.UserInfo;
import org.example.exception.CustomizedException;
import org.example.mapper.FollowingGroupMapper;
import org.example.mapper.UserInfoMapper;
import org.example.mapper.UserMapper;
import org.example.util.PageResult;
import org.example.util.RSAUtil;
import org.example.util.SHA256Util;
import org.example.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    //TODO
    private final UserMapper userMapper;
    private final UserInfoMapper userInfoMapper;
    private final FollowingGroupMapper followingGroupMapper;
    private final UserAuthService userAuthService;

    //Starting with Spring 4.3, if a class has only one constructor, the @Autowired annotation is no longer required
    @Autowired
    public UserService(UserMapper userMapper, UserInfoMapper userInfoMapper,
                       FollowingGroupMapper followingGroupMapper, UserAuthService userAuthService) {
        this.userMapper = userMapper;
        this.userInfoMapper = userInfoMapper;
        this.followingGroupMapper =followingGroupMapper;
        this.userAuthService = userAuthService;
    }

    @Transactional
    public void addUser(User user) {
        // insert a record in t_user
        String userPhone = user.getPhone();
        if (userPhone == null || userPhone.equals("")) {
            throw new CustomizedException("Phone number can't be null or empty");
        }
        if (userMapper.getUserByPhone(userPhone) != null) {
            throw new CustomizedException("Phone number has already been registered!");
        }
        String userEmail = user.getEmail();
        if (userEmail != null && userMapper.getUserByEmail(userEmail) != null) {
            throw new CustomizedException("Email has already been registered!");
        }
        user.setCreatedTime(LocalDateTime.now());
        user.setSalt(SHA256Util.createSalt());
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(user.getPassword());
        } catch (Exception e) {
            throw new CustomizedException("RSA password decryption failed!");
        }
        String hashedPwd = SHA256Util.generateHashedStr(rawPassword, user.getSalt());
        user.setPassword(hashedPwd);
        userMapper.insertUser(user);
        // insert the corresponding record in t_user_info
        UserInfo userInfo = new UserInfo();
        Long userId = userMapper.getUserByPhone(userPhone).getId();
        userInfo.setUserId(userId); //TODO: to-be-verified
        userInfo.setCreatedTime(LocalDateTime.now());
        userInfoMapper.insertUserInfo(userInfo);
        // add two system following groups
        FollowingGroup specialAttentionGroup = new FollowingGroup();
        specialAttentionGroup.setUserId(userId);
        specialAttentionGroup.setName("special attention");
        specialAttentionGroup.setType("1");
        specialAttentionGroup.setCreatedTime(LocalDateTime.now());
        FollowingGroup defaultFollowingGroup = new FollowingGroup();
        defaultFollowingGroup.setUserId(userId);
        defaultFollowingGroup.setName("default group");
        defaultFollowingGroup.setType("2");
        defaultFollowingGroup.setCreatedTime(LocalDateTime.now());
        followingGroupMapper.addFollowingGroup(specialAttentionGroup);
        followingGroupMapper.addFollowingGroup(defaultFollowingGroup);
        // add default roles
        userAuthService.addUserDefaultRole(userId);
    }

    public String login(User user) throws Exception {
        String phone = user.getPhone();
        String email = user.getEmail();
        if ((phone == null || phone.equals("")) && (email == null || email.equals(""))) {
            throw new CustomizedException("Phone and email are empty or null!");
        }
        User storedUser = userMapper.getUserByPhoneOrEmail(phone, email);
        if (storedUser == null) {
            throw new CustomizedException("User doesn't exist!");
        }
        String decryptedPwd;
        try {
            decryptedPwd = RSAUtil.decrypt(user.getPassword());
        } catch (Exception e) {
            throw new CustomizedException("RSA password decryption failed!");
        }
        if (!SHA256Util.verify(decryptedPwd, storedUser.getPassword(), storedUser.getSalt())) {
            throw new CustomizedException("Incorrect password!");
        }
        return TokenUtil.generateToken(storedUser.getId());
    }

    public User getUserById(Long userId) {
        User user = userMapper.getUserById(userId); // t_user doesn't store userInfo
        user.setUserInfo(userInfoMapper.getUserInfoByUserId(userId));
        return user;
    }

    public void updateUser(User user) {
        User storedUser = userMapper.getUserById(user.getId());
        if (storedUser == null) {
            throw new CustomizedException("User doesn't exist");
        }
        String encryptedPwd = user.getPassword();
        if (!(encryptedPwd == null || encryptedPwd.equals(""))) {
            String rawPwd;
            try {
                rawPwd = RSAUtil.decrypt(encryptedPwd);
            } catch (Exception e) {
                throw new CustomizedException("RSA password decryption failed");
            }
            user.setPassword(SHA256Util.generateHashedStr(rawPwd, storedUser.getSalt()));
        }
        user.setUpdatedTime(LocalDateTime.now());
        userMapper.updateUser(user);
    }

    public void updateUserInfo(UserInfo userInfo) {
        UserInfo storedUserInfo = userInfoMapper.getUserInfoByUserId(userInfo.getUserId());
        if (storedUserInfo == null) {
            throw new CustomizedException("UserInfo doesn't exist");
        }
        userInfo.setUpdatedTime(LocalDateTime.now());
        userInfoMapper.updateUserInfo(userInfo);
    }

    public PageResult<UserInfo> pageListUserInfo(JSONObject params) {
        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");
        params.put("start", (pageNum - 1) * pageSize);
        List<UserInfo> userInfoList = userInfoMapper.pageListUserInfo(params);
        Integer total = userInfoList.size();
        return new PageResult<>(total, userInfoList);
    }
}
