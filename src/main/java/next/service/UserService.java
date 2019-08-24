package next.service;

import core.db.DataBase;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;


public class UserService {

    public User createUser(UserCreatedDto createdDto) {
        User user = new User(createdDto.getUserId(), createdDto.getPassword(), createdDto.getName(), createdDto.getEmail());
        DataBase.addUser(user);
        return user;
    }

    public User getUser(String userId) {
        return DataBase.findUserById(userId);
    }

    public User updateUser(String userId, UserUpdatedDto updatedDto) {
        User user = DataBase.findUserById(userId);
        return user.update(new User(user.getUserId(), user.getPassword(), updatedDto.getName(), updatedDto.getEmail()));
    }
}
