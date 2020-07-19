package Finall_Project_2.Server.User;

import Finall_Project_2.Server.Game.GameRecord;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//user ha ro ham mikhaim dar data base zakhire konim pas inam az serializable ers mibare
public class User implements Serializable , Comparable{
    public Socket socket;
    public String name;
    public String password;
    public int score;
    List<GameRecord> gameRecordsList = new ArrayList<>();

    public User(String name , String password , Socket socket){
        this.name=name;
        this.password=password;
        this.socket=socket;
    }

    public  String setNewPass(String pass){
        char[] password=pass.toCharArray();
        if (password.length<8)
            return "invalid format of password . please enter just digits and more than 7 digits";
        int unicode;
        for (char i : password) {
            unicode=(int)i;
            if (unicode<48||unicode>57)
                return "invalid format of password . please enter just digits and more than 7 digits";
        }
        return null;
    }

    @Override
    public String toString() {
        return "name='" + name +" "+ score +" "+"'\\n";
    }

    @Override
    public boolean equals(Object obj) {
        User user =(User)obj;
        if (this.name.equals(user.name)&&this.password.equals(user.password))
            return true;
        return false;
    }

    @Override
    public int compareTo(Object O) {
        User o =(User)O;
        if (this.score> o.score)
            return 1;
        if (this.score< o.score)
            return -1;
        return 0;
    }

    // ye chan ta tabe k be game record list game ezafe kone ,...
    //inja kheili ba server kar nadarim
}
