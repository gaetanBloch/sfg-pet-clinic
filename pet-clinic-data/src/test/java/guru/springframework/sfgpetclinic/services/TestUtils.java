package guru.springframework.sfgpetclinic.services;

import guru.springframework.sfgpetclinic.model.Owner;

/**
 * @author Gaetan Bloch
 * Created on 20/03/2020
 */
public final class TestUtils {
    public static final Long ID = 1L;
    public static final String LAST_NAME = "Smith";
    public static final Owner OWNER = Owner.builder().id(TestUtils.ID).lastName(TestUtils.LAST_NAME).build();

    private TestUtils() {
        // To prevent instantiation
        throw new UnsupportedOperationException();
    }
}
