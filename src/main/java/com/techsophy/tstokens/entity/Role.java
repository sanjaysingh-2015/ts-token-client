package com.techsophy.tstokens.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Document("ms_roles")
public class Role {
    @Id
    private String id;

    private EnumRole name;
}
