/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;

/**
 *
 * @author nick
 */
public class File implements FileSystem, Serializable {

    private String name;
    private Directory parentDir;

    public File(String name) {
        this.name = name;
    }

    //get name of file 
    @Override
    public String getName() {
        return name;
    }

    //set name of file
    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Directory getParentDir() {
        return parentDir;
    }

    public void setParentDir(Directory parentDir) {
        this.parentDir = parentDir;
    }

    @Override
    public String is() {
        return "File";
    }

}
