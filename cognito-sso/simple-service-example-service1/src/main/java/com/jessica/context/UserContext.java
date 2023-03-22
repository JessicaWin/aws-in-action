package com.jessica.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserContext {

    private static final InheritableThreadLocal<UserContext> instanceHolder = new InheritableThreadLocal<>();

    public static void reset(@NonNull UserContext value) {
        instanceHolder.set(value);
    }

    public static UserContext getInstance() {
        return instanceHolder.get();
    }

    private String userId;

}
