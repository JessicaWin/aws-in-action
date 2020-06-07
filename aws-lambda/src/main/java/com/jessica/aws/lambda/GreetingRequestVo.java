package com.jessica.aws.lambda;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GreetingRequestVo {
    private String name;
    private String time;
    private String city;
    private String day;
}
