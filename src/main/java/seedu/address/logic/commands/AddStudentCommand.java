package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULE_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELE_HANDLE;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.module.Module;
import seedu.address.model.module.ModuleName;
import seedu.address.model.module.student.Student;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskDeadline;
import seedu.address.model.task.TaskId;
import seedu.address.model.task.TaskName;
import seedu.address.model.task.UniqueTaskList;

/**
 * Adds a student to the address book.
 */
public class AddStudentCommand extends AddCommand {

    public static final String COMMAND_WORD = "add student";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a student to a module.\n"
            + "Parameters: "
            + PREFIX_MODULE_NAME + "MODULE NAME "
            + PREFIX_STUDENT_ID + "STUDENT ID "
            + PREFIX_NAME + "NAME "
            + PREFIX_TELE_HANDLE + "TELE HANDLE "
            + PREFIX_EMAIL + "EMAIL\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_MODULE_NAME + "CS2103 "
            + PREFIX_STUDENT_ID + "A1234567A "
            + PREFIX_NAME + "John Doe "
            + PREFIX_TELE_HANDLE + "@johndoe "
            + PREFIX_EMAIL + "johnd@example.com ";

    public static final String MESSAGE_ADD_STUDENT_SUCCESS = "New student added to the module: %1$s";
    public static final String MESSAGE_DUPLICATE_STUDENT = "This student already exists in the module";

    private final Student studentToAdd;
    private ModuleName moduleName;

    /**
     * Creates an AddCommand to add the specified {@code Student}
     *
     * @param student The student to be added to a module.
     * @param moduleName The name of the module the student will be added to.
     */
    public AddStudentCommand(Student student, ModuleName moduleName) {
        requireNonNull(student);
        studentToAdd = student;
        this.moduleName = moduleName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Module> lastShownList = model.getFilteredModuleList();
        Module module;
        for (Module mod : lastShownList) {
            if (mod.getName().equals(moduleName)) {
                module = mod;
                if (module.hasStudent(studentToAdd)) {
                    throw new CommandException(MESSAGE_DUPLICATE_STUDENT);
                }
                module.addStudent(studentToAdd);
                // for each task in this module's taskList, add it to a new UniqueTaskList
                // give the new UniqueTaskList to student after all tasks have been added
                UniqueTaskList thisModuleTaskList = module.getTaskList();
                UniqueTaskList newStudentTaskList = new UniqueTaskList();
                for (Task task : thisModuleTaskList) {
                    ModuleName moduleName = task.getTaskModuleName();
                    TaskId taskId = task.getTaskId();
                    TaskName taskName = task.getTaskName();
                    TaskDeadline taskDeadline = task.getTaskDeadline();
                    Task taskToAdd = new Task(moduleName, taskId, taskName, taskDeadline);
                    newStudentTaskList.add(taskToAdd);
                }
                studentToAdd.setTaskList(newStudentTaskList);
                return new CommandResult(String.format(MESSAGE_ADD_STUDENT_SUCCESS, studentToAdd));
            }
        }
        throw new CommandException(String.format(Messages.MESSAGE_MODULE_NAME_NOT_FOUND, moduleName.getModuleName()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddStudentCommand // instanceof handles nulls
                && studentToAdd.equals(((AddStudentCommand) other).studentToAdd));
    }
}
