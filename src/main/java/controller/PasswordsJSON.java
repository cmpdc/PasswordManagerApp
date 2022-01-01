package controller;

import core.Password;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;

public class PasswordsJSON implements IPasswords {

    private final ArrayList<Password> passwordsList = new ArrayList<>();
    private final Path filePath;
    private final File theFile;

    public PasswordsJSON(){
        filePath = Paths.get("src/main/resources/view/passwords.json");
        theFile = filePath.toFile();
    }

    private void save(ArrayList<Password> passwords){
        // update ALL contents in txt file by using the elements from given list
        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(theFile)))){

            JSONArray jsonArray = new JSONArray();

            for (Password eachPassword: passwords){

                JSONObject passwordContentData = new JSONObject();
                passwordContentData.put("Title", eachPassword.getTitle());
                passwordContentData.put("Notes", eachPassword.getNotes());
                passwordContentData.put("Generated Salt", eachPassword.getGeneratedSalt());
                passwordContentData.put("Encoded Password", eachPassword.getEncodedPassword());
                passwordContentData.put("DateAndTime", String.valueOf(eachPassword.getDateAndTimeRaw()));

                jsonArray.put(passwordContentData.toMap());
            }

            writer.write(String.valueOf(jsonArray.toString(4)));
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
    public void read() {

        try{
            String text = new String(Files.readAllBytes(filePath));

            // verify if file is empty to prevent the annoying error...
            if(!text.equalsIgnoreCase("")){
                JSONArray jsonPasswords = new JSONArray(text);

                for(int i = 0; i < jsonPasswords.length(); i++){
                    Password password = new Password(
                            jsonPasswords.getJSONObject(i).getString("Title"),
                            jsonPasswords.getJSONObject(i).getString("Notes"),
                            jsonPasswords.getJSONObject(i).getString("Generated Salt"),
                            jsonPasswords.getJSONObject(i).getString("Encoded Password"),
                            OffsetDateTime.parse(jsonPasswords.getJSONObject(i).getString("DateAndTime"))
                    );

                    this.passwordsList.add(password);
                }
            }
        }

        catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
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
