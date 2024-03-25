package com.example.springSecurity.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    private int mid;
    private String name;
    private LocalDate regDate;
    private String email;

}
