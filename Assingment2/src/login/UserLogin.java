package login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class UserLogin extends HashMap <String, String>{

    public UserLogin(){}

    public UserLogin(String username, String password)
    {
        this.put(username, LoginController.encryptPassword(password));
    }

    public boolean checkName(String username)
    {
        return this.containsKey(username);
    }

    public boolean checkPassword(String username,String password)
    {
        return this.get(username).equals(LoginController.encryptPassword(password));
    }



}
