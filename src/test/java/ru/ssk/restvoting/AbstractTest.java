package ru.ssk.restvoting;

import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import static ru.ssk.restvoting.TestUtil.getCauseException;
import static ru.ssk.restvoting.util.ValidationUtil.getRootCause;

@SpringBootTest
@ActiveProfiles({"test"})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public abstract class AbstractTest {
    protected <T extends Throwable> void validateRootCause(Class<T> rootExceptionClass, Runnable test) {
        Assertions.assertThrows(rootExceptionClass, () -> {
            try {
                test.run();
            } catch (Throwable e) {
                throw getRootCause(e);
            }
        });
    }

    protected <T extends Throwable> void validateCause(Class<T> causeClass, String causeExceptionMessage, Runnable test) {
        Assertions.assertThrows(causeClass, () -> {
            try {
                test.run();
            } catch (Throwable t) {
                throw getCauseException(causeClass, causeExceptionMessage, t);
            }
        });
    }
}


