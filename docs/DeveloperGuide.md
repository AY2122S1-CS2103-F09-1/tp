---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/se-edu/addressbook-level3/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

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

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2122S1-CS2103-F09-1/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2122S1-CS2103-F09-1/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `AddressBookParser` class to parse the user command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add a person).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2122S1-CS2103-F09-1/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2122S1-CS2103-F09-1/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in json format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Delete Module

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


### Edit Module

1. When the `Logic` is called upon to execute `edit module m/CS2103 n/CS2105`, it uses the `TeachingAssistantBuddyParser` class to parse the user command.
2. The `TeachingAssistantBuddyParser` parses the first command word `edit`, and pass the rest of the input to a `EditCommandParser`.
3. The `EditCommandParser` then figures out the type of object to edit, in this case a `Module` object as indicated by `module`.
4. The `EditModuleCommandParser` will parse the tokens `m/<old module name>` and `n/<new module name>` and create a `EditModuleDescriptor`
   (defined in EditModuleCommand) with the new module name.
5. `EditModuleCommandParser` will also create a `EditModuleCommand` with the `EditModuleDescriptor` and the module to edit, which is executed by the `LogicManager`
6. The `EditModuleCommand` communicates with the `Model` when it is executed.
7. The `Model` will look for a `Module` with the specified name and edit the name.
8. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.

### Add Module

1. When the `Logic` is called upon to execute `add module m/CS2103`, it uses the `TeachingAssistantBuddyParser` class to parse the user command.
2. The `TeachingAssistantBuddyParser` parses the first command word `add`, and pass the rest of the input to a `AddCommandParser`.
3. The `AddCommandParser` then figures out the type of object to add, in this case a `Module` object as indicated by `module`.
4. The `AddModuleCommandParser` will wrap the module name in a `ModuleName` object and pass it into a `AddModuleCommand`.
5. This results in a `AddModuleCommand` object (which is a subclass of `AddCommand`), which is executed by the `Logic Manager`.
6. The `AddModuleCommand` communicates with the `Model` when it is executed.
7. The `Model` will add a new `Module` with the new `ModuleName`. 
8. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.

### Add Student

1. When the `Logic` is called upon to execute `add student m/CS2103 i/A1234567A n/Amy bee t/@amybee e/amybee@gmail.com`, it uses the `TeachingAssistantBuddyParser` class to parse the user command.
2. The `TeachingAssistantBuddyParser` parses the first command word `add`, and pass the rest of the input to a `AddCommandParser`.
3. The `AddCommandParser` then figures out the type of object to add, in this case a `Student` object as indicated by `student`.
4. The `AddStudentCommandParser` will wrap the module name in a `ModuleName` object and the student's information in a `Student` object and pass it into a `AddStudentCommand`.
5. This results in a `AddStudentCommand` object (which is a subclass of `AddCommand`), which is executed by the `Logic Manager`.
![How the command `add student m/CS2103 i/A1234567A n/Amy bee t/@amybee e/amybee@gmail.com` is parsed in the Logic component](images/AddStudentSequenceDiagram1.png)
6. The `AddStudentCommand` communicates with the `Model` when it is executed.
7. The `Model` will look for a `Module` with the specified `ModuleName`.
8. The `Module` with the specified `ModuleName` will then add the new `Student` object to its student list.
9. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.
![How the `AddStudentCommand` is executed](images/AddStudentSequenceDiagram2.png)

### Delete Student

1. When the `Logic` is called upon to execute `delete student m/CS2103 i/A1234567A`, it uses the `TeachingAssistantBuddyParser` class to parse the user command.
2. The `TeachingAssistantBuddyParser` parses the first command word `delete`, and pass the rest of the input to a `DeleteCommandParser`.
3. The `DeleteCommandParser` then figures out the type of object to delete, in this case a `Student` object as indicated by `student`.
4. The `DeleteStudentCommandParser` will wrap the module name in a `ModuleName` object and the student ID in a `StudentId` object and pass them into a `DeleteStudentCommand`.
5. This results in a `DeleteStudentCommand` object (which is a subclass of `DeleteCommand`), which is executed by the `Logic Manager`.
![How the command `delete student m/CS2103 i/A1234567A` is parsed in the Logic component](images/DeleteStudentSequenceDiagram1.png)
6. The `DeleteStudentCommand` communicates with the `Model` when it is executed.
7. The `Model` will look for a `Module` with the specified `ModuleName`.
8. The `Module` with the specified `ModuleName` will then look for the student with the specified `StudentId` and delete the student.
9. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.
![How the `DeleteStudentCommand` is executed](images/DeleteStudentSequenceDiagram2.png)

### Edit Student

1. When `Logic` is called upon to execute the user input `edit student m/CS2103 i/A1234567A n/Amy Lee`, it uses the
   `TeachingAssistantBuddyParser` class to parse the user command.
2. `TeachingAssistantBuddyParser` parses the first command word `edit`, and pass the rest of the input to `EditCommandParser`.
3. The `EditCommandParser` then figures out the type of object to edit, in this case a `Student` object as indicated by `student`.
4. The `EditStudentCommandParser` will then parse the tokens, `m/CS2103`, `i/A1234567A` and `n/Amy Lee` in this case, to determine the moduleName (`CS2103`in this case),
   and to create an `EditStudentDescriptor` with the student ID `A1234567A` and the name `Amy Lee`, and pass these `ModuleName` and `EditStudentDescriptor` objects into an `EditStudentCommand`.
5. This results in a `EditStudentCommand` object (which is a subclass of `EditCommand`), which is executed by the `Logic
   Manager`.
![How the command `edit student m/CS2103 i/A1234567A n/Amy Lee` is parsed in the Logic component](images/EditStudentSequenceDiagram1.png)
6. The `EditStudentCommand` communicates with the `Model` when it is executed.
7. The `Model` will look for the `Student` with the specified student id, `A1234567A` in this case,  inside the `Module` specified by
   the module name, `CS2103` in this case, and then replacing the old editable fields (such as student name, tele handle and email)
   of that `Student` using any optionally provided new fields.
8. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.
![How the `EditStudentCommand` is executed](images/EditStudentSequenceDiagram2.png)

### Find Student

1. When the `Logic` is called upon to execute `find m/CS2103 i/A1234567A`, it uses the `TeachingAssistantBuddyParser` class to parse the user command.
2. The `TeachingAssistantBuddyParser` parses the first command word `find`, and pass the rest of the input to a `FindStudentCommandParser`.
3. The `FindStudentCommandParser` will wrap the module name in a `ModuleName` object and the student's ID in a `StudentId` object and pass it into a `FindStudentCommand`.
4. This results in a `FindStudentCommand` object, which is executed by the `Logic Manager`.
![How the command `find m/CS2103 i/A1234567A` is parsed in the Logic component](images/FindStudentSequenceDiagram1.png)
5. The `FindStudentCommand` communicates with the `Model` when it is executed.
6. The `Model` will look for a `Module` with the specified `ModuleName`.
7. The `Module` with the specified `ModuleName` will then look for the `Student` with the specified `StudentId` and update the module's filtered student's list to only contain this specific student.
8. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.
![How the `FindStudentCommand` is executed](images/FindStudentSequenceDiagram2.png)

### Add Task

1. When `Logic` is called upon to execute the user input `add task m/CS2103 ti/T1 a/assignment1 d/20/10/2021`, it uses the 
   `TeachingAssistantBuddyParser` class to parse the user command.
2. `TeachingAssistantBuddyParser` parses the first command word `add`, and pass the rest of the input to `AddCommandParser`.
3. `AddCommandParser` then figures out the type of object to add, in this case a `task` object as indicated by the 
   keyword `task`, and pass the rest of the arguments to `AddTaskCommandParser`.
4. `AddTaskCommandParser` will create a `moduleName` object, a `taskName` object, and a `taskDeadline` object based on 
   the input arguments. They are then passed to `AddTaskCommand`.
5. This results in a `AddTaskCommand` object (which is a subclass of `AddCommand`), which is executed by the `Logic 
   Manager`.
6. The `AddTaskCommand` communicates with the `Model` when it is executed.
7. The `Model` will add a new `Task` with the new `taskName` under the `Module` with that `moduleName`.
8. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.

### Edit Task

1. When `Logic` is called upon to execute the user input `edit task m/CS2103 ti/T1 a/final exam`, it uses the
   `TeachingAssistantBuddyParser` class to parse the user command.
2. `TeachingAssistantBuddyParser` parses the first command word `edit`, and pass the rest of the input to `EditCommandParser`.
3. The `EditCommandParser` then figures out the type of object to edit, in this case a `Task` object as indicated by `task`.
4. The `EditTaskCommandParser` will then parse the tokens, `m/CS2103` and `ti/T1` in this case, to determine the module (`CS2103`in this case), 
   and the taskId (`T1` in this case) of the task that is being edited, and pass the rest of the tokens to `EditTaskCommand`.
5. The `EditTaskCommand` create an `EditTaskDescriptor` object (defined in EditTaskCommand), using the given `moduleName` object, 
   `taskId` object, and any other tokens passed from `EditTaskCommandParser` representing the newly updated fields of the task object.
6. This results in a `EditTaskCommand` object (which is a subclass of `EditCommand`), which is executed by the `Logic
   Manager`.
7. The `EditTaskCommand` communicates with the `Model` when it is executed.
8. The `Model` will look for the `Task` with the specified task id, `T1` in this case,  inside the `Module` specified by
   the module name, `CS2103` in this case, and then replacing the old editable fields (such as task name, task deadline) 
   of that `Task` using any optionally provided new fields.
9. The `Model` will look for the `Task` with the specified task id, `T1` in this case, for every `Student` in the `Module` specified by
   the module name, `CS2103` in this case, and replacing the old editable fields of (such as task name, task deadline) 
   of that `Task` using any optionally provided new fields.
10. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.

### Delete Task

1. When the `Logic` is called upon to execute `delete task m/CS2103 ti/T1`, it uses the `TeachingAssistantBuddyParser` class to parse the user command.
2. The `TeachingAssistantBuddyParser` parses the first command word `delete`, and pass the rest of the input to a `DeleteCommandParser`.
3. The `DeleteTaskCommandParser` then figures out the type of object to delete, in this case a `Task` object as indicated by `task`.
4. The `DeleteStudentCommandParser` will wrap the module name in a `ModuleName` object and the task ID in a `TaskId` object and pass them into a `DeleteTaskCommand`.
5. This results in a `DeleteTaskCommand` object (which is a subclass of `DeleteCommand`), which is executed by the `Logic Manager`.
6. The `DeleteTaskCommand` communicates with the `Model` when it is executed.
7. The `Model` will look for a `Module` with the specified `ModuleName`.
8. The `Module` with the specified `ModuleName` will then look for the task with the specified `TaskId` and delete the task.
9. The `Module` will also iterate through its `Student` list and delete the specified `Task` from all the students.
10. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

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

*{More to be added}*

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

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2. Should be able to respond to a user command within 2 seconds.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Should be able to handle up to 100 students without significant lag in response time.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Module**: An academic module or subject that is frequently found in universities and institutes of higher learning

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
