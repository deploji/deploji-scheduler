package com.deploji.scheduler.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Job {
    @Id
    private String id;
    private String Type;
    private String StartedAt;
    private String FinishedAt;
    private String Application;
    private String ApplicationID;
    private String Playbook;
    private String Project;
    private String ProjectID;
    private String Inventory;
    private String InventoryID;
    private String Template;
    private String TemplateID;
    private String Key;
    private String KeyID;
    private String User;
    private String UserID;
    private String Status;
    private String Version;
    private String ExtraVariables;
}
