package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULE_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK_NAME;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.core.Messages;
import seedu.address.commons.util.CollectionUtil;
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
 * Edits a task's information.
 */
public class EditTaskCommand extends EditCommand {

    public static final String COMMAND_WORD = "edit task";

    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits a task's information. Must provide at least "
            + "one editable field (name/deadline) to be edited.\n"
            + "Parameters: "
            + PREFIX_MODULE_NAME + "MODULE NAME "
            + PREFIX_TASK_ID + "TASK ID "
            + PREFIX_TASK_NAME + "TASK NAME "
            + PREFIX_TASK_DEADLINE + "TASK DEADLINE\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_MODULE_NAME + "CS2103 "
            + PREFIX_TASK_ID + "T1 "
            + PREFIX_TASK_NAME + "V13 "
            + PREFIX_TASK_DEADLINE + "21 Oct 2021 "
            + "(edits all fields)\n"
            + "You may omit editable fields that are not being edited.";

    private static Logger logger = Logger.getLogger("Edit Task Logger");

    private EditTaskDescriptor editTaskDescriptor;
    private ModuleName moduleName;

    /**
     * Creates an EditTaskCommand to edit the specified {@code Task}
     *
     * @param moduleName The name of the module for which the task is assigned.
     * @param editTaskDescriptor The edited task descriptor.
     */
    public EditTaskCommand(ModuleName moduleName, EditTaskDescriptor editTaskDescriptor) {
        requireAllNonNull(moduleName, editTaskDescriptor);
        this.editTaskDescriptor = editTaskDescriptor;
        this.moduleName = moduleName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Module> lastShownList = model.getFilteredModuleList();
        for (Module module : lastShownList) {
            if (module.getName().equals(moduleName)) {
                // right module reached
                List<Student> studentList = module.getFilteredStudentList();
                for (Student student : studentList) {
                    editTaskListOfStudent(student);
                }
                return editTaskListOfModule(module);
            }
        }
        throw new CommandException(String.format(Messages.MESSAGE_MODULE_NAME_NOT_FOUND, moduleName.getModuleName()));
    }

    /**
     * Edits a {@code Task}'s information. The {@code Task} will be from the specified {@code Student}.
     *
     * @param student The student whose task will be edited.
     * @return Statement indicating that the edit is successful.
     * @throws CommandException Exception thrown when task is not found.
     */
    public CommandResult editTaskListOfStudent(Student student) throws CommandException {
        requireNonNull(student);
        UniqueTaskList studentTaskList = student.getTaskList();
        for (Task task : studentTaskList) {
            if (task.getTaskId().equals(editTaskDescriptor.taskId)) {
                Task editedTask = createEditedTask(task, editTaskDescriptor);
                // replaces the old task with the editedTask
                logger.log(Level.INFO, "editing task: " + task.getTaskId()
                        + " of all students in module: " + task.getModuleNameString());
                studentTaskList.setTask(task, editedTask);
                return new CommandResult(String.format(Messages.MESSAGE_EDIT_TASK_SUCCESS, task.getTaskName()));
            }
        }
        throw new CommandException(String.format(Messages.MESSAGE_TASK_NOT_FOUND, editTaskDescriptor.taskId));
    }

    /**
     * Edits a {@code Task}'s information. The {@code Task} will be from the specified {@code Student}.
     *
     * @param module The student whose task will be edited.
     * @return Statement indicating that the edit is successful.
     * @throws CommandException Exception thrown when task is not found.
     */
    public CommandResult editTaskListOfModule(Module module) throws CommandException {
        requireNonNull(module);
        UniqueTaskList moduleTaskList = module.getTaskList();
        for (Task task : moduleTaskList) {
            if (task.getTaskId().equals(editTaskDescriptor.taskId)) {
                Task editedTask = createEditedTask(task, editTaskDescriptor);
                // replaces the old task with the editedTask
                logger.log(Level.INFO, "editing task: " + task.getTaskId()
                        + " of module: " + module.getName());
                moduleTaskList.setTask(task, editedTask);
                return new CommandResult(String.format(Messages.MESSAGE_EDIT_TASK_SUCCESS, task.getTaskName()));
            }
        }
        throw new CommandException(String.format(Messages.MESSAGE_TASK_NOT_FOUND, editTaskDescriptor.taskId));
    }

    /**
     * Creates and returns a {@code Task} with the details of {@code taskToEdit}
     * edited with {@code editTaskDescriptor}.
     *
     * @param taskToEdit The task to be edited.
     * @param editTaskDescriptor The edited task descriptions.
     * @return The edited task.
     */
    private static Task createEditedTask(Task taskToEdit, EditTaskDescriptor editTaskDescriptor) {
        assert taskToEdit != null;

        ModuleName moduleName = taskToEdit.getTaskModuleName();
        // taskId is not to be edited
        TaskId taskId = taskToEdit.getTaskId();
        TaskName updatedTaskName = editTaskDescriptor.getTaskName().orElse(taskToEdit.getTaskName());
        TaskDeadline updatedTaskDeadline = editTaskDescriptor.getTaskDeadline().orElse(taskToEdit.getTaskDeadline());
        boolean isComplete = taskToEdit.isComplete();

        Task editedTask = new Task(moduleName, taskId, updatedTaskName, updatedTaskDeadline, isComplete);
        return editedTask;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditTaskCommand)) {
            return false;
        }

        // state check
        EditTaskCommand e = (EditTaskCommand) other;
        return moduleName.equals(e.moduleName)
                && editTaskDescriptor.equals(e.editTaskDescriptor);
    }

    /**
     * Stores the details to edit the {@code Task} with. Each non-empty field value will replace the
     * corresponding field value of the {@code Task}.
     */
    public static class EditTaskDescriptor {
        private TaskId taskId;
        private TaskName taskName;
        private TaskDeadline taskDeadline;
        private boolean isComplete;

        public EditTaskDescriptor() {}

        /**
         * Copy constructor.
         *
         * @param  toCopy The edit task descriptor to be copied.
         */
        public EditTaskDescriptor(EditTaskDescriptor toCopy) {
            setTaskId(toCopy.taskId);
            setTaskName(toCopy.taskName);
            setTaskDeadline(toCopy.taskDeadline);
            setIsComplete(toCopy.isComplete);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(taskName, taskDeadline);
        }

        public void setTaskId(TaskId taskId) {
            this.taskId = taskId;
        }

        public Optional<TaskId> getTaskId() {
            return Optional.ofNullable(taskId);
        }

        public void setTaskName(TaskName taskName) {
            this.taskName = taskName;
        }

        public Optional<TaskName> getTaskName() {
            return Optional.ofNullable(taskName);
        }

        public void setTaskDeadline(TaskDeadline taskDeadline) {
            this.taskDeadline = taskDeadline;
        }

        public Optional<TaskDeadline> getTaskDeadline() {
            return Optional.ofNullable(taskDeadline);
        }

        public void setIsComplete(boolean isComplete) {
            this.isComplete = isComplete;
        }

        public Optional<Boolean> getIsComplete() {
            return Optional.ofNullable(isComplete);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditTaskDescriptor)) {
                return false;
            }

            // state check
            EditTaskDescriptor e = (EditTaskDescriptor) other;

            return getTaskName().equals(e.getTaskName())
                    && getTaskDeadline().equals(e.getTaskDeadline())
                    && getIsComplete().equals(e.getIsComplete());
        }
    }
}
