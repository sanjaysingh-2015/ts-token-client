package com.techsophy.tstokens.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MasterLookupPayload {
    private List<MasterLookupItemPayload> organizations;
    private List<MasterLookupItemPayload> departments;
    private List<MasterLookupItemPayload> categories;
    private List<MasterLookupItemPayload> tokenTypes;
    private List<MasterLookupItemPayload> counters;
    private List<MasterLookupItemPayload> processStages;
    private List<MasterLookupItemPayload> devices;
}
