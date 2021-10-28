---
layout: page
title: User Guide
---

Teaching Assistant’s Buddy (TAB) is a **desktop app for managing tutees, optimized for use via a Command Line Interface** (CLI) while still
having the benefits of a Graphical User Interface (GUI). If you can type fast, TAB can get your tutee management
tasks done faster than traditional GUI apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `11` or above installed in your Computer.

1. Download the latest `tab.jar` from [here](https://github.com/AY2122S1-CS2103-F09-1/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for TAB.

1. Double-click the file to start the app. The GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`add module CS2103`** and pressing Enter will add the module CS2103 to TAB.<br>
   Some example commands you can try:

   * **`module add CS2103`** : Adds the module CS2103 to TAB

   * **`student add A0123456A, John Doe, @johndoe, john.doe@u.nus.edu`** : Adds student John Doe to TAB

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `<angle brackets>` are the parameters to be supplied by the user.<br>
  e.g. in `module add <module name>`, `<module name>` is a parameter which can be used as `add module CS2103`.

* Parameters must be in the specified order.<br>
  e.g. if the command specifies `module add <module name>, <task name>`, `module add <task name>, <module name>` will not work

* Commas are used to separate parameters supplied by the user.

* In the current version, refrain from using commas within the parameters itself.

</div>


### Adding new task to a module: `add task`

Create a new task for a specific module
Task name should only contain alphanumeric characters
Task deadline should only contain alphanumeric characters and dashes

Format: `add task m/<module name>, a/<task name>, d/<task deadline>`

Examples:
* `add task m/CS2103 a/assignment1 d/2021.10.12`:
  * Adds the task assignment1 with deadline on 12 October 2021 to module CS2103
* `add task m/CS2100 a/lab1 d/2021.10.13 `:
  * Adds the task lab1 with deadline on 13 October 2021 to module CS2100

### Creating a new module: `add module`

Adds a new module to TAB

Format: `add module m/<module name>`

Examples:
* `add module m/CS2103`: Adds module CS2103 to TAB
* `add module m/CS2100`: Adds module CS2100 to TAB

### Editing a module's name: `edit module`

Edits the name of the module

Format: `edit module m/<old module name> mn/<new module name>`

Examples:
* `edit module m/CS2103 mn/CS2105`: Renames the module "CS2103" to "CS2105"


### Add student to a specific module: `add student`

Add a new student to the specified module
Also automatically copies over the task list from the module to the student

Format: `add student m/<module name> i/<student id> n/<student name> t/<student tele handle> e/<student email>`

Examples:
* `add student m/CS2103 i/A0123456A n/John Doe t/@johndoe e/john.doe@u.nus.edu`: Adds student with id
   A0123456A to module CS2103
* `add student m/CS2100 i/A0123457A n/Amy t/@amylee e/amy@u.nus.edu`: Adds student with id A0123457A to module CS2100

### Delete student from a specific module: `delete student`

Deletes the specified student from the specified module

Format: `delete student m/<module name> i/<student id>`

Examples:
* `delete student m/CS2103 i/A1234567A`: Removes student with id A1234567A from module CS2103’s list of students
* `delete student m/CS2100 i/A7654321A`: Removes student with id A7654321A from module CS2100’s list of students

### Edit a student's information: `edit student`

Edits a student's information (at least one editable field (name/tele handle/email) should be provided, 
and only provided editable fields will be changed)

Format: `edit student m/<module name> i/<student id> n/<edited student name> t/<edited student tele handle> e/<edited student email>`

Examples:
* `edit student m/CS2103 i/A0123456A n/John Doe`: Edits the name of the student with id
  A0123456A in module CS2103 to John Doe
* `edit student m/CS2100 i/A0123457A t/@amylee e/amy@u.nus.edu`: Edits the tele handle and email of the student with id
  A0123457A in module CS2100 to @amylee and amy@u.nus.edu respectively

### Edit a task's information: `edit task`

Edits a task's information (at least one editable field (name/deadline) should be provided,
and only provided editable fields will be changed)
Task name should only contain alphanumeric characters
Task deadline should only contain alphanumeric characters and dashes

Format: `edit task m/<module name> ti/<task id> n/<edited student name> t/<edited student tele handle> e/<edited student email>`

Examples:
* `edit task m/CS2103 i/T10 a/Final exam d/20-11-2021`: Edits the name and deadline of the task with task id 'T10'
  to 'Final exam' and '20-11-2021' respectively
* `edit task m/CS2103 i/T10 d/23-11-2021`: Edits only the deadline of the task with task id 'T10' to '20-11-2021'

### Delete a task from a module: `delete task`

Deletes the specified task from the specified module using the TaskID.
Also deletes the task from all the students in that module.

Format: `delete task m/<module name> ti/<task id>`

Examples:
* `delete task m/CS2103 ti/T1`: Deletes the task with task ID T1 from CS2103
* `delete task m/CS2105 ti/T2`: Deletes the task with task ID T2 from CS2105

### Mark a task as done: `mark done`

Mark the specified task from the specified module under the specified student as done.
This means that this student has completed this task of this module.

Format: `mark done m/<module name> i/<student id> ti/<task id>`

Examples:
* `mark done m/CS2103 i/A1234567A ti/T1`:
  * Mark the task with ID T1 under student with ID A1234567A of module CS2103 as done.
  * Student A1234567A has completed task T1 of module CS2103.

### Mark a task as undone: `mark undone`

Mark the specified task from the specified module under the specified student as undone.
This means that this student has not completed this task of this module, possibly to correct a mistake by the TA.

Format: `mark undone m/<module name> i/<student id> ti/<task id>`

Examples:
* `mark undone m/CS2103 i/A1234567A ti/T1`:
    * Mark the task with ID T1 under student with ID A1234567A of module CS2103 as undone.
    * Student A1234567A has not completed task T1 of module CS2103 yet.
    * But for some reason (eg. human error) the task is marked as done.

### Find a student: `find`

Find the student in the module specified along with the student's information

Format: `find m/<module name> i/<student id>`

Examples:
* `find m/CS2103 i/A0123456A`: Finds and shows student with ID A0123456A from module CS2103

### Delete module from TAB: `delete module`

Deletes the specified module from TAB

Format: `delete module m/module_name`

Examples:
* `delete module m/CS2103`: Deletes module CS2103 from TAB
* `delete module m/CS2100`: Deletes module CS2100 from TAB

### Go to homepage: `home`

Returns to homepage which shows the complete list of modules and students along with their tasks

Format: `home`

### Clear all information: `clear`

Clears all information stored in the application

Format: `clear`

### Exit the application: `exit`

Closes Teaching Assistant's Buddy application

Format: `exit`

## FAQ

**Q**: How do I assign tasks to students<br>
**A**: Adding the student to the module will automatically add the tasks in the module to the student

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `module add <module name>` <br> e.g., `module add CS2103`
**Delete** | `module delete <module name>`<br> e.g., `module delete CS2103`
