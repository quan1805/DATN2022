package huce.it.datnbackend.common;

public enum AccountRole {
    ROLE_ADMIN(0),
    ROLE_MANAGER(1),
    ROLE_CUSTOMER(2);

    public final int n;
    AccountRole(int n){
        this.n = n;
    }

    public int getValue(){
        return this.n;
    }

    public static AccountRole getAccountRole(int n){
        switch (n){
            case 0:
                return ROLE_ADMIN;
            case 1:
                return ROLE_MANAGER;
            case 2:
                return ROLE_CUSTOMER;
        }
        return ROLE_CUSTOMER;
    }
}
