package com.techsophy.tstokens.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Document("cf_counter_process_stage_mapping")
public class CounterProcessMapping {
    @Id
    private String id;
    @NotNull
    private String counterId;
    @NotNull
    private String processStageId;
}
