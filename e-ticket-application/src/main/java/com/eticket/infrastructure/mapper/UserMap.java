package com.eticket.infrastructure.mapper;

import com.eticket.application.api.dto.account.UserDetailResponse;
import com.eticket.application.api.dto.account.UserGetResponse;
import com.eticket.domain.entity.account.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMap {
    @Autowired
    private ModelMapper modelMapper;

    public UserGetResponse toUserGetResponse(User user) {
        return modelMapper.map(user, UserGetResponse.class);
    }

    public UserDetailResponse toUserDetailResponse(User user, int bookingNum, int followedNum) {
        UserDetailResponse userDetailResponse = modelMapper.map(user, UserDetailResponse.class);
        userDetailResponse.setAddressString(user.getAddress().toAddressString());
        userDetailResponse.setBookingNum(bookingNum);
        userDetailResponse.setFollowedNum(followedNum);
        return userDetailResponse;
    }
}
