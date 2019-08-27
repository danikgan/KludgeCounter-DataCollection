# Script for my Research Project

## Description
This script downloads the specified number of commits of each project following the project GIT link from the TXT file taken as an input. It runs PMD over each commit, identifying the unique PMD alerts considering the changes between two consecutive files. At a final step, it records the information in XLSX format.

## Requirements
1. Create an input TXT file named "links.txt", where every line corresponds to a project, such as a line contains "n link.git", where 'n' is the number of commits to analyse, and 'link.git' is the Git link to the desired project. If 'n' equals 0, then the script will download ALL commits of the project. Be aware that some project take long time to be analysed.
2. As you run the script, specify the path to the input TXT file. The output file will be created there, too.
3. Wait for the script to complete. Do not delete created files until the script is complete. It will delete these files automatically. However, the script should save your data upon abropt termination, and it will allow to resume on the next relaunch.

There will be created a file named "records.xlsx" upon completion. Any temporary files will be removed automatically. For faster runtime, make sure to have stable and fast internet connection, and powerful computer.

##### Current version of the code works on MacOS, Linux and Windows (but Windows may have compatability issues with PMD).

##### The root folder contains examples of the correct input file, named "links.txt", and the intended output file, named "records.txt".

## Abstract

Pressurized by close deadlines, developers have an incentive to commit quick code implementations instead of proper fixes which require longer development time. This introduces long term quality deteriorations and high maintenance costs, and is commonly known as ”technical debt”. The concept is not new and this paper investigates how quick patches affect long running open-source projects.

A simulation model was built, named ”KludgeCounter”. ”Kludge- Counter” gathers commit quality information from Git repositories and identifies bug reports using a linter tool. In this report, it was applied to the Apache Ant project using PMD to assess the quality of individual commits. Additionally, it collects JSON data from Bugzilla to identify the timeline of each bug creation and resolution. The data gathered is analyzed and probability distributions are modelled. These are used in the simulation model, allowing to further explore how different parame- ters of the simulation affect the total number of resolved bugs, and what are the chances of having a quick patch or a deeper fix. Developers are analyzed in terms of their commitment to submit deep fixes. For instance, it was discovered that in the Apache Ant Project, 27% of all developers submitted at least 1 commit with a PMD alert in it, but only about 10% of all commits have the alert. Finally, the simulation is used to model the real-world situations, and some hypothetical: worst- and best-case scenarios.

Overall, ”KludgeCounter” is part of an ongoing project where a game- theoretic model is used in empirical analysis of open-source projects. ”KludgeCounter” is a valuable tool for analyzing the impact of quick patches on the code base quality, supporting theoretical concepts by the data collected online.
