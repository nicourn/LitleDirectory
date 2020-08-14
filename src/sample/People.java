package sample;


public class People {
    private String firstname;
    private String lastname;
    private int score;
    private int numSchool;

    People(String firstname, String lastname,
           int scores, int numSchool){
        this.firstname = firstname;
        this.lastname = lastname;
        this.score = scores;
        this.numSchool = numSchool;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public int getNumSchool() {
        return numSchool;
    }

    public int getScore() {
        return score;
    }
}
