package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULE_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_ID;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.module.Module;
import seedu.address.model.module.ModuleName;
import seedu.address.model.module.student.Student;
import seedu.address.model.module.student.StudentId;

/**
 * Deletes a student identified using it's displayed index from the address book.
 */
public class DeleteStudentCommand extends DeleteCommand {

    public static final String COMMAND_WORD = "delete student";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the student identified by the student id from the module specified.\n"
            + "Parameters: "
            + PREFIX_MODULE_NAME + "MODULE NAME "
            + PREFIX_STUDENT_ID + "STUDENT ID\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_MODULE_NAME + "CS2103 "
            + PREFIX_STUDENT_ID + "A1234567A ";

    public static final String MESSAGE_DELETE_STUDENT_SUCCESS = "Deleted student: %1$s";

    private static Logger logger = Logger.getLogger("Delete Student Logger");

    private StudentId studentId;
    private ModuleName moduleName;

    /**
     * Deletes a student identified using its student ID from a module.
     */
    public DeleteStudentCommand(StudentId studentId, ModuleName moduleName) {
        this.studentId = studentId;
        this.moduleName = moduleName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Module> lastShownList = model.getFilteredModuleList();

        for (Module module : lastShownList) {
            if (module.getName().equals(moduleName)) {
                return deleteStudentFromModule(module);
            }
        }
        throw new CommandException(String.format(Messages.MESSAGE_MODULE_NAME_NOT_FOUND, moduleName.getModuleName()));
    }

    /**
     * Deletes the student from the specified module.
     *
     * @param module The module the student will be deleted from.
     * @return Statement indicating that the deletion is successful.
     * @throws CommandException Exception thrown when student is not found.
     */
    public CommandResult deleteStudentFromModule(Module module) throws CommandException {
        List<Student> studentList = module.getFilteredStudentList();
        for (Student student : studentList) {
            if (student.getStudentId().equals(studentId)) {
                logger.log(Level.INFO, "deleting student: " + student.getStudentId());
                module.removeStudent(student);
                return new CommandResult(String.format(MESSAGE_DELETE_STUDENT_SUCCESS, studentId));
            }
        }
        throw new CommandException(String.format(Messages.MESSAGE_STUDENT_NOT_FOUND, studentId));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteStudentCommand // instanceof handles nulls
                && studentId.equals(((DeleteStudentCommand) other).studentId)); // state check
    }
}
