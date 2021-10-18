# Shell Simulator
	
## Description
This is an implementation of a simplified shell that works with a file system in an easy and intuitive manner. The file system is perceived as a tree structure, with the root directory on top (it is similar with the Linux file system structure).

## Usage
The program will run as following:
1. compile and run from command promptusing given Makefile:
	- **make**: compiles .java files;
	- **java -cp classes Main INPUT_PATH=enter_input_path OUTPUT_PATH=enter_out_path ERRORS_PATH=enter_errors_path**: executes the program;
	- **make clean**: removes .class files;

## Commands
*ls [path]*
	- lists the files within the current directory if no path is given as argument, or the files within the given directory; if the given directory isn't valid, an error will be returned;
        - -R: lists the tree structure of the file system in a depth-first manner, beginning with the current directory, or the given directory if path is specified; the files on each level of the tree with be be chosen in a lexicographic order;

###
*pwd*
	- return the absolute path of the current directory;

        - cd [path]
            -> sets the current folder to the given directory; if the argument isn't 
          a valid directory, an error will be returned;

        - cp source destination
            -> copies the source file in the destination directory; if the source file
          is not valid or it's missing, an error will be returned; if the destination 
          directory isn't valid, it's missing or on it already exists a file with the 
          same name, an error will be returned;

        - mv source destination
            -> moves the source file in the destination directory; if the source file
          is not valid or it's missing, an error will be returned; if the destination 
          directory isn't valid, it's missing or on it already exists a file with the 
          same name, an error will be returned;
            -> if the source is aa sub-tree that includes the current directory, the 
          new current directory will be changed as well, accordingly with the new 
          current directory sub-tree.

        - rm path
            -> removes the given file; if the file isn't valid or it's missing, an 
          error will be returned;
            -> if the current directory path includes the argument path, no actions 
          will be executed;

        - touch file
            -> creates a new file with the given path; if the path isn't valid, an 
          error will be returned; if the file already exists in the given path, an 
          error will be returned;

        - mkdir directory
            -> creates a new directory with the given path; if the path isn't valid, 
          an error will be returned; if the directory already exists in the given 
          path, an error will be returned;

        - grep regex
            -> it will be used with the ls command, delimited by a pipe ("|"); it will
          restrain the output of the ls command only to those which match the regex;

    The program uses design patterns such as: Command Pattern, Singleton Pattern and
Factory Pattern. Each command is defined as a singleton object, which is being chosen
at runtime accordingly through the Factory Pattern, and executed through Command 
Pattern. The paths to the given files or directories will be similar with those used
in Linux. No path will end with "/", except the root directory. The output will be
redirected in the output file and the errors in the errors file.

    The input file will contain:
        -> on each line it will be the command to be executed, with or without 
      parameters;

    The output file will contain:
        -> on each line it will be shown the number of the command, and for those 
      which an output is returned, the output will be printed on the next line;

    The errors file will contain:
        -> on each line it will be shown the number of the command, and for those 
      which an error is returned, the error will be printed on the next line;
