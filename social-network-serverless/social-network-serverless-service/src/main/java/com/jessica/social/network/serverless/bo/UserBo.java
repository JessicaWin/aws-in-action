package com.jessica.social.network.serverless.bo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.jessica.social.network.serverless.item.UserItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBo {
    private String id;
    private String userName;
    private String password;
    private String email;
    private String imageName;

    public UserItem toItem() {
        return UserItem.builder()
                .id(this.id)
                .userName(this.userName)
                .password(this.password)
                .email(this.email)
                .imageName(this.imageName)
                .build();
    }

    public static UserBo fromItem(UserItem userItem) {
        if(userItem == null) {
            return null;
        }
        return UserBo.builder()
                .id(userItem.getId())
                .userName(userItem.getUserName())
                .password(userItem.getPassword())
                .email(userItem.getEmail())
                .imageName(userItem.getImageName())
                .build();
    }
}
