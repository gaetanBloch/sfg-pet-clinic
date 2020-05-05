package guru.springframework.sfgpetclinic;

import guru.springframework.sfgpetclinic.model.Owner;

/**
 * @author Gaetan Bloch
 * Created on 20/03/2020
 */
public final class TestUtils {
    public static final Long ID = 1L;
    public static final Long ID2 = 2L;
    public static final Owner OWNER = Owner.builder().id(ID).build();
    public static final Owner OWNER2 = Owner.builder().id(ID2).build();

    private TestUtils() {
        // To prevent instantiation
        throw new UnsupportedOperationException();
    }
}
