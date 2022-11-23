package com.eticket.application.api.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListUserGetResponse {
    private int size;
    private List<UserGetResponse> userList;
}
