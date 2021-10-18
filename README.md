# Shell Simulator
	
## Description
This is an implementation of a simplified shell that works with a file system in an easy and intuitive manner. The file system is perceived as a tree structure, with the root directory on top (it is similar with the Linux file system structure). The program uses design patterns such as: 
- **Command Pattern**
- **Singleton Pattern**
- **Factory Pattern**. 

####
Each command is defined as a singleton object, which is being chosen at runtime accordingly through the Factory Pattern, and executed through Command Pattern. The paths to the given files or directories will be similar with those used in Linux. No path will end with "/", except the root directory. The output will be redirected in the output file and the errors in the errors file.


## Usage
Compile and run from command prompt using given Makefile:
- **make**: compiles .java files;
- **java -cp classes Main INPUT_PATH=enter_input_path OUTPUT_PATH=enter_out_path ERRORS_PATH=enter_errors_path**: executes the program;
- **make clean**: removes .class files;

## Commands

#### *- ls [path]*
Lists the files within the current directory if no path is given as argument, or the files within the given directory. If the given directory isn't valid, an error is returned.
- -R: lists the tree structure of the file system in a depth-first manner, beginning with the current directory, or the given directory if path is specified. The files on each level of the tree are chosen in lexicographic order;

#### *- pwd*
Returns the absolute path of the current directory.

#### *- cd [path]*
Sets the current folder to the given directory. If the argument isn't a valid directory, an error is returned.

#### *- cp [source] [destination]*
Copies the source file in the destination directory. If the source file is not valid or it's missing, an error will be returned. If the destination directory isn't valid, it's missing or on it already exists a file with the same name, an error is returned.

#### *- rm [path]*
Removes the given file. If the file isn't valid or it's missing, an error is returned. If the current directory path includes the argument path, no actions is executed.

#### *- touch [file]*
Creates a new file with the given path. If the path isn't valid, an error is returned. If the file already exists in the given path, an error is returned.

#### *- mkdir [directory]*
Creates a new directory with the given path. If the path isn't valid, an error is returned. If the directory already exists in the given path, an error is returned.

#### *- grep [regex]*
It is used with the ls command, delimited by a pipe ("|"). It restrains the output of the ls command only to those which match the regex.

#### *- mv [source] [destination]*
Moves the source file in the destination directory. If the source file is not valid or it's missing, an error is returned. If the destination directory isn't valid, it's missing or in it already exists a file with the same name, an error is returned. If the source is a sub-tree that includes the current directory, the new current directory will be changed as well, accordingly with the new current directory sub-tree.


## Input
On each line of the input file is the command to be executed, with or without parameters.

## Output
On each line of the output file is shown the number of the command, and for those which an output is returned, the output is printed on the next line.

## Errors
On each of the errors file is shown the number of the command, and for those which an error is returned, the error is printed on the next line.
