package com.epam.study.snet.entity;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.time.LocalDateTime;

@Value
@Builder
public class Message {
    long id;
    User sender;
    User receiver;
    String body;
    LocalDateTime sendingTime;
    boolean unread;
}
