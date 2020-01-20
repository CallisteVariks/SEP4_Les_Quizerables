package debugit.lesquizerables;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String UID, name, location, sex, rank, title, email;
    private int points, numberOfGames;


    public User() {

    }

    public User(String name, String location, String sex, String email) {
        this.name = name;
        this.location = location;
        this.sex = sex;
        this.email = email;
        this.rank = null;
        this.title = null;
        this.points = 0;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }

    public void setNumberOfGames(int numberOfGames) {
        this.numberOfGames = numberOfGames;
    }

    public String toString() {
        return "ActivityModel{" +
                "name='" + name + '\'' +
                ",sex='" + sex + '\'' +
                ", location='" + location + '\'' +
                ", email='" + email + '\'' +
                ", rank='" + rank + '\'' +
                ", points=" + points + '\'' +
                ", numberOfGames=" + numberOfGames +
                '}';
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("sex", sex);
        result.put("location", location);
        result.put("email", email);
        result.put("rank", rank);
        result.put("points", points);
        result.put("numberOfGames", numberOfGames);
        return result;
    }

}
