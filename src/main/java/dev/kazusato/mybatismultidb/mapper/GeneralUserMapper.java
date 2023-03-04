package dev.kazusato.mybatismultidb.mapper;

import dev.kazusato.mybatismultidb.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GeneralUserMapper {

    @Select("select * from users order by user_id")
    List<UserEntity> listUsers();

}
