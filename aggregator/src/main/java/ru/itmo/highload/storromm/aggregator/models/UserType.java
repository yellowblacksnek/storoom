package ru.itmo.highload.storromm.aggregator.models;
import java.util.Collection;

public enum UserType {
    admin(0), superuser(1), client(2);

    private final Integer hierarchy;

    UserType(final Integer hierarchy) {
        this.hierarchy = hierarchy;
    }

    public Integer getHierarchy() {
        return hierarchy;
    }

    public static UserType getHighestOf(Collection<UserType> types) {
        UserType highest = null;

        for(UserType cur : types) {
            if(highest == null) {
                highest = cur;
                continue;
            }
            if(cur.getHierarchy() < highest.getHierarchy()) {
                highest = cur;
            }
        }
        return highest;
    }
}
