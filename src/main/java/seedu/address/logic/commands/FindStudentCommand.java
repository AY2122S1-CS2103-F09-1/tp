package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_FIND_STUDENT_SUCCESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULE_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_ID;

import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.module.Module;
import seedu.address.model.module.ModuleName;
import seedu.address.model.module.ModuleNameEqualsKeywordsPredicate;
import seedu.address.model.module.student.Student;
import seedu.address.model.module.student.StudentId;
import seedu.address.model.module.student.StudentIdEqualsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindStudentCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds student with the student ID from a module.\n"
            + "Parameters: "
            + PREFIX_MODULE_NAME + "MODULE NAME "
            + PREFIX_STUDENT_ID + "STUDENT ID "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_MODULE_NAME + "CS2103 "
            + PREFIX_STUDENT_ID + "A1234567A ";

    private final StudentId studentId;
    private final ModuleName moduleName;

    /**
     * Finds a student in TAB using the student's ID.
     *
     * @param moduleName The module of the student to be found.
     * @param studentId The student ID of the student to be found.
     */
    public FindStudentCommand(ModuleName moduleName, StudentId studentId) {
        this.moduleName = moduleName;
        this.studentId = studentId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Module> lastShownList = model.getFilteredModuleList();

        for (Module module : lastShownList) {
            if (module.getName().equals(moduleName)) {
                String[] moduleNameKeywords = new String[]{moduleName.moduleName};
                ModuleNameEqualsKeywordsPredicate predicate =
                        new ModuleNameEqualsKeywordsPredicate(Arrays.asList(moduleNameKeywords));
                model.updateFilteredModuleList(predicate);
                findStudentFromModule(module);
                return new CommandResult(String.format(MESSAGE_FIND_STUDENT_SUCCESS, studentId));
            }
        }
        throw new CommandException(String.format(Messages.MESSAGE_MODULE_NAME_NOT_FOUND, moduleName.moduleName));
    }

    /**
     * Find a student from the specified module.
     *
     * @param module The module the student will be searched from.
     * @throws CommandException Exception thrown when student is not found.
     */
    public void findStudentFromModule(Module module) throws CommandException {
        List<Student> studentList = module.getFilteredStudentList();
        for (Student student : studentList) {
            if (student.getStudentId().equals(studentId)) {
                StudentIdEqualsKeywordsPredicate predicate =
                        new StudentIdEqualsKeywordsPredicate(studentId.value);
                module.updateFilteredStudentList(predicate);
                return;
            }
        }
        throw new CommandException(String.format(Messages.MESSAGE_STUDENT_NOT_FOUND, studentId));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindStudentCommand // instanceof handles nulls
                && studentId.equals(((FindStudentCommand) other).studentId)
                && moduleName.equals(((FindStudentCommand) other).moduleName)); // state check
    }
}
