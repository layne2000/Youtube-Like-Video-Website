package org.example;

import org.example.entity.User;
import org.example.entity.UserInfo;
import org.example.exception.CustomizedException;
import org.example.mapper.UserInfoMapper;
import org.example.mapper.UserMapper;
import org.example.util.RSAUtil;
import org.example.util.SHA256Util;
import org.example.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserService {
    //TODO
    private final UserMapper userMapper;
    private final UserInfoMapper userInfoMapper;

    //Starting with Spring 4.3, if a class has only one constructor, the @Autowired annotation is no longer required
    @Autowired
    public UserService(UserMapper userMapper, UserInfoMapper userInfoMapper) {
        this.userMapper = userMapper;
        this.userInfoMapper = userInfoMapper;
    }

    //TODO: add @Transactional
    public void addUser(User user) {
        // insert a record in t_user
        String userPhone = user.getPhone();
        if(userPhone == null || userPhone.equals("")){
            throw new CustomizedException("Phone number can't be null or empty");
        }
        if(userMapper.getUserByPhone(userPhone) != null){
            throw new CustomizedException("Phone number has already been registered!");
        }
        String userEmail = user.getEmail();
        if(userEmail != null && userMapper.getUserByEmail(userEmail) != null){
            throw new CustomizedException("Email has already been registered!");
        }
        user.setCreatedTime(LocalDateTime.now());
        user.setSalt(SHA256Util.createSalt());
        String rawPassword;
        try{
            rawPassword = RSAUtil.decrypt(user.getPassword());
        }catch(Exception e){
            throw new CustomizedException("RSA password decryption failed!");
        }
        String hashedPwd = SHA256Util.generateHashedStr(rawPassword, user.getSalt());
        user.setPassword(hashedPwd);
        userMapper.insertUser(user);
        // insert the corresponding record in t_user_info
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userMapper.getUserByPhone(userPhone).getId()); //
        userInfo.setCreatedTime(LocalDateTime.now());
        userInfoMapper.insertUserInfo(userInfo);
    }

    public String login(User user) throws Exception {
        String phone = user.getPhone();
        String email = user.getEmail();
        if((phone == null || phone.equals("")) && (email == null || email.equals(""))){
            throw new CustomizedException("Phone and email are empty or null!");
        }
        User storedUser = userMapper.getUserByPhoneOrEmail(phone, email);
        if(storedUser == null){
            throw new CustomizedException("User doesn't exist!");
        }
        String decryptedPwd;
        try{
            decryptedPwd = RSAUtil.decrypt(user.getPassword());
        }catch(Exception e){
            throw new CustomizedException("RSA password decryption failed!");
        }
        if(!SHA256Util.verify(decryptedPwd, storedUser.getPassword(),storedUser.getSalt())){
            throw new CustomizedException("Incorrect password!");
        }
        return TokenUtil.generateToken(storedUser.getId());
    }

    public User getUserById(Long userId){
        User user = userMapper.getUserById(userId); // t_user doesn't store userInfo
        user.setUserInfo(userInfoMapper.getUserInfoByUserId(userId));
        return user;
    }

    public void updateUser(User user){
        //TODO

    }

    public void updateUserInfo(UserInfo userInfo){
        //TODO

    }
}
