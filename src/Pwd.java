/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;

/**
 *
 * @author nick
 */
public class Pwd implements Command {

    public static Pwd instance = null;

    private Pwd() {

    }

    public static Pwd getInstance() {
        if (instance == null) {
            instance = new Pwd();
        }
        return instance;
    }

    @Override
    public void execute() {
        String path = makePath(Main.currDirectory);
        try {
            Main.writeOutput.write(path);
            Main.writeOutput.write("\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String makePath(Directory directory) {
        String path = "";

        while (!directory.getName().equalsIgnoreCase("/")) {
            path = "/" + directory.getName() + path;
            directory = (Directory) directory.getList().get(1);
        }

        if (path.equalsIgnoreCase("")) {
            return "/";
        } else {
            return path;
        }
    }

}
