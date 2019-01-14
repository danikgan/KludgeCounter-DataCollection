# Script for my Research Project

## Description
This script downloads the specified number of commits of each project following the project GIT link from the TXT file taken as an input. It runs PMD over each commit, identifying the unique PMD alerts considering the changes between two consecutive files. At a final step, it records the information in XLSX format.

## Requirements
1. Create an input TXT file named "links.txt", where every line corresponds to a project, such as a line contains "n link.git", where 'n' is the number of commits to analyse, and 'link.git' is the Git link to the desired project. If 'n' equals 0, then the script will download ALL commits of the project. Be aware that some project take long time to be analysed.
2. As you run the script, specify the path to the input TXT file. The output file will be created there, too.
3. Wait for the script to complete. Do not delete created files until the script is complete. It will delete these files automatically.

There will be created a file named "records.xlsx" upon completion. Any temporary files will be removed automatically. For faster runtime, make sure to have stable and fast internet connection, and powerful computer.

##### Current version of the code works on MacOS only.

##### The root folder contains examples of the correct input file, named "links.txt", and the intended output file, named "records.txt".
