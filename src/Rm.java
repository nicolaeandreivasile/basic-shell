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
public class Rm implements Command {

    public static Rm instance = null;
    private String filePath;

    private Rm() {

    }

    public static Rm getInstance() {
        if (instance == null) {
            instance = new Rm();
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
            removeFile(Main.root);
        } else {
            /* if the path is not absolute */
            removeFile(Main.currDirectory);
        }

    }

    public void removeFile(Directory directory) {

        String[] tokens = filePath.split("/");   //store directories names into array
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
                            && dir.is().equals("Directory")) {
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

        /* if directory cannot be found, print error message */
        if (!exist1) {
            try {
                Main.writeError.write("rm: cannot remove " + filePath + ": No such file or directory");
                Main.writeError.write("\n");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            /* if directory/file can be found */
            boolean exist3 = false;     //contor to see if directory/file exists
            for (FileSystem dir : directory1.getList()) {
                if (tokens[tokens.length - 1].equalsIgnoreCase(".")) {
                    /* if sirectory is . */
                    exist3 = true;
                } else if (tokens[tokens.length - 1].equalsIgnoreCase("..")) {
                    /* if directory is .. */
                    exist3 = true;
                } else if (tokens[tokens.length - 1].equals(dir.getName())
                        && !dir.equals(directory1.getList().get(0))
                        && !dir.equals(directory1.getList().get(1))) {
                    exist3 = true;      // file/directory exists

                    /* if exists, check if it is a subtree of current directory*/
                    String currDirectoryPath = makePath(Main.currDirectory);    //path of current directory
                    String toBeDeletedPath = makePath(directory1);
                    if (toBeDeletedPath.equals("/")) {
                        toBeDeletedPath = "/" + tokens[tokens.length - 1];    //path of to be deleted file
                    } else {
                        toBeDeletedPath = toBeDeletedPath + "/" + tokens[tokens.length - 1];    //path of to be deleted file
                    }

                    /* if current directory path doesn't contain the to deleted file path,
                     * delete the file  
                     */
                    if (!currDirectoryPath.contains(toBeDeletedPath)) {
                        directory1.remove(dir);
                    }
                    break;
                }
            }

            /* if directory/file cannot be found, print error message */
            if (!exist3) {
                try {
                    Main.writeError.write("rm: cannot remove " + filePath + ": No such file or directory");
                    Main.writeError.write("\n");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
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
