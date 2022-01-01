package controller;

import core.Password;

import java.util.ArrayList;

public interface IPasswords {

    public void create(Password password);
    public void read();
    public void update(Password oldPassword, Password newPassword);
    public void delete(Password password);
    
    public ArrayList<Password> getAll();

}
