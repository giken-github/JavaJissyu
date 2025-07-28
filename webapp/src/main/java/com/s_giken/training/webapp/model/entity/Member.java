package com.s_giken.training.webapp.model.entity;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Nullable
    private Long memberId;

    @NotBlank
    private String mail;

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Nullable
    private LocalDate endDate;

    @NotNull
    private Byte paymentMethod;

    @Nullable
    private Timestamp createdDate;

    @Nullable
    private Timestamp updatedDate;

    public String getPaymentMethodString() {
        return switch (paymentMethod) {
            case 1 -> "クレジット決済";
            case 2 -> "銀行振込";
            default -> "(不明)";
        };
    }
}
