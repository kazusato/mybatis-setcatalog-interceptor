package dev.kazusato.mybatismultidb.dto;

import dev.kazusato.mybatismultidb.entity.UserEntity;

public record UserDto(
        long userId,
        String userName
) {

    public static UserDto convertFromUserEntity(UserEntity userEntity) {
        return new UserDto(userEntity.userId(), userEntity.userName());
    }

}
