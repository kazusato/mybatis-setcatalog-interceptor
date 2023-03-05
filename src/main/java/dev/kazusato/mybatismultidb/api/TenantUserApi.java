package dev.kazusato.mybatismultidb.api;

import dev.kazusato.mybatismultidb.dto.UserDto;
import dev.kazusato.mybatismultidb.interceptor.TenantRequired;
import dev.kazusato.mybatismultidb.mapper.GeneralUserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/{tenantId}/user")
public class TenantUserApi {

    private final GeneralUserMapper generalUserMapper;

    public TenantUserApi(GeneralUserMapper generalUserMapper) {
        this.generalUserMapper = generalUserMapper;
    }

    @GetMapping({"/list", "/listgeneral"})
    @TenantRequired
    public List<UserDto> listUsers(@PathVariable String tenantId) {
        var userList = generalUserMapper.listUsers();

        return userList.stream().map(UserDto::convertFromUserEntity).toList();
    }

}
