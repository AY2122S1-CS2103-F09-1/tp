package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULE_NAME;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.module.Module;

/**
 * Adds a student to the address book.
 */
public class AddModuleCommand extends AddCommand {

    public static final String COMMAND_WORD = "add module";
    public static final String COMMAND_TYPE = "module";

    public static final String MESSAGE_USAGE = AddCommand.COMMAND_WORD + " " + COMMAND_TYPE
            + ": Adds a module to TAB.\n"
            + "Parameters: "
            + PREFIX_MODULE_NAME + "MODULE NAME\n"
            + "Example: " + AddCommand.COMMAND_WORD + " " + COMMAND_TYPE + " "
            + PREFIX_MODULE_NAME + "CS2103 ";

    public static final String MESSAGE_SUCCESS = "New module added: %1$s";
    public static final String MESSAGE_DUPLICATE_MODULE = "This module already exists in TAB";

    private final Module toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Module}
     *
     * @param module The module to be added to TAB.
     */
    public AddModuleCommand(Module module) {
        requireNonNull(module);
        toAdd = module;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (model.hasModule(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_MODULE);
        }

        model.addModule(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddModuleCommand // instanceof handles nulls
                && toAdd.equals(((AddModuleCommand) other).toAdd));
    }
}
