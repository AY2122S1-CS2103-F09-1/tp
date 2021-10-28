package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STUDENT_ID_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STUDENT_ID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TELE_HANDLE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TELE_HANDLE_BOB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.module.student.Student;

public class TypicalStudents {

    public static final Student AMY = new StudentBuilder().withName(VALID_NAME_AMY).withStudentId(VALID_STUDENT_ID_AMY)
            .withTeleHandle(VALID_TELE_HANDLE_AMY).withEmail(VALID_EMAIL_AMY).build();
    public static final Student BOB = new StudentBuilder().withName(VALID_NAME_BOB).withStudentId(VALID_STUDENT_ID_BOB)
            .withTeleHandle(VALID_TELE_HANDLE_BOB).withEmail(VALID_EMAIL_BOB).build();

    /**
     * Returns a list of students.
     *
     * @return A list of students
     */
    public static List<Student> getTypicalStudents() {
        return new ArrayList<>(Arrays.asList(AMY, BOB));
    }
}
