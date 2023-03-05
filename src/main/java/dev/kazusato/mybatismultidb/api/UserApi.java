package dev.kazusato.mybatismultidb.api;

import dev.kazusato.mybatismultidb.dto.UserDto;
import dev.kazusato.mybatismultidb.mapper.GeneralUserMapper;
import dev.kazusato.mybatismultidb.mapper.OsakaUserMapper;
import dev.kazusato.mybatismultidb.mapper.TokyoUserMapper;
import dev.kazusato.mybatismultidb.mybatis.CatalogHolder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserApi {

    private final TokyoUserMapper tokyoUserMapper;

    private final OsakaUserMapper osakaUserMapper;

    private final GeneralUserMapper generalUserMapper;

    public UserApi(TokyoUserMapper tokyoUserMapper, OsakaUserMapper osakaUserMapper, GeneralUserMapper generalUserMapper) {
        this.tokyoUserMapper = tokyoUserMapper;
        this.osakaUserMapper = osakaUserMapper;
        this.generalUserMapper = generalUserMapper;
    }

    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> listUsers() {
        var tokyoUserList = tokyoUserMapper.listUsers();
        var osakaUserList = osakaUserMapper.listUsers();

        var userList = new ArrayList<UserDto>();
        userList.addAll(tokyoUserList.stream().map(UserDto::convertFromUserEntity).toList());
        userList.addAll(osakaUserList.stream().map(UserDto::convertFromUserEntity).toList());

        return userList;
    }

    @GetMapping(path = "/listgeneral", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> listUsersGeneral() {
        CatalogHolder.setCatalog("tokyo");
        var tokyoUserList = generalUserMapper.listUsers();
        CatalogHolder.setCatalog("osaka");
        var osakaUserList = generalUserMapper.listUsers();

        var userList = new ArrayList<UserDto>();
        userList.addAll(tokyoUserList.stream().map(UserDto::convertFromUserEntity).toList());
        userList.addAll(osakaUserList.stream().map(UserDto::convertFromUserEntity).toList());

        return userList;
    }

}
