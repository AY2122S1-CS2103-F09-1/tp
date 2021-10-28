package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Adds a module, a student or a task to the TAB.
 */
public abstract class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_SUCCESS = "New %1$s added: %2$s";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Add either a module, a student or a task\n"
            + "Parameters: Add_Type (can be module, student or task)\n"
            + "Example: " + COMMAND_WORD + " module m/CS2103";

    @Override
    public abstract CommandResult execute(Model model) throws CommandException;

    @Override
    public abstract boolean equals(Object other);
}
