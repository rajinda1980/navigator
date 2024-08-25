package org.phinxt.navigator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorDetail {
    private final String field;
    private final String issue;
}
