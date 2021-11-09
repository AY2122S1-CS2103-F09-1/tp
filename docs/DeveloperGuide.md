---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

## **Acknowledgements**

* This project is evolved from the [AddressBook Level 3 (AB3)](https://se-education.org/addressbook-level3/UserGuide.html), a desktop app for managing contacts, optimized for use via a Command Line Interface (CLI).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/AY2122S1-CS2103-F09-1/tp/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

<div style="page-break-after: always;"></div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/AY2122S1-CS2103-F09-1/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2122S1-CS2103-F09-1/tp/blob/master/src/main/java/seedu/address/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.


**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete module m/CS2103`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

<div style="page-break-after: always;"></div>

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

<div style="page-break-after: always;"></div>

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2122S1-CS2103-F09-1/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `ModuleListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2122S1-CS2103-F09-1/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2122S1-CS2103-F09-1/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Module` object residing in the `Model`.

<div style="page-break-after: always;"></div>

### Logic component

**API** : [`Logic.java`](https://github.com/AY2122S1-CS2103-F09-1/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When the `Logic` is called upon to execute `delete module m/CS2103`, it uses the `TeachingAssistantBuddyParser` class to parse the user command.
2. The `TeachingAssistantBuddyParser` parses the first command word `delete`, and pass the rest of the input to a `DeleteCommandParser`.
3. The `DeleteCommandParser` then figures out the type of object to delete, in this case a `Module` object as indicated by `module`.
4. The `DeleteModuleCommandParser` will wrap the module name in a `ModuleName` object and pass it into a `DeleteModuleCommand`.
5. This results in a `DeleteModuleCommand` object (which is a subclass of `DeleteCommand`), which is executed by the `Logic Manager`.

![How the command `delete module m/CS2103` is parsed in the Logic component](images/DeleteModuleSequenceDiagram1.png)
6. The `DeleteModuleCommand` communicates with the `Model` when it is executed.
7. The `Model` will look for a `Module` with the specified `ModuleName` and delete it.
8. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.

![How the `DeleteModuleCommand` is executed](images/DeleteModuleSequenceDiagram2.png)

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `TeachingAssistantBuddyParser` class creates an `XYZTypeCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `Add`, `Edit`. `Type` is a placeholer for the type of object to be operated on e.g., `Module`, `Student` or `Task`) which uses the other classes shown above to parse the user command and create a `XYZTypeCommand` object (e.g., `AddModuleCommand`) which the `TeachingAssistantBuddyParser` returns back as a `Command` object.
* All `XYZTypeCommandParser` classes (e.g., `AddModuleCommandParser`, `DeleteModuleCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

<div style="page-break-after: always;"></div>

### Model component
**API** : [`Model.java`](https://github.com/AY2122S1-CS2103-F09-1/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the Teaching Assistant Buddy data i.e., all `Module` objects (which are contained in a `UniqueModuleList` object).
* stores the currently 'selected' `Module` objects which is exposed to outsiders as an unmodifiable `ObservableList<Module>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change. `Module` also stores a unique student list and a filtered student list (used for example, when searching for a specific `Student`). Each `Student` has a unique task list.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div style="page-break-after: always;"></div>

### Storage component

**API** : [`Storage.java`](https://github.com/AY2122S1-CS2103-F09-1/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both teaching assistant buddy data and user preference data in json format, and read them back into corresponding objects.
* inherits from both `TeachingAssistantBuddy` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Add Module

1. When the `Logic` is called upon to execute `add module m/CS2103`, it uses the `TeachingAssistantBuddyParser` class to parse the user command.
2. The `TeachingAssistantBuddyParser` parses the first command word `add`, and pass the rest of the input to a `AddCommandParser`.
3. The `AddCommandParser` then figures out the type of object to add, in this case a `Module` object as indicated by `module`.
4. The `AddModuleCommandParser` will wrap the module name in a `ModuleName` object and pass it into a `AddModuleCommand`.
5. This results in a `AddModuleCommand` object (which is a subclass of `AddCommand`), which is executed by the `Logic Manager`.
![How the command `add module m/CS2103` is parsed in the Logic component](images/AddModuleSequenceDiagram1.png)
6. The `AddModuleCommand` communicates with the `Model` when it is executed.
7. The `Model` will add a new `Module` with the new `ModuleName`.
8. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.

![How the command `add module m/CS2103` is executed](images/AddModuleSequenceDiagram2.png)

<div style="page-break-after: always;"></div>

### Edit Module

1. When the `Logic` is called upon to execute `edit module m/CS2103 mn/CS2105`, it uses the `TeachingAssistantBuddyParser` class to parse the user command.
2. The `TeachingAssistantBuddyParser` parses the first command word `edit`, and pass the rest of the input to a `EditCommandParser`.
3. The `EditCommandParser` then figures out the type of object to edit, in this case a `Module` object as indicated by `module`.
4. The `EditModuleCommandParser` will parse the tokens `m/<old module name>` and `mn/<new module name>`.
5. `EditModuleCommandParser` will also create a `EditModuleCommand` and the module to edit, which is executed by the `LogicManager`
![How the command `edit module m/CS2103 mn/CS2105` is parsed in the Logic component](images/EditModuleSequenceDiagram1.png)
6. The `EditModuleCommand` communicates with the `Model` when it is executed.
7. The `EditModuleCommand` will look for a `Module` with the specified name and edit the name after getting a list of modules from `Model`.
8. The `Model` will then cahnge the old module to the newly edited module.
9. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.

![How the command `edit module m/CS2103 mn/CS2105` is executed](images/EditModuleSequenceDiagram2.png)

<div style="page-break-after: always;"></div>

### Add Student

1. When the `Logic` is called upon to execute `add student m/CS2103 i/A1234567A n/Amy bee t/@amybee e/amybee@gmail.com`, it uses the `TeachingAssistantBuddyParser` class to parse the user command. (refer to the diagram for **Add Module**)
2. The `TeachingAssistantBuddyParser` parses the first command word `add`, and pass the rest of the input to a `AddCommandParser`.
3. The `AddCommandParser` then figures out the type of object to add, in this case a `Student` object as indicated by `student`.
4. The `AddStudentCommandParser` will wrap the module name in a `ModuleName` object and the student's information in a `Student` object and pass it into a `AddStudentCommand`.
5. This results in a `AddStudentCommand` object (which is a subclass of `AddCommand`), which is executed by the `Logic Manager`.
6. The `AddStudentCommand` communicates with the `Model` when it is executed.
7. The `Model` will look for a `Module` with the specified `ModuleName`.
8. The `Module` with the specified `ModuleName` will then add the new `Student` object to its student list.
9. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.
![How the `AddStudentCommand` is executed](images/AddStudentSequenceDiagram2.png)

<div style="page-break-after: always;"></div>

### Delete Student

1. When the `Logic` is called upon to execute `delete student m/CS2103 i/A1234567A`, it uses the `TeachingAssistantBuddyParser` class to parse the user command. (refer to the diagram for **Delete Module** under **Design** > **Logic component**)
2. The `TeachingAssistantBuddyParser` parses the first command word `delete`, and pass the rest of the input to a `DeleteCommandParser`.
3. The `DeleteCommandParser` then figures out the type of object to delete, in this case a `Student` object as indicated by `student`.
4. The `DeleteStudentCommandParser` will wrap the module name in a `ModuleName` object and the student ID in a `StudentId` object and pass them into a `DeleteStudentCommand`.
5. This results in a `DeleteStudentCommand` object (which is a subclass of `DeleteCommand`), which is executed by the `Logic Manager`.
6. The `DeleteStudentCommand` communicates with the `Model` when it is executed.
7. The `Model` will look for a `Module` with the specified `ModuleName`.
8. The `Module` with the specified `ModuleName` will then look for the student with the specified `StudentId` and delete the student.
9. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.
![How the `DeleteStudentCommand` is executed](images/DeleteStudentSequenceDiagram2.png)

<div style="page-break-after: always;"></div>

### Edit Student

1. When `Logic` is called upon to execute the user input `edit student m/CS2103 i/A1234567A n/Amy Lee`, it uses the
   `TeachingAssistantBuddyParser` class to parse the user command. (refer to the diagram for **Edit Module**)
2. `TeachingAssistantBuddyParser` parses the first command word `edit`, and pass the rest of the input to `EditCommandParser`.
3. The `EditCommandParser` then figures out the type of object to edit, in this case a `Student` object as indicated by `student`.
4. The `EditStudentCommandParser` will then parse the tokens, `m/CS2103`, `i/A1234567A` and `n/Amy Lee` in this case, to determine the moduleName (`CS2103`in this case),
   and to create an `EditStudentDescriptor` with the student ID `A1234567A` and the name `Amy Lee`, and pass these `ModuleName` and `EditStudentDescriptor` objects into an `EditStudentCommand`.
5. This results in a `EditStudentCommand` object (which is a subclass of `EditCommand`), which is executed by the `Logic
   Manager`.
6. The `EditStudentCommand` communicates with the `Model` when it is executed.
7. The `Model` will look for the `Student` with the specified student id, `A1234567A` in this case,  inside the `Module` specified by
   the module name, `CS2103` in this case, and then replacing the old editable fields (such as student name, tele handle and email)
   of that `Student` using any optionally provided new fields.
8. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.
![How the `EditStudentCommand` is executed](images/EditStudentSequenceDiagram2.png)

<div style="page-break-after: always;"></div>

### Find Student

1. When the `Logic` is called upon to execute `find m/CS2103 i/A1234567A`, it uses the `TeachingAssistantBuddyParser` class to parse the user command. (refer to the diagram under **Design** > **Logic component**)
2. The `TeachingAssistantBuddyParser` parses the first command word `find`, and pass the rest of the input to a `FindStudentCommandParser`.
3. The `FindStudentCommandParser` will wrap the module name in a `ModuleName` object and the student's ID in a `StudentId` object and pass it into a `FindStudentCommand`.
4. This results in a `FindStudentCommand` object, which is executed by the `Logic Manager`.
5. The `FindStudentCommand` communicates with the `Model` when it is executed.
6. The `Model` will look for a `Module` with the specified `ModuleName`.
7. The `Module` with the specified `ModuleName` will then look for the `Student` with the specified `StudentId` and update the module's filtered student's list to only contain this specific student.
8. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.
![How the `FindStudentCommand` is executed](images/FindStudentSequenceDiagram2.png)

<div style="page-break-after: always;"></div>

### Add Task

1. When `Logic` is called upon to execute the user input `add task m/CS2103 ti/T1 a/assignment1 d/2021-10-20`, it uses the 
   `TeachingAssistantBuddyParser` class to parse the user command. (refer to the diagram for **Add Module**)
2. `TeachingAssistantBuddyParser` parses the first command word `add`, and pass the rest of the input to `AddCommandParser`.
3. `AddCommandParser` then figures out the type of object to add, in this case a `task` object as indicated by the 
   keyword `task`, and pass the rest of the arguments to `AddTaskCommandParser`.
4. `AddTaskCommandParser` will create a `moduleName` object, a `taskName` object, and a `taskDeadline` object based on 
   the input arguments. They are then passed to `AddTaskCommand`.
5. This results in a `AddTaskCommand` object (which is a subclass of `AddCommand`), which is executed by the `Logic 
   Manager`.
6. The `AddTaskCommand` communicates with the `Model` when it is executed.
7. The `Model` will add a new `Task` with the new `taskName` under the `Module` with that `moduleName`. The `Task` will also be 
   added to all the `Student`s under the specified `Module`.
8. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.
![How the command `add task m/CS2103 ti/T1 a/assignment1 d/2021-10-20` is executed](images/AddTaskSequenceDiagram2.png)

<div style="page-break-after: always;"></div>

### Edit Task

1. When `Logic` is called upon to execute the user input `edit task m/CS2103 ti/T1 a/final exam`, it uses the
   `TeachingAssistantBuddyParser` class to parse the user command. (refer to the diagram for **Edit Module**)
2. `TeachingAssistantBuddyParser` parses the first command word `edit`, and pass the rest of the input to `EditCommandParser`.
3. The `EditCommandParser` then figures out the type of object to edit, in this case a `Task` object as indicated by `task`.
4. The `EditTaskCommandParser` will then parse the tokens, `m/CS2103` and `ti/T1` in this case, to determine the module (`CS2103`in this case), 
   and the taskId (`T1` in this case) of the task that is being edited, and pass the rest of the tokens to `EditTaskCommand`.
5. The `EditTaskCommand` create an `EditTaskDescriptor` object (defined in EditTaskCommand), using the given `moduleName` object, 
   `taskId` object, and any other tokens passed from `EditTaskCommandParser` representing the newly updated fields of the task object.
6. This results in a `EditTaskCommand` object (which is a subclass of `EditCommand`), which is executed by the `Logic
   Manager`.
7. The `EditTaskCommand` communicates with the `Model` when it is executed.
8. The `EditTaskCommand` will get a list of modules from `Model` and look for the specified `Module`.
9. The `EditTaskCommand` will look for the `Task` with the specified task id, `T1` in this case, for every `Student` in 
   the student list under `Module` specified by the module name, `CS2103` in this case, and replacing the old editable
   fields of (such as task name, task deadline) of that `Task` using any optionally provided new fields.
10. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.
    
![How the command `edit task m/CS2103 ti/T1 a/final exam` is executed](images/EditTaskSequenceDiagram2.png)

<div style="page-break-after: always;"></div>

### Delete Task

1. When the `Logic` is called upon to execute `delete task m/CS2103 ti/T1`, it uses the `TeachingAssistantBuddyParser` class to parse the user command. (refer to the diagram for **Delete Module** under **Design** > **Logic component**)
2. The `TeachingAssistantBuddyParser` parses the first command word `delete`, and pass the rest of the input to a `DeleteCommandParser`.
3. The `DeleteTaskCommandParser` then figures out the type of object to delete, in this case a `Task` object as indicated by `task`.
4. The `DeleteStudentCommandParser` will wrap the module name in a `ModuleName` object and the task ID in a `TaskId` object and pass them into a `DeleteTaskCommand`.
5. This results in a `DeleteTaskCommand` object (which is a subclass of `DeleteCommand`), which is executed by the `Logic Manager`.
6. The `DeleteTaskCommand` communicates with the `Model` when it is executed.
7. The `DeleteTaskCommand` will get the current list of modules from `Model`, and look for the specified `Module`.
8. The `Module` with the specified `ModuleName` will then look for the task with the specified `TaskId` and delete the task.
9. The `Module` will also iterate through its `Student` list and delete the specified `Task` from all the students.
10. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.

![How the command `delete task m/CS2103 ti/T1` is executed](images/DeleteTaskSequenceDiagram2.png)

--------------------------------------------------------------------------------------------------------------------
<div style="page-break-after: always;"></div>

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is a tutor overseeing students for an academic module or subject
* has a need to manage a moderate to large number of students
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: manage students' academic progress faster than a typical mouse/GUI driven app


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                 | I want to …​                | So that I can…​                                                     |
| -------- | ------------------------------------------ | ------------------------------ | ---------------------------------------------------------------------- |
| `* * *`  | new user                                   | add a student to a module      |                                                                        |
| `* * *`  | new user                                   | delete a student from a module | remove data that I no longer need                                      |
| `* * *`  | new user                                   | edit a student's information   | change and update the student's information easily                     |
| `* * *`  | new user                                   | get a student's information    | find and check a student's progress easily                             |
| `* * *`  | new user                                   | create a new module            |                                                                        |
| `* * *`  | new user                                   | delete a module                | remove data that I no longer need                                      |
| `* * *`  | new user                                   | edit a module's name           | change and update the module's name easily                             |
| `* * *`  | new user                                   | add task to a module           |                                                                        |
| `* * *`  | new user                                   | delete task from a module      | remove data that I no longer need                                      |
| `* * *`  | new user                                   | edit a task's information      | change and update information about the task easily                    |
| `* * *`  | new user                                   | mark a task as done            | track my students' progress                                            |
| `* * *`  | new user                                   | mark a task as undone          | track my students' progress                                            |


<div style="page-break-after: always;"></div>

### Use cases

(For all use cases below, the **System** is `Teaching Assistant’s Buddy` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Add a student to a module**

**MSS**

1. User requests to add a student to a module by listing down the module's and student's information
2. System finds the module
3. System adds the student into the module's student's list

   Use case ends.

**Extensions**

* 1a. Module is not found.
    * 1a1. System shows an error message.

      Use case ends.
* 1b. Student's ID is already in the module student's list.
    * 1b1. System shows an error message.

      Use case ends.

**Use case: Delete a student from a module**

**MSS**

1. User requests to delete a specific student from a module in system
2. System finds the module
3. System deletes the student from the module

   Use case ends.

**Extensions**

* 1a. Module is not found.
    * 1a1. System shows an error message.

      Use case ends.
* 1b. Student's ID is not found in the module's student's list.
    * 1b1. System shows an error message.

      Use case ends.

**Use case: Edit a student's information**

**MSS**

1. User requests to edit a specific student's information in a module
2. System finds the module
3. System finds the student with the specified student ID
4. System edits the student's information

   Use case ends.

**Extensions**

* 1a. Module is not found.
    * 1a1. System shows an error message.

      Use case ends.
* 1b. Student's ID is not found in the module's student's list.
    * 1b1. System shows an error message.

      Use case ends.

**Use case: Get a student's information**

**MSS**

1. User requests to find a student in a module and get his/her information
2. System finds the module
3. System finds the student and shows his/her information

   Use case ends.

**Extensions**

* 1a. Module is not found.
    * 1a1. System shows an error message.

      Use case ends.
* 1b. Student is not found.
    * 1b1. System shows an error message.

      Use case ends.

<div style="page-break-after: always;"></div>

**Use case: Create a new module**

**MSS**

1. User requests to create a new module in System
2. System creates the module

   Use case ends.

**Extensions**

* 1a. Module is already in System.
    * 1a1. System shows an error message.

      Use case ends.

**Use case: Delete a module**

**MSS**

1. User requests to delete a module in System
2. System deletes the module

   Use case ends.

**Extensions**

* 1a. Module is not found.
    * 1a1. System shows an error message.

      Use case ends.

**Use case: Edit a module's name**

**MSS**

1. User requests to edit a module's name
2. System finds the module
3. System edits the module's name 

   Use case ends.

**Extensions**

* 1a. Module is not found.
    * 1a1. System shows an error message.

      Use case ends.

<div style="page-break-after: always;"></div>

**Use case: Add task to a module**

**MSS**

1. User requests to add a task to a module in System
2. System adds the task to the module and its students

   Use case ends.

**Extensions**

* 1a. Task is not found.
    * 1a1. System shows an error message.

      Use case ends.
* 1b. Module is not found.
    * 1b1. System shows an error message.

      Use case ends.

**Use case: Delete task from a module**

**MSS**

1. User requests to delete a task from a module in System
2. System deletes the task from the module and its students

   Use case ends.

**Extensions**

* 1a. Task is not found.
    * 1a1. System shows an error message.

      Use case ends.
* 1b. Module is not found.
    * 1b1. System shows an error message.

      Use case ends.


**Use case: Mark task as done**

**MSS**

1. User requests to mark a task of a student in a module as done
2. System marks the task as done

   Use case ends.

**Extensions**

* 1a. Module is not found.
    * 1a1. System shows an error message.

      Use case ends.
* 1b. Student is not found.
    * 1b1. System shows an error message.

      Use case ends.
* 1c. Task is not found.
    * 1c1. System shows an error message.

      Use case ends.

**Use case: Mark task as not done**

**MSS**

1. User requests to mark a task of a student in a module as not done
2. System marks the task as not done

   Use case ends.

**Extensions**

* 1a. Module is not found.
    * 1a1. System shows an error message.

      Use case ends.
* 1b. Student is not found.
    * 1b1. System shows an error message.

      Use case ends.
* 1c. Task is not found.
    * 1c1. System shows an error message.

      Use case ends.

<div style="page-break-after: always;"></div>

**Use case: Edit a task's information**

**MSS**

1. User requests to edit a task's information
2. System finds the module and students that have the specified task
3. System edits the task's information

   Use case ends.

**Extensions**

* 1a. Module is not found.
    * 1a1. System shows an error message.

      Use case ends.
* 1b. Task is not found.
    * 1b1. System shows an error message.

      Use case ends.

<div style="page-break-after: always;"></div>
    
### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2. Should be able to respond to a user command within 2 seconds.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Should be able to handle up to 100 students without significant lag in response time.


### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Module**: An academic module or subject that is frequently found in universities and institutes of higher learning

--------------------------------------------------------------------------------------------------------------------
<div style="page-break-after: always;"></div>

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch of the TAB application.

   1. Download the jar file and copy into an empty folder.

   2. Double-click the jar file.
      Expected: Shows the GUI with a set of sample contacts. The window size may not be optimal.

2. Shutting down the TAB application.

   1. Close the application using the close button in the application window, or using the exit command as listed in a section below.
   
3. Dealing with missing/corrupted data files. 
   1. When the data file is missing or not in the correct format, the application will be starting with an empty TeachingAssistantBuddy.

### Saving the data

1. TAB data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

<div style="page-break-after: always;"></div>

### Adding a module

1. Adding a module while all modules are being shown.

   1. Prerequisites: List all modules using the `home` command. TAB can be empty.
   
   2. Test case: `add module m/CS2105`<br>
     Expected: The `Module` with the name `CS2105` is added to the list of modules.
   
   3. Test case: `add module m/ALREADY_EXISTING_MODULE`<br>
     Expected: No `Module` is added. Error details shown in the status message.
   
   4. Other incorrect add module commands to try: `add module`, `add subject`, `...`<br>
     Expected: Similar to previous.
   
### Editing a module

1. Editing a module while all modules are being shown.

   1. Prerequisites: List all modules using the `home` command. At least 1 `Module` is in the TAB.
   
   2. Test case: `edit module m/CS2100 mn/CS2106`<br>
      Expected: The `Module` with the name `CS2100` is edited to have the name `CS2106`. 
   
   3. Test case: `edit module m/NON_EXISTENT_MODULE mn/NEW_MODULE_NAME`<br>
      Expected: No `Module` is deleted. Error details shown in the status message.
   
   4. Other incorrect edit module commands to try: `edit module`, `edit lesson`, `...`<br>
      Expected: Similar to previous.

### Deleting a module

1. Deleting a module while all modules are being shown.

   1. Prerequisites: List all modules using the `home` command. At least 1 `Module` is in the TAB.

   2. Test case: `delete module m/CS2103`<br>
     Expected: The `Module` with the name `CS2103` is deleted from the list of modules.

   3. Test case: `delete module m/NON_EXISTENT_MODULE`<br>
     Expected: No `Module` is deleted. Error details shown in the status message.

   4. Other incorrect delete module commands to try: `delete module`, `delete tutor`, `...`<br>
     Expected: Similar to previous.

### Adding a student

1. Adding a student to a module while all modules are being shown.

    1. Prerequisites: List all modules using the `home` command. At least 1 `Module` is in the TAB.

    2. Test case: `add student m/CS2103 i/A0123456A n/John Doe t/@johndoe e/john.doe@u.nus.edu`<br>
       Expected: The `Student` with the student id `A0123456A` is added to the `Module` `CS2103`.

    3. Test case: `add student m/NON_EXISTENT_MODULE i/A0123456A n/John Doe t/@johndoe e/john.doe@u.nus.edu`<br>
       Expected: No `Student` is added. Error details shown in the status message.

    4. Other incorrect add student commands to try: `add student i/A0123456A n/John Doe`, `add tutee m/CS2103 i/A0123456A`, `...`<br>
       Expected: Similar to previous.

### Editing a student

1. Editing a student while all modules are being shown.

    1. Prerequisites: List all modules using the `home` command. At least 1 `Module` is in the TAB and that `Module` has at least 1 `Student`.

    2. Test case: `edit student m/CS2103 i/A0123456A n/John Wick`<br>
       Expected: The `Student` in `Module` `CS2103` with the student id `A0123456A` is edited to have the name `John Wick`.

    3. Test case: `edit student m/CS2103 i/NON_EXISTENT_STUDENT_ID n/Johnny English`<br>
       Expected: No `Student` is deleted. Error details shown in the status message.

    4. Other incorrect edit student commands to try: `edit student`, `edit agent`, `...`<br>
       Expected: Similar to previous.

<div style="page-break-after: always;"></div>

### Deleting a student

1. Deleting a student while all modules are being shown.

    1. Prerequisites: List all modules using the `home` command. At least 1 `Module` is in the TAB and that `Module` has at least 1 `Student`.

    2. Test case: `delete student m/CS2103 i/A0123456A`<br>
       Expected: The `Student` with the student id `A0123456A` is deleted from the `Module` `CS2103`.

    3. Test case: `delete student m/NON_EXISTENT_STUDENT_ID`<br>
       Expected: No `Student` is deleted. Error details shown in the status message.

    4. Other incorrect delete student commands to try: `delete student`, `delete person i/A0123456A`, `...`<br>
       Expected: Similar to previous.
    
### Finding a student

1. Finding the information of a specific student in a specific module.

    1. Prerequisites: List all modules using the `home` command. At least 1 `Module` is in the list and that `Module` contains at least 1 `Student`.

    2. Test case: `find m/CS2103 i/A1234567A`<br>
       Expected: The `Student` with student id `A1234567A` in the `Module` `CS2103` will be chosen and displayed in TAB.

    3. Test case: `find m/CS2103 i/NON_EXISTING_STUDENT_ID`<br>
       Expected: No `Student` will be chosen and displayed in TAB. Error details shown in the status message.

    4. Other incorrect find commands to try: `lookup`, `search`, `...`<br>
       Expected: Similar to previous.

<div style="page-break-after: always;"></div>

### Going to the homepage

1. Going to the homepage of TAB from anywhere in the application.

    1. Prerequisites: The TAB application is running.

    2. Test case: `home`<br>
       Expected: The home page of TAB will be shown, along with all the data currently in TAB.

    3. Other incorrect home commands to try: `list`, `back`, `...`<br>
       Expected: Similar to previous.

### Adding a task

1. Adding a task to a module (and all its students) while all modules are being shown.

    1. Prerequisites: List all modules using the `home` command. At least 1 `Module` is in the TAB. The task will still be added if there are no `Student`s in the `Module`, but the change will only be visible after a `Student` has been added.

    2. Test case: `add task m/CS2103 ti/T10 a/assignment1 d/2021-10-12`<br>
       Expected: The `Task` with the task id `T10`, name `assignment1` and the deadline `Tue, Oct 12 2021` is added to the `Module` `CS2103` and all the `Students` in that module.

    3. Test case: `add task m/NON_EXISTENT_MODULE ti/T10 a/assignment1 d/2021-10-12`<br>
       Expected: No `Task` is added. Error details shown in the status message.

    4. Other incorrect add task commands to try: `add task m/CS2103 a/task with no id`, `add assignment`, `...`<br>
       Expected: Similar to previous.

### Editing a task

1. Editing a task of a module (and all its students) while all modules are being shown.

    1. Prerequisites: List all modules using the `home` command. At least 1 `Module` is in the TAB and that `Module` has at least 1 `Task`. The task will still be edited if there are no `Student`s in the `Module`, but the change will only be visible after a `Student` has been added.

    2. Test case: `edit task m/CS2103 ti/T10 a/Final exam d/2021-11-23`<br>
     Expected: The `Task` in `Module` `CS2103` with the task id `T10` is edited to have the name `Final exam` and deadline `Tue, Nov 23 2021`.

    3. Test case: `edit task m/CS2103 ti/NON_EXISTENT_TASK_ID a/Practical exam`<br>
     Expected: No `Task` is deleted. Error details shown in the status message.

    4. Other incorrect edit task commands to try: `edit exam`, `edit task m/NON_EXISTENT_MODULE`, `...`<br>
     Expected: Similar to previous.

### Deleting a task

1. Deleting a task from a module (and from all its students) while all modules are being shown.

    1. Prerequisites: List all modules using the `home` command. At least 1 `Module` is in the TAB and that `Module` has at lest 1 `Task`. The task will still be deleted if there are no `Student`s in the `Module`, but the change will only be visible after a `Student` has been added.

    2. Test case: `delete task m/CS2103 ti/T1`<br>
       Expected: The `Task` with the task id `T1` is deleted from the `Module` `CS2103`.

    3. Test case: `delete task m/NON_EXISTENT_MODULE`<br>
       Expected: No `Task` is deleted. Error details shown in the status message.

    4. Other incorrect delete task commands to try: `delete assignment`, `delete task m/CS2103 i/NON_EXISTENT_TASK_ID`, `...`<br>
       Expected: Similar to previous.

### Marking a task as done

1. Marking a task from a student in a particular module as done (`Completed`).

   1. Prerequisites: List all modules using the `home` command. The `Task` being marked done must belong to a `Student` in a `Module` in TAB and the `Task` must be `Incomplete`.
   
   2. Test case: `mark done m/CS2103 i/A1234567A ti/T1`<br>
      Expected: The `Task` with the task id `T1` is marked as `Completed` for `Student` with student id `A1234567A` of `Module` `CS2103`.
   
   3. Test case: `mark done m/CS2103 i/A1234567A ti/T1` (again, after the task is already marked as `Completed`)<br>
      Expected: No `Task` is marked as `Completed`. Error details shown in the status message.
   
   4. Other incorrect mark done commands to try: `mark finished`, `mark done m/NON_EXISTENT_MODULE i/NON_EXISTENT_STUDENT_ID ti/NON_EXISTENT_TASK_ID`, `...`<br>
      Expected: Similar to previous.

### Marking a task as undone

1. Marking a task from a student in a particular module as undone (`Incomplete`).

    1. Prerequisites: List all modules using the `home` command. The `Task` being marked undone must belong to a `Student` in a `Module` in TAB and the `Task` must be `Completed`.

    2. Test case: `mark undone m/CS2103 i/A1234567A ti/T1`<br>
       Expected: The `Task` with the task id `T1` is marked as `Incomplete` for `Student` with student id `A1234567A` of `Module` `CS2103`.

    3. Test case: `mark undone m/CS2103 i/A1234567A ti/T1` (again, after the task is already marked as `Incomplete`)<br>
       Expected: No `Task` is marked as `Completed`. Error details shown in the status message.
   
    4. Other incorrect mark undone commands to try: `mark unfinished`, `mark undone m/NON_EXISTENT_MODULE i/NON_EXISTENT_STUDENT_ID ti/NON_EXISTENT_TASK_ID`, `...`<br>
       Expected: Similar to previous.

### Clearing all information

1. Clearing all information from TAB using the `clear` command.

   1. Prerequisites: The TAB application is running.
   
   2. Test case: `clear` <br>
      Expected: All information in TAB will be cleared. If TAB has no information, the successful status message will still be shown.
   
   3. Other incorrect clear commands to try: `reset`, `refresh`, `...`<br>
      Expected: Similar to previous.
   
### Exiting the application

1. Exiting the application using the `exit` command.

   1. Prerequisites: The TAB application is running.
   
   2. Test case: `exit`<br>
      Expected: The TAB application will exit.

   3. Other incorrect exit commands to try: `bye`, `quit`, `logout`, `...`<br>
      Expected: The TAB application will not exit. Error details shown in ths status message
<div style="page-break-after: always;"></div>

------------------------------------------

## **Appendix: Effort**

With AB3 as the baseline of comparison, our project is much more demanding. 
AB3, as an address book application that manages contact details, has only one model which is a Person. 
For our project which is building a helper for teaching assistants, there are three models, namely, Modules, Students, and Tasks.

This created some problems when we were designing the UI, including how to arrange these three types of objects, and what their relationship would be. 
A meeting was held, during which we used Miro to come up with wireframes and prototypes and conducted discussions to finalise our design. 
Although it was harder to manage more than one type of object, we all embraced this challenge and successfully rendered a working product.

Another challenge we faced was more specific. When we added in students and tasks to TAB, tasks under some students were not shown.
And this bug was inconsistent - sometimes we could not reproduce it even when following the exact steps.
We initially considered it as a UI bug and wasn't sure how to go about solving it. But we did not give up and leave it unattended.
After much effort, one of us discovered that these missing tasks were not added to their corresponding students in time.
Following this lead, another team member located to code that was causing this issue and fixed it.
With hard work and perseverance, we managed to solve something previously thought of as unsolvable.

Other challenges we had included dealing with testing and storage. For many of us, this was the first time handling these aspects of coding.
It was not easy, especially for those members who volunteered as trailblazers and set the standard for writing test cases and storage-related logic. 
Kudos to them!

Overall, this project posed many difficulties and challenges that we had to overcome.
To many of us, this was the first collaborative software engineering project that involved facets that were once unfamiliar, such as testing, UI and storage.
It would be an understatement to say that it was not easy.
Nevertheless, we, together as a team, rose above all these obstacles and passed this trial of diligence, determination, resourcefulness, and teamwork.
