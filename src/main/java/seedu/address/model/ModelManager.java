package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.student.Student;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final TeachingAssistantBuddy teachingAssistantBuddy;
    private final UserPrefs userPrefs;
    private final FilteredList<Student> filteredStudents;

    /**
     * Initializes a ModelManager with the given teachingAssistantBuddy and userPrefs.
     */
    public ModelManager(ReadOnlyTeachingAssistantBuddy tab, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(tab, userPrefs);

        logger.fine("Initializing with address book: " + tab + " and user prefs " + userPrefs);
        //The string name is hardcoded for now. Ability to change teachingAssistantBuddy name will come later
        this.teachingAssistantBuddy = new TeachingAssistantBuddy(tab);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredStudents = new FilteredList<>(this.teachingAssistantBuddy.getStudentList());
    }

    public ModelManager() {
        this(new TeachingAssistantBuddy(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== TeachingAssistantBuddy ==================================================================

    @Override
    public void setBuddy(ReadOnlyTeachingAssistantBuddy module) {
        this.teachingAssistantBuddy.resetData(module);
    }

    @Override
    public ReadOnlyTeachingAssistantBuddy getBuddy() {
        return teachingAssistantBuddy;
    }

    /**
     * @param student
     * @return true if current teachingAssistantBuddy has specified student
     */
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return teachingAssistantBuddy.hasStudent(student);
    }

    @Override
    public void deleteStudent(Student target) {
        teachingAssistantBuddy.removeStudent(target);
    }

    @Override
    public void addStudent(Student student) {
        teachingAssistantBuddy.addStudent(student);
        updateFilteredStudentList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setStudent(Student target, Student editedStudent) {
        requireAllNonNull(target, editedStudent);
        teachingAssistantBuddy.setStudent(target, editedStudent);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Student> getFilteredStudentList() {
        return filteredStudents;
    }

    @Override
    public void updateFilteredStudentList(Predicate<Student> predicate) {
        requireNonNull(predicate);
        filteredStudents.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return teachingAssistantBuddy.equals(other.teachingAssistantBuddy)
                && userPrefs.equals(other.userPrefs)
                && filteredStudents.equals(other.filteredStudents);
    }

}
