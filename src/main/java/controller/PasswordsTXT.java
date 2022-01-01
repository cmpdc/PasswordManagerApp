package controller;

import core.Password;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;

public class PasswordsTXT implements IPasswords {

    private final ArrayList<Password> passwordsList = new ArrayList<>();
    private final Path filePath;
    private final File theFile;

    public PasswordsTXT(){
        filePath = Paths.get("src/main/resources/view/passwords.txt");
        theFile = filePath.toFile();
    }

    private void save(ArrayList<Password> passwords){

        // update ALL contents in txt file by using the elements from given list
        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(theFile)))){
            for (Password eachPassword: passwords){

                writer.write(String.valueOf(
                        eachPassword.getTitle() + "*" +
                                eachPassword.getNotes() + "*" +
                                eachPassword.getGeneratedSalt() + "*" +
                                eachPassword.getEncodedPassword() + "*" +
                                eachPassword.getDateAndTimeRaw() + "\n"
                ));
            }
        }
        catch (Exception e){
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    @Override
    public void create(Password password) {
        this.passwordsList.add(password);
        this.save(this.passwordsList);
    }

    @Override
    public void read(){
        try(BufferedReader reader = new BufferedReader(new FileReader(theFile))){
            String line = reader.readLine();

            while(line != null) {
                String[] parts = line.split("\\*");

                String title = parts[0];
                String notes = parts[1];
                String generatedSalt = parts[2];
                String generatedEncodedPassword = parts[3];
                OffsetDateTime currentDateAndTime = OffsetDateTime.parse(parts[4]);

                Password password = new Password(
                        title,
                        notes,
                        generatedSalt,
                        generatedEncodedPassword,
                        currentDateAndTime
                );

                this.passwordsList.add(password);

                line = reader.readLine();
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Password oldPassword, Password newPassword) {

        if(newPassword != null){

            // replace oldPassword with newPassword in passwordList
            // -- the simplest way I can think of to implement this method is by using the old password's index
            int oldPasswordIndex = this.passwordsList.indexOf(oldPassword);
            this.passwordsList.set(oldPasswordIndex, newPassword);

            this.save(this.passwordsList);
        }

        else System.out.println("ERROR: Updating Password.");
    }

    @Override
    public void delete(Password password) {

        if(this.passwordsList.contains(password)){

            this.passwordsList.remove(password);

            // removing a password will update the list
            // therefore, the save function will also update the txt file...

            this.save(this.passwordsList);
        }
        else System.out.println("ERROR: Password not found.");
    }

    @Override
    public ArrayList<Password> getAll() {return this.passwordsList;}
}
