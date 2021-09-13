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
public class Mkdir implements Command {

    public static Mkdir instance = null;
    private String dirPath;

    private Mkdir() {
    }

    public static Mkdir getInstance() {
        if (instance == null) {
            instance = new Mkdir();
        }
        return instance;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    @Override
    public void execute() {

        String[] tokens = dirPath.split("/");   //store directories names into array

        /* if path is absolute */
        if (tokens[0].equalsIgnoreCase("")) {
            createPathDirectory(Main.root);
        } else {
            /* if the path is not absolute */
            createPathDirectory(Main.currDirectory);
        }
    }

    public void createPathDirectory(Directory directory) {

        String[] tokens = dirPath.split("/");   //store directories names into array
        Directory directory1 = directory;       //save root directory
        boolean exist1 = true;      //contor to see if parent path exists
        int i = 0;

        /*if the path is absolute, start with the second element */
        if (tokens[0].equalsIgnoreCase("")) {
            i = 1;
        }

        /* check if parent directory exists */
        for (; i < tokens.length - 1; i++) {
            boolean exist2 = false; //contor to see if each directory is found

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
                            && dir.is().equalsIgnoreCase("Directory")) {
                        directory1 = (Directory) dir;
                        exist2 = true;      //directory is found 
                        break;
                    }
                }
            }

            /* if one of the directories is not found, directory cannot be created */
            if (!exist2) {
                exist1 = false;
                break;
            }
        }

        /* if directory cannot be created, print error message */
        if (!exist1) {
            String path = dirPath.substring(0, dirPath.lastIndexOf("/"));
            try {
                Main.writeError.write("mkdir: " + path + ": No such directory");
                Main.writeError.write("\n");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            /* if directory can be created */
            boolean exist3 = false;     //contor to see if directory exists
            for (FileSystem dir : directory1.getList()) {
                if (tokens[tokens.length - 1].equals(dir.getName())
                        && !dir.equals(directory1.getList().get(0))
                        && !dir.equals(directory1.getList().get(1))) {
                    exist3 = true;      //if exists, print error message
                    String path = makePath(directory1);
                    if (path.equals("/")) {
                        path = "/" + tokens[tokens.length - 1];
                    } else {
                        path = path + "/" + tokens[tokens.length - 1];
                    }
                    try {
                        Main.writeError.write("mkdir: cannot create directory " + path + ": Node exists");
                        Main.writeError.write("\n");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
            }

            /* if directory doesn't exist, create it */
            if (!exist3) {
                Directory directory2 = new Directory(tokens[tokens.length - 1]);
                directory2.add(directory2);     // . directory
                directory2.add(directory1);     // .. directory
                directory1.add(directory2);     // create directory
            }
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
