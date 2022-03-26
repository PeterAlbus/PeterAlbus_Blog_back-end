package com.peteralbus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type User.
 * @author PeterAlbus
 * Created on 2022/3/26.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements Serializable
{
    @TableId(type= IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Long userId;
    String userUsername;
    String userPassword;
    String userPhone;
    String userMail;
    Integer userIdentity;
    String userAvatar;
    String userSalt;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    LocalDateTime gmtCreate;
}
