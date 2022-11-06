package com.eticket.infrastructure.kafka.model;

import com.eticket.infrastructure.kafka.enumeration.MailMessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailMessage {
    private MailMessageType type;
    private String email;
    private String informationField;
}
