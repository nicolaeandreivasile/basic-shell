/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nick
 */
public class Cp implements Command {

    public static Cp instance = null;
    private String sourcePath;
    private String destPath;

    private Cp() {

    }

    public static Cp getInstance() {
        if (instance == null) {
            instance = new Cp();
        }
        return instance;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;

    }

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;

    }

    @Override
    public void execute() {
        String[] tokensSource = sourcePath.split("/");  // store directories names into array
        String firstSource = tokensSource[0];
        String firstDest = null;
        if (destPath.equalsIgnoreCase("/")) {
            firstDest = "/";
        } else {
            String[] tokensDest = destPath.split("/"); // store directories names into array
            firstDest = tokensDest[0];
        }

        FileSystem sourceFile = null;   // source file
        FileSystem destFile = null;     // dest folder

        /* if path is absolute for source*/
        if (firstSource.equalsIgnoreCase("")) {
            sourceFile = getFile(Main.root, sourcePath);
        } else {
            /* if the path is not absolute for source*/
            sourceFile = getFile(Main.currDirectory, sourcePath);
        }
        /* check if source and dest are valid */
        if (sourceFile == null) {
            return;
        }
        /* if path is absolute for dest*/
        if (firstDest.equalsIgnoreCase("")) {
            destFile = getFile(Main.root, destPath);
        } else {
            /* if the path is not absolute for dest*/
            destFile = getFile(Main.currDirectory, destPath);
        }
        /* check if source and dest are valid */
        if (destFile == null) {
            return;
        }

        makeCp(sourceFile, (Directory) destFile);   //make cp
    }

    public FileSystem getFile(Directory directory, String path) {

        String[] tokens = null;
        if (path.equals(sourcePath)) {
            tokens = sourcePath.split("/");     //store source directories names into array
        } else if (path.equals(destPath)) {
            if (destPath.equals("/")) {
                return Main.root;
            } else {
                tokens = destPath.split("/");       //store dest directories names into array
            }
        }
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
                if (path.equals(sourcePath)) {
                    Main.writeError.write("cp: cannot copy " + path + ": No such file or directory");
                } else if (path.equals(destPath)) {
                    Main.writeError.write("cp: cannot copy into " + path + ": No such directory");
                }
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
                            && !dir.equals(directory1.getList().get(1))) {
                        exist3 = true;      // file/directory exists
                        return dir;
                    }
                }
            }

            /* if directory/file cannot be found, print error message */
            if (!exist3) {
                try {
                    if (path.equals(sourcePath)) {
                        Main.writeError.write("cp: cannot copy " + path + ": No such file or directory");
                    } else if (path.equals(destPath)) {
                        Main.writeError.write("cp: cannot copy into " + path + ": No such directory");
                    }
                    Main.writeError.write("\n");
                    return null;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    public void makeCp(FileSystem sourceFile, Directory destFile) {

        /* check if file/directory exists in dest folder */
        boolean exist3 = false;     //contor to see if directory/file exists
        for (FileSystem dir : destFile.getList()) {
            if (sourceFile.getName().equals(dir.getName())
                    && !dir.equals(destFile.getList().get(0))
                    && !dir.equals(destFile.getList().get(1))) {
                exist3 = true;      // file/directory exists
                try {
                    Main.writeError.write("cp: cannot copy " + sourcePath + ": Node exists at destination");   // print error message
                    Main.writeError.write("\n");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        /* if directory/file doesn't exist, copy it */
        if (!exist3) {
            FileSystem returnFile = makeDeepCopy(sourceFile);   //make a deep copy

            /* if it's a directory, actualize parent directory */
            if (returnFile.is().equalsIgnoreCase("Directory")) {
                ((Directory) returnFile).replace(destFile, 1);
            } else if (returnFile.is().equalsIgnoreCase("File")) {
                /* if it's a file, actualize parent directory */
                ((File) returnFile).setParentDir(destFile);
            }
            destFile.add(returnFile);
        }
    }

    public FileSystem makeDeepCopy(FileSystem sourceFile) {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        FileSystem returnFile = null;

        /* make a deep copy using seialization and a char buffer */
        try {
            /* serialize sourceFile */
            ByteArrayOutputStream bufferOut = new ByteArrayOutputStream();  //create output buffer
            oos = new ObjectOutputStream(bufferOut);
            oos.writeObject(sourceFile);    //put sourceFile in buffer

            /* deserialize sourceFile */
            ByteArrayInputStream bufferIn = new ByteArrayInputStream(bufferOut.toByteArray());  //create input buffer
            ois = new ObjectInputStream(bufferIn);
            returnFile = (FileSystem) ois.readObject(); //extract sourceFile from input buffer
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return returnFile;  //return deep copy
    }
}
