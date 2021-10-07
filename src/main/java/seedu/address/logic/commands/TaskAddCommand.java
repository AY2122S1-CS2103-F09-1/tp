package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Changes the remark of an existing person in the address book.
 */
public class TaskAddCommand extends Command {

    public static final String COMMAND_WORD = "task add";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult("Hello from task add");
    }
}
