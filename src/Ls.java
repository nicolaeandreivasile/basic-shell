/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nick
 */
public class Ls implements Command {

    public static Ls instance = null;
    private String dirPath = null;
    private String R = null;
    private Pattern grepRegex = null;

    private Ls() {

    }

    public static Ls getInstance() {
        if (instance == null) {
            instance = new Ls();
        }
        return instance;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getR() {
        return R;
    }

    public void setR(String R) {
        this.R = R;
    }

    public Pattern getGrepRegex() {
        return grepRegex;
    }

    public void setGepRegex(Pattern grepRegex) {
        this.grepRegex = grepRegex;
    }

    @Override
    public void execute() {
        Directory lsDir = null;
        if (dirPath == null) {
            /* if directory is current directory */
            lsDir = Main.currDirectory;
        } else if (dirPath.equals("/")) {
            /* if directory is root */
            lsDir = Main.root;
        } else {
            /* if drectory is not root */
            String[] tokens = dirPath.split("/");

            /* if path is absolute */
            if (tokens[0].equalsIgnoreCase("")) {
                lsDir = (Directory) getFile(Main.root);
            } else {
                /* if path is not absolute */
                lsDir = (Directory) getFile(Main.currDirectory);
            }
        }

        if (lsDir == null) {
            return;
        }

        if (R == null) {
            makeLs(lsDir);
        } else {
            makeLsR(lsDir);
        }

    }

    public FileSystem getFile(Directory directory) {

        String[] tokens = dirPath.split("/");   //if there is, continue
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
                Main.writeError.write("ls: " + dirPath + ": No such directory");
                Main.writeError.write("\n");
                return null;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            /* if directory/file can be found */
            boolean exist3 = false;     //contor to see if directory/file exists

            /* if directory is . */
            if (tokens[tokens.length - 1].equalsIgnoreCase(".")) {
                return (Directory) directory1.getList().get(0);
            } else if (tokens[tokens.length - 1].equalsIgnoreCase("..")) {
                /* if directory is .. */
                if (!directory1.getName().equalsIgnoreCase("/")) {
                    return (Directory) directory1.getList().get(1);
                }
            } else {
                for (FileSystem dir : directory1.getList()) {
                    if (tokens[tokens.length - 1].equals(dir.getName())
                            && !dir.equals(directory1.getList().get(0))
                            && !dir.equals(directory1.getList().get(1))
                            && dir.is().equals("Directory")) {
                        exist3 = true;      // file/directory exists
                        return dir;
                    }
                }
            }


            /* if directory/file cannot be found, print error message */
            if (!exist3) {
                //to do
                try {
                    Main.writeError.write("ls: " + dirPath + ": No such directory");
                    Main.writeError.write("\n");
                    return null;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    public void makeLs(Directory directory) {

        String path = makePath(directory);
        try {
            Main.writeOutput.write(path + ":");
            Main.writeOutput.write("\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Collections.sort(directory.getList().subList(2, directory.getList().size()), new Comparator<FileSystem>() {
            @Override
            public int compare(FileSystem t, FileSystem t1) {
                return t.getName().compareTo(t1.getName());
            }
        });

        for (int i = 2; i < directory.getList().size(); i++) {
            String pathEach = null;
            if (directory.getList().get(i).is().equals("Directory")) {
                pathEach = makePath((Directory) directory.getList().get(i));

            } else if (directory.getList().get(i).is().equals("File")) {
                pathEach = makePath(((File) directory.getList().get(i)).getParentDir());
                if (pathEach.equals("/")) {
                    pathEach = "/" + directory.getList().get(i).getName();
                } else {
                    pathEach = pathEach + "/" + directory.getList().get(i).getName();
                }
            }
            if (grepRegex != null) {
                Matcher match = grepRegex.matcher(directory.getList().get(i).getName());
                if (match.matches()) {
                    try {
                        Main.writeOutput.write(pathEach);
                        if (i != directory.getList().size() - 1) {
                            Main.writeOutput.write(" ");
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                try {
                    Main.writeOutput.write(pathEach);
                    if (i != directory.getList().size() - 1) {
                        Main.writeOutput.write(" ");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        }
        try {
            Main.writeOutput.write("\n");
            Main.writeOutput.write("\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void makeLsR(FileSystem directory) {

        if (directory.is().equals("File")) {
            return;
        }

        makeLs((Directory) directory);

        for (int i = 2; i < ((Directory) directory).getList().size(); i++) {
            makeLsR(((Directory) directory).getList().get(i));
        }
        return;
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
