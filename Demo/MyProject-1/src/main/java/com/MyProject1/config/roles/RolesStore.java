package com.MyProject1.config.roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface RolesStore {
    public final static String admin = "ADMIN";
    public final static String manager = "MANAGER";
    public final static String user = "USER";
    public final static String student = "STUDENT";

    public final static List<String> Roles = Arrays.asList(admin, manager, user);
    public final static List<String> Roles_amdin = Arrays.asList(admin, manager, user);
    public final static List<String> Roles_manager = Arrays.asList(manager, user);
    public final static List<String> Roles_user = Arrays.asList(user, student);

}
