/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.regex.Pattern;

/**
 *
 * @author nick
 */
public class CommandFactory {

    public Command getCommand(String command) {

        String[] tokens = command.split(" ");
        if (tokens[0] == null) {
            return null;
        }
        if (tokens[0].equalsIgnoreCase("ls")) {
            Ls comm = Ls.getInstance();
            String[] lsTokens = command.split(" \\| ");
            String[] token = lsTokens[0].split(" ");

            if (token.length == 1) {
                comm.setR(null);
                comm.setDirPath(null);
            } else if (token.length == 2) {
                if (tokens[1].equals("-R")) {
                    comm.setR(tokens[1]);
                    comm.setDirPath(null);
                } else {
                    comm.setR(null);
                    comm.setDirPath(tokens[1]);
                }
            } else if (token.length == 3) {
                if (tokens[1].equals("-R")) {
                    comm.setR(tokens[1]);
                    comm.setDirPath(tokens[2]);
                } else {
                    comm.setDirPath(tokens[1]);
                    comm.setR(tokens[2]);
                }
            }

            if (command.contains("grep")) {
                String regex = tokens[tokens.length - 1];
                regex = regex.substring(1, regex.length() - 1);
                comm.setGepRegex(Pattern.compile(regex));
            } else {
                comm.setGepRegex(null);
            }
            return comm;

        } else if (tokens[0].equalsIgnoreCase("pwd")) {
            return Pwd.getInstance();

        } else if (tokens[0].equalsIgnoreCase("cd")) {
            Cd comm = Cd.getInstance();
            comm.setDirPath(tokens[1]);
            return comm;

        } else if (tokens[0].equalsIgnoreCase("cp")) {
            Cp comm = Cp.getInstance();
            comm.setSourcePath(tokens[1]);
            comm.setDestPath(tokens[2]);
            return comm;

        } else if (tokens[0].equalsIgnoreCase("mv")) {
            Mv comm = Mv.getInstance();
            comm.setSourcePath(tokens[1]);
            comm.setDestPath(tokens[2]);
            return comm;

        } else if (tokens[0].equalsIgnoreCase("rm")) {
            Rm comm = Rm.getInstance();
            comm.setFilePath(tokens[1]);
            return comm;

        } else if (tokens[0].equalsIgnoreCase("touch")) {
            Touch comm = Touch.getInstance();
            comm.setFilePath(tokens[1]);
            return comm;

        } else if (tokens[0].equalsIgnoreCase("mkdir")) {
            Mkdir comm = Mkdir.getInstance();
            comm.setDirPath(tokens[1]);
            return comm;

        }
        return null;
    }

}
