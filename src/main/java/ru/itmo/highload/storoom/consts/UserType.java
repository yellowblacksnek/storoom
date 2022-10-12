package ru.itmo.highload.storoom.consts;

public enum UserType {
    admin(0), superuser(1), client(2);

    private Integer hierarchy;

    UserType(final Integer hierarchy) {
        this.hierarchy = hierarchy;
    }

    public Integer getHierarchy() {
        return hierarchy;
    }
}
