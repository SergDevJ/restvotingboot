package ru.ssk.restvoting.util.validation;

import javax.validation.groups.Default;

public class ValidationGroup {
    public interface Password {}
    public interface Persist extends Default {}
}
