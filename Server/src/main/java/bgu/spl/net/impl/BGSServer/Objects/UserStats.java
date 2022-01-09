package bgu.spl.net.impl.BGSServer.Objects;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class UserStats {

    private short age;
    private short posts;
    private short followers;
    private short following;


    public UserStats(User user){
        try {
            age = calculateAge(user.getBirthday());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        posts = user.getNumberOfPosts();
        followers = user.getNumberOfFollowers();
        following = user.getNumberOfFollowings();
    }

    private short calculateAge(String birthday) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date d = sdf.parse(birthday);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);

        LocalDate l1 = LocalDate.of(year, month, date);
        LocalDate now1 = LocalDate.now();
        return (short) Period.between(l1, now1).getYears();
    }

    public short getAge() {
        return age;
    }

    public short getPosts() {
        return posts;
    }

    public short getFollowers() {
        return followers;
    }

    public short getFollowing() {
        return following;
    }
}
