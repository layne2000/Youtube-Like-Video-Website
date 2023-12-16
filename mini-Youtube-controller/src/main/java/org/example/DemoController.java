package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class DemoController {
    @Autowired
    private UserService userService;


    @DeleteMapping("/demos")
    public void delete(String userId) throws SQLException {
        userService.deleteUserById(userId);
    }


}
