package Finall_Project_2.Server.DataBase;

import Finall_Project_2.Server.Game.GameRecord;
import Finall_Project_2.Server.User.User;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
//azash ye instance darim
public class DataBase {
    private final static DataBase instance = new DataBase();
    public List<GameRecord> games;
    public List<User> AllUsers;

    private DataBase(){
        games=new CopyOnWriteArrayList<>();
        AllUsers=new CopyOnWriteArrayList<>();
    }

    public static DataBase getInstance() {
        return instance;
    }

    //bara game ham tarif kon
    public void saveUsers(){//aval tedade hame user ha ro minevise dar file va bad etelaate una rp
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(Paths.get("src/Server/DataBase/Users.ser")))){
            objectOutputStream.writeInt(AllUsers.size());
            for (User allUser: AllUsers) {
                objectOutputStream.writeObject(allUser);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//bara game ham hamintor
    public void ReadUsers(){
        try (ObjectInputStream objectInputStream=new ObjectInputStream(Files.newInputStream(Paths.get("src/Server/DataBase/Users.ser")))){
            int size = objectInputStream.readInt();//avale file ye int dare k tedade karbara ro malum mikone
            AllUsers.clear();
            for (int i=0 ; i<size ; i++){
                AllUsers.add((User)objectInputStream.readObject());//hala chon hamegi az jense User hastan , az un file object khundim b tedade user ha , update shodashono rikhtim az aval tu allusers bad az pak kardane allusers dar bala
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> listOfUsersWithThisSubName(String subname){
        ArrayList<User> users = new ArrayList<>();
        for (User i : AllUsers) {
            if (i.name.startsWith(subname))
                users.add(i);
        }
        return users;
    }

    public User getUser(String name){
        for (User i : AllUsers) {
            if (i.name.equals(name))
                return i;
        }
        return null;
    }

    public User getUser(User user){
        if (AllUsers.contains(user)){
            for (User i:AllUsers) {
                if (i.equals(user))
                    return i;
            }
        }
        return null;
    }


    public String addUser(User user){
        for (User user1: AllUsers) {
            if (user1.name.equals(user.name))
                return "this username is already existed";
        }

        char[] password=user.password.toCharArray();
        if (password.length<8)
            return "invalid format of password . please enter just digits and more than 7 digits";
        int unicode;
        for (char i : password) {
            unicode=(int)i;
            if (unicode<48||unicode>57)
                return "invalid format of password . please enter just digits and more than 7 digits";
        }

        AllUsers.add(user);
        return "added";
    }
    public void deleteUser(User user){
        AllUsers.remove(user);
    }

    public void addGame(GameRecord gameRecord){
        this.games.add(gameRecord);
    }











}
