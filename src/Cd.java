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
public class Cd implements Command {

    public static Cd instance = null;
    private String dirPath;

    private Cd() {

    }

    public static Cd getInstance() {
        if (instance == null) {
            instance = new Cd();
        }
        return instance;
    }

    public String getdirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    @Override
    public void execute() {

        /* if directory is root */
        if (dirPath.equalsIgnoreCase("/")) {
            Main.currDirectory = Main.root;
        } else {
            /* if drectory is not root */
            String[] tokens = dirPath.split("/");

            /* if path is absolute */
            if (tokens[0].equalsIgnoreCase("")) {
                actualizeDir(Main.root);
            } else {
                /* if path is not absolute */
                actualizeDir(Main.currDirectory);
            }
        }
    }

    public void actualizeDir(Directory directory) {

        String[] tokens = dirPath.split("/");   // store directories names into array
        Directory directory1 = directory;       // save root directory
        boolean exist1 = true;      // contor to see if path exists
        int i = 0;

        /*if the path is absolute, start with the second element */
        if (tokens[0].equalsIgnoreCase("")) {
            i = 1;
        }
        /* check if path exists */
        for (; i < tokens.length - 1; i++) {
            boolean exist2 = false;     //contor to see if each directory is found

            /* if directory is . */
            if (tokens[i].equalsIgnoreCase(".")) {
                directory1 = (Directory) directory1.getList().get(0);
                exist2 = true;
            } else if (tokens[i].equalsIgnoreCase("..")) {
                /* if directory is .. */
                if (!directory1.getName().equalsIgnoreCase("/")) {
                    directory1 = (Directory) directory1.getList().get(1);
                    exist2 = true;
                }
            } else {
                /* if directory is something else */
                for (FileSystem dir : directory1.getList()) {
                    if (tokens[i].equals(dir.getName())
                            && !dir.equals(directory1.getList().get(0))
                            && !dir.equals(directory1.getList().get(1))
                            && dir.is().equals("Directory")) {
                        directory1 = (Directory) dir;
                        exist2 = true;      //directory is found 
                        break;
                    }
                }

                /* if one of the directiries is not found, directory cannot be created */
                if (!exist2) {
                    exist1 = false;
                    break;
                }
            }
        }

        /* if cd cannot follow the path, print error message */
        if (!exist1) {
            try {
                Main.writeError.write("cd: " + dirPath + ": No such directory");
                Main.writeError.write("\n");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            /* if directory can be found */
            boolean exist3 = false;     //contor to see if directory exists

            /* if directory is . */
            if (tokens[tokens.length - 1].equalsIgnoreCase(".")) {
                Main.currDirectory = (Directory) directory1.getList().get(0);
                exist3 = true;
            } else if (tokens[tokens.length - 1].equalsIgnoreCase("..")) {
                /* if directory is .. */
                if (!directory1.getName().equalsIgnoreCase("/")) {
                    Main.currDirectory = (Directory) directory1.getList().get(1);
                    exist3 = true;
                }
            } else {
                /* if directory is something else */
                for (FileSystem dir : directory1.getList()) {
                    if (tokens[tokens.length - 1].equals(dir.getName())
                            && !dir.equals(directory1.getList().get(0))
                            && !dir.equals(directory1.getList().get(1))
                            && dir.is().equals("Directory")) {
                        exist3 = true;      // directory exists
                        Main.currDirectory = (Directory) dir;   // actualize current directory
                        break;
                    }
                }
            }
            /* if directory doesn't exist, print error message*/
            if (!exist3) {
                try {
                    Main.writeError.write("cd: " + dirPath + ": No such directory");
                    Main.writeError.write("\n");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
