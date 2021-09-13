/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Reciever class
 *
 * @author nick
 */
public class Directory implements FileSystem, Serializable {

    private String name;
    private ArrayList<FileSystem> fileSystem;

    public Directory(String name) {
        this.name = name;
        fileSystem = new ArrayList<>();
    }

    // get name of directory
    @Override
    public String getName() {
        return name;
    }

    //set name of directory
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String is() {
        return "Directory";
    }

    //add file to directory
    public void add(FileSystem f) {
        fileSystem.add(f);
    }

    //remove file from directory
    public void remove(FileSystem f) {
        fileSystem.remove(f);
    }

    public void replace(FileSystem f, int i) {
        fileSystem.remove(i);
        fileSystem.add(i, f);
    }

    //get list of files from directory
    public ArrayList<FileSystem> getList() {
        return fileSystem;
    }

    public void sort() {
        Comparator<? super FileSystem> cmprtr = null;
        fileSystem.sort(cmprtr);
    }
}
