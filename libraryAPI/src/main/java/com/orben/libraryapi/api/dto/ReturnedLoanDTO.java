package com.orben.libraryapi.api.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ReturnedLoanDTO {
    private boolean returned;
}
