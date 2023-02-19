import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.voroniuk.prokat.utils.ValidationService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class ValidationServiceEmailTest {

    private final String email;

    private final boolean expected;

    public ValidationServiceEmailTest(String email, boolean expected) {
        this.email = email;
        this.expected = expected;
    }

    @Test
    public void TestEmail() {
        assertThat(ValidationService.emailValidator(email), is(expected));
    }

    @Parameterized.Parameters
    public static List<Object> listArguments() {
        return Arrays.asList(new Object[][]{
                {"test_test@ukr.net",   true},
                {"test_test@ukr.net",   true},
                {"testtestukr.net",     false},
                {"test_test@ukr..net",  false}
        });
    }
}
