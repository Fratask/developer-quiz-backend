package ru.fratask.service.user;

import ru.fratask.model.entity.Role;
import ru.fratask.model.entity.User;
import ru.fratask.model.entity.UserRole;

public interface UserService {

    /**
     * register new User with  established username and password
     *
     * @param username registration username
     * @param password registration password
     * @param email registration email
     * @return created User
     */
    User register(String username, String password, String email);

    /**
     * Load User by id or username
     * Notice: if User have id and username, they are must comparable
     *
     * @param user User object with id or username, or both
     * @return found user
     */
    User load(User user);

    /**
     * Update user info
     * User id and username cannot be changed
     *
     * @param user new user info
     * @return updated user info
     */
    User update(User user);

    /**
     * Delete user by id or username
     * Notice: if User have id and username, they are must comparable
     *
     * @param user User object with id or username, or both
     * @return deleted user
     */
    User delete(User user);

    /**
     * Setting role for user by  user (id or username or both) and role (id or name or both)
     * Notice: if Role have id and name, they are must comparable
     * Notice: if User have id and username, they are must comparable
     *
     * @param user user with id or username or both
     * @param role role with id or name or both
     * @return userRole
     */
    UserRole setRole(User user, Role role);

    /**
     * Confirm user email registration
     *
     * @param email user email
     * @param code  confirmation code
     */
    void confirm(String email, String code);
}
