package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULE_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELE_HANDLE;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.Messages;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.module.Module;
import seedu.address.model.module.ModuleName;
import seedu.address.model.module.student.Email;
import seedu.address.model.module.student.Name;
import seedu.address.model.module.student.Student;
import seedu.address.model.module.student.StudentId;
import seedu.address.model.module.student.TeleHandle;
import seedu.address.model.task.UniqueTaskList;

/**
 * Edits a student's information.
 */
public class EditStudentCommand extends EditCommand {

    public static final String COMMAND_WORD = "edit student";

    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits a student's information. Must provide at least "
            + "one field (name/tele handle/email) to be edited. "
            + "Parameters: "
            + PREFIX_MODULE_NAME + "MODULE NAME "
            + PREFIX_STUDENT_ID + "STUDENT ID "
            + PREFIX_NAME + "EDITED NAME (OR) "
            + PREFIX_TELE_HANDLE + "EDITED TELE HANDLE (OR) "
            + PREFIX_EMAIL + "EDITED EMAIL\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_MODULE_NAME + "CS2103 "
            + PREFIX_STUDENT_ID + "A1234567A "
            + PREFIX_NAME + "John Doe "
            + PREFIX_TELE_HANDLE + "@johndoe "
            + PREFIX_EMAIL + "johnd@example.com "
            + "(edits all fields)";

    private EditStudentDescriptor editStudentDescriptor;
    private ModuleName moduleName;

    /**
     * Creates an EditStudentCommand to edit the specified {@code Student}
     *
     * @param moduleName The name of the module the student is registered in.
     * @param editStudentDescriptor The edited student descriptor.
     */
    public EditStudentCommand(ModuleName moduleName, EditStudentDescriptor editStudentDescriptor) {
        requireNonNull(editStudentDescriptor);
        requireNonNull(moduleName);
        this.editStudentDescriptor = editStudentDescriptor;
        this.moduleName = moduleName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Module> lastShownList = model.getFilteredModuleList();
        for (Module module : lastShownList) {
            if (module.getName().equals(moduleName)) {
                return editStudentInformation(module);
            }
        }
        throw new CommandException(String.format(Messages.MESSAGE_MODULE_NAME_NOT_FOUND, moduleName.moduleName));
    }

    /**
     * Edits a student's information. the student from the specified module.
     *
     * @param module The module the student will be deleted from.
     * @return Statement indicating that the deletion is successful.
     * @throws CommandException Exception thrown when student is not found.
     */
    public CommandResult editStudentInformation(Module module) throws CommandException {
        List<Student> studentList = module.getFilteredStudentList();
        for (Student student : studentList) {
            if (student.getStudentId().equals(editStudentDescriptor.studentId)) {
                editStudentDescriptor.setUniqueTaskList(student.getTaskList());
                Student editedStudent = createEditedStudent(student, editStudentDescriptor);

                module.setStudent(student, editedStudent);
                return new CommandResult(String.format(Messages.MESSAGE_EDIT_STUDENT_SUCCESS, student.getStudentId()));
            }
        }
        throw new CommandException(String.format(Messages.MESSAGE_STUDENT_NOT_FOUND, editStudentDescriptor.studentId));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Student createEditedStudent(Student studentToEdit, EditStudentDescriptor editPersonDescriptor) {
        assert studentToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(studentToEdit.getName());
        TeleHandle updatedTeleHandle = editPersonDescriptor.getTeleHandle().orElse(studentToEdit.getTeleHandle());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(studentToEdit.getEmail());
        StudentId updatedStudentId = editPersonDescriptor.getStudentId().orElse(studentToEdit.getStudentId());
        UniqueTaskList uniqueTaskList = editPersonDescriptor.uniqueTaskList;

        Student editedStudent = new Student(updatedStudentId, updatedName, updatedTeleHandle, updatedEmail);
        editedStudent.setTaskList(uniqueTaskList);
        return editedStudent;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditStudentCommand)) {
            return false;
        }

        // state check
        EditStudentCommand e = (EditStudentCommand) other;
        return moduleName.equals(e.moduleName)
                && editStudentDescriptor.equals(e.editStudentDescriptor);
    }

    /**
     * Stores the details to edit the student with. Each non-empty field value will replace the
     * corresponding field value of the student.
     */
    public static class EditStudentDescriptor {
        private Name name;
        private TeleHandle teleHandle;
        private Email email;
        private StudentId studentId;
        private UniqueTaskList uniqueTaskList;

        public EditStudentDescriptor() {}

        /**
         * Copy constructor.
         *
         * @param  toCopy The edit student descriptor to be copied.
         */
        public EditStudentDescriptor(EditStudentDescriptor toCopy) {
            setName(toCopy.name);
            setStudentId(toCopy.studentId);
            setEmail(toCopy.email);
            setTeleHandle(toCopy.teleHandle);
            setUniqueTaskList(toCopy.uniqueTaskList);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, teleHandle, email, studentId);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setTeleHandle(TeleHandle teleHandle) {
            this.teleHandle = teleHandle;
        }

        public Optional<TeleHandle> getTeleHandle() {
            return Optional.ofNullable(teleHandle);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setStudentId(StudentId studentId) {
            this.studentId = studentId;
        }

        public Optional<StudentId> getStudentId() {
            return Optional.ofNullable(studentId);
        }

        public void setUniqueTaskList(UniqueTaskList uniqueTaskList) {
            this.uniqueTaskList = uniqueTaskList;
        }

        public Optional<UniqueTaskList> getUniqueTaskList() {
            return Optional.ofNullable(uniqueTaskList);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditStudentDescriptor)) {
                return false;
            }

            // state check
            EditStudentDescriptor e = (EditStudentDescriptor) other;

            return getName().equals(e.getName())
                    && getStudentId().equals(e.getStudentId())
                    && getEmail().equals(e.getEmail())
                    && getTeleHandle().equals(e.getTeleHandle());
        }
    }
}
