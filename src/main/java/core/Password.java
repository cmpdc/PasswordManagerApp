package core;

import core.utils.Utils;

import java.time.OffsetDateTime;

public class Password {

    private String title;
    private String notes;
    private String generatedSalt;
    private String encodedPassword;
    private OffsetDateTime dateAndTime;

    public Password(){}

    public Password(String title, String notes, String generatedSalt, String generatedEncodedPassword, OffsetDateTime currentDateAndTime){
        this.setTitle(title);
        this.setNotes(notes);
        this.setGeneratedSalt(generatedSalt);
        this.setEncodedPassword(generatedEncodedPassword);
        this.setDateAndTime(currentDateAndTime);
    }

    public String getTitle() {return title;}
    public String getNotes() {return notes;}
    public String getGeneratedSalt() {return generatedSalt;}
    public String getEncodedPassword() {return encodedPassword;}
    public OffsetDateTime getDateAndTimeRaw() {return dateAndTime;}

    public void setTitle(String title) {
        this.title = title;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public void setGeneratedSalt(String generatedSalt) {
        this.generatedSalt = generatedSalt;
    }
    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }
    public void setDateAndTime(OffsetDateTime dateAndTime) {this.dateAndTime = dateAndTime;}

    @Override
    public String toString(){
        return title + "\n" + Utils.getFormattedDateTime(dateAndTime);
    }
}
