package core;

public class PasswordHolder {
    private Password password;

    private final static PasswordHolder INSTANCE = new PasswordHolder();

    private PasswordHolder(){}

    public static PasswordHolder getInstance(){return INSTANCE;}

    public void setPassword(Password password){this.password = password;}
    public Password getPassword(){return this.password;}

}
