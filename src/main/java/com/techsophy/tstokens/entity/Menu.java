package com.techsophy.tstokens.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Document("ms_menus")
public class Menu {
    @Id
    private String id;
    @NotNull
    @Size(max = 10)
    private String code;
    @NotNull
    @Size(max = 100)
    private String name;
    @NotNull
    @Size(max = 100)
    private String displayName;
    @NotNull
    @Size(max = 10)
    private String parentMenuCode;
    @Size(max = 255)
    private String uiPageUrl;
    private Integer displayOrder;
    @Size(max = 255)
    private String icon;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createdOn;
    @Size(max = 10)
    private String status;
    @DocumentReference(lazy = true, lookup = "{ 'parentMenuCode' : ?#{#self.code} }")
    List<Menu> childMenu;
}
