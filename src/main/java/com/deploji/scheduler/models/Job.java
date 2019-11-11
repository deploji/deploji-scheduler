package com.deploji.scheduler.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Job {
    @JsonProperty("ApplicationID")
    private Long applicationID;

    @JsonProperty("Playbook")
    private String playbook;

    @JsonProperty("InventoryID")
    private Long inventoryID;

    @JsonProperty("TemplateID")
    private Long templateID;

    @JsonProperty("KeyID")
    private Long keyID;

    @JsonProperty("UserID")
    private Long userID;

    @JsonProperty("Version")
    private String version;

    @JsonProperty("ExtraVariables")
    private String extraVariables;
}
