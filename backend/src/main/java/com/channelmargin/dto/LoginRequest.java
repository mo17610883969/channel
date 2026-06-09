package com.channelmargin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "鐢ㄦ埛鍚嶄笉鑳戒负绌?)
    private String username;

    @NotBlank(message = "瀵嗙爜涓嶈兘涓虹┖")
    private String password;
}