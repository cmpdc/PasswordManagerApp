package core;

import core.utils.Constants;

public class PasswordChecker {
    private final String PASSWORD;

    private double passStrengthScore;

    public PasswordChecker(String passVal){
        this.PASSWORD = passVal;
        this.getStrength();
    }

    private void getStrength(){
        boolean usedUpper = false;
        boolean usedLower = false;
        boolean usedNums = false;
        boolean usedPuncs = false;

        for(int i = 0; i < this.PASSWORD.length(); i++){
            char eachChar = this.PASSWORD.charAt(i);
            int evaluatedChar = evaluateChars(eachChar);

            switch (evaluatedChar) {
                case 1 -> usedUpper = true;
                case 2 -> usedLower = true;
                case 3 -> usedNums = true;
                case 4 -> usedPuncs = true;
                default -> {}
            }
        }

        // add to score if each password validation is true
        if (usedUpper) this.passStrengthScore += 1;
        if (usedLower) this.passStrengthScore += 1;
        if (usedNums) this.passStrengthScore += 1;
        if (usedPuncs) this.passStrengthScore += 1;

        // add score for password lengths too
        if (this.PASSWORD.length() >= Constants.PASSWORD_MINIMUM_LENGTH) this.passStrengthScore += 2;
        if (this.PASSWORD.length() >= 20) this.passStrengthScore += 1;
        if (this.PASSWORD.length() >= Constants.PASSWORD_MAXIMUM_LENGTH) this.passStrengthScore += 3;
    }

    private int evaluateChars(char character){
        int charCode = 0;

        // upper cases (A - Z)
        if ((int) character >= 65 && (int) character <= 90) charCode = 1;

            // lower cases (a - z)
        else if ((int) character >= 97 && (int) character <= 122) charCode = 2;

            // digit (0 - 9)
        else if ((int) character >= 60 && (int) character <= 71) charCode = 3;

            // symbols
        else charCode = 4;

        return charCode;
    }

    public double getPasswordStrengthScore(){
        return passStrengthScore / 10;
    }

    public String getPasswordStrengthScoreAsString(){
        String evaluation = "";

        if (this.passStrengthScore >= 6){
            evaluation += "Very Good";
        }

        else if (this.passStrengthScore >= 5){
            evaluation += "Good";
        }

        else if (this.passStrengthScore >= 3){
            evaluation += "Medium";
        }

        else evaluation += "Weak";


        return evaluation;
    }

    @Override
    public String toString(){
        String evaluation = "", evaluationMessage = "";

        if (this.passStrengthScore >= 6){
            evaluation += "very good";
            evaluationMessage += "";
        }
        else if (this.passStrengthScore >= 5){
            evaluation += "good";
            evaluationMessage += "But it could be better!";
        }
        else if (this.passStrengthScore >= 3){
            evaluation += "medium";
            evaluationMessage += "Make it better?";
        }

        else{
            evaluation += "weak";
            evaluationMessage += "It is better to make a new password!";
        }

        return "" + evaluation.toUpperCase() +
                ". " + evaluationMessage;
    }
}
