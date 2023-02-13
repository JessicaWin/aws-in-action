package com.jessica.social.network.serverless.vo;

import com.jessica.social.network.serverless.bo.UserBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "UserVo", description = "data model for user")
public class UserVo {
    @ApiModelProperty(required = false, notes = "do not need to set when post request, will only be used when return data")
    @Nullable
    private String id;
    @ApiModelProperty(required = true, allowEmptyValue = false, notes = "length should between 6 and 30")
    @NotBlank
    @Size(min= 6, max = 30)
    private String userName;
    @ApiModelProperty(required = true, allowEmptyValue = false, notes = "length should between 6 and 30")
    @NotBlank
    @Size(min= 6, max = 30)
    private String password;
    @ApiModelProperty(required = false)
    private String email;
    @ApiModelProperty(required = false)
    private String imageName;

    public UserBo toBo() {
        return  UserBo.builder()
                .id(UUID.randomUUID().toString())
                .userName(this.userName)
                .password(this.password)
                .email(this.email)
                .imageName(this.imageName)
                .build();
    }
    
    public static UserVo fromBo(UserBo userBo) {
        if(userBo == null) {
            return null;
        }
        return UserVo.builder()
                .id(userBo.getId())
                .userName(userBo.getUserName())
                .password(userBo.getPassword())
                .email(userBo.getEmail())
                .imageName(userBo.getImageName())
                .build();
    }
}
