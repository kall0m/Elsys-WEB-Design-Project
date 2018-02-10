package healthblog.services;

import healthblog.models.User;

import java.util.List;

public interface UserService {
    boolean authenticate(String username, String password);

    User findByEmail(String email);

    //User findByConfirmationToken(String confirmationToken);

    //User findByForgotPasswordToken(String confirmationToken);

    List<User> getAllUsers();

    void saveUser(User user);
}
