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
public class Touch implements Command {

    public static Touch instance = null;
    private String filePath;

    private Touch() {

    }

    public static Touch getInstance() {
        if (instance == null) {
            instance = new Touch();
        }
        return instance;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void execute() {
        String[] tokens = filePath.split("/");   //store directories names into array

        /* if path is absolute */
        if (tokens[0].equalsIgnoreCase("")) {
            createPathFile(Main.root);
        } else {
            /* if the path is not absolute */
            createPathFile(Main.currDirectory);
        }
    }

    public void createPathFile(Directory directory) {

        String[] tokens = filePath.split("/");  //store directories names into array
        Directory directory1 = directory;       //save root directory
        boolean exist1 = true;      //contor to see if parent path exists
        int i = 0;

        /*if the path is absolute, start with the second element */
        if (tokens[0].equalsIgnoreCase("")) {
            i = 1;
        }

        /* check if parent directory exists */
        for (; i < tokens.length - 1; i++) {
            boolean exist2 = false;     //contor to see if each directory from path is found 

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
            }

            /* if one of the directories is not found, file cannot be created */
            if (!exist2) {
                exist1 = false;
                break;
            }
        }

        /* if file cannot be created, print error message */
        if (!exist1) {
            String path = filePath.substring(0, filePath.lastIndexOf("/"));
            try {
                Main.writeError.write("touch: " + path + ": No such directory");
                Main.writeError.write("\n");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            /* if file can be created */
            boolean exist3 = false;     //contor to see if file exists
            for (FileSystem dir : directory1.getList()) {
                if (tokens[tokens.length - 1].equals(dir.getName())
                        && !dir.equals(directory1.getList().get(0))
                        && !dir.equals(directory1.getList().get(1))) {
                    exist3 = true;      //if exists, print error message
                    String path = makePath((Directory) directory1);
                    if (path.equals("/")) {
                        path = "/" + tokens[tokens.length - 1];
                    } else {
                        path = path + "/" + tokens[tokens.length - 1];
                    }
                    try {
                        Main.writeError.write("touch: cannot create file " + path + ": Node exists");
                        Main.writeError.write("\n");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
            }

            /* if directory doesn't exist, create it */
            if (!exist3) {
                File file = new File(tokens[tokens.length - 1]);
                file.setParentDir(directory1);
                directory1.add(file);
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
