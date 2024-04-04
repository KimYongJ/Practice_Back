package com.practice_back.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    private long    cntCartItems;
    private boolean master;
    private String  email;
    private String  password;
}
