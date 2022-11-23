package com.eticket.infrastructure.mapper;

import com.eticket.application.api.dto.account.EmployeeDetailResponse;
import com.eticket.application.api.dto.account.EmployeeGetResponse;
import com.eticket.domain.entity.account.Employee;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMap {
    @Autowired
    private ModelMapper modelMapper;

    public EmployeeGetResponse toEmployeeGetResponse(Employee employee) {
        return modelMapper.map(employee, EmployeeGetResponse.class);
    }

    public EmployeeDetailResponse toEmployeeDetailResponse(Employee employee) {
        EmployeeDetailResponse employeeDetailResponse = modelMapper.map(employee, EmployeeDetailResponse.class);
        if (employee.getAddress() != null) {
            employeeDetailResponse.setAddressString(employee.getAddress().toAddressString());
        }
        return employeeDetailResponse;
    }
}
