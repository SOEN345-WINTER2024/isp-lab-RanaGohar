
import java.util.*;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * This class contains 13 JUnit tests for the Iterator interface. The tests are derived from an
 * IDM (input domain modeling) based on the JavaDoc API for Iterator.
 * The three methods tested are: hasNext(), next(), remove()
 * The following characteristics have been identified and are used to generate tests for the methods:
 * C1: iterator has more values
 * C2: iterator returns a non-null object reference
 * C3: remove() is supported
 * C4: remove() precondition is satisfied
 * C5: collection in consistent state while iterator in use
 * Each characteristic has a boolean partition
 */

public class IteratorTest {

    private List<String> list;
    private Iterator<String> itr;

    @Before
    public void setUp() {
        list = new ArrayList<String>();
        list.add("Elephant");
        list.add("Monkey");
        itr = list.iterator();
    }

    // 3 Tests for Iterator method hasNext()
    //  The 2 characteristics associated with hasNext() are: C1, C5

    // Test 1 of hasNext(): testHasNext_BaseCase():  C1-T, C5-T
    @Test
    public void testHasNext_BaseCase() {
        // Act
        boolean hasNext = itr.hasNext();

        // Assert
        assertTrue("Iterator should have more elements", hasNext);
    }

    // Test 2 of hasNext(): testHasNext_C1(): C1-F, C5-T
    @Test
    public void testHasNext_C1() {
        for (int i = 0; i < 2; i++) {
            itr.next();
        }
        assertFalse("Iterator should not have more elements", itr.hasNext());
    }

    // Test 3 of hasNext(): testHasNext_C5(): C1-T, C5-F
    // This test fails!
    // The reason is that standard Java implementations of the Iterator
    // interface are not consistent in their use of ConcurrentModificationException.

    @Test(expected = ConcurrentModificationException.class)
        public void testHasNext_C5() {
        list.add("elephant");
        itr.next();
        itr.hasNext();
    }

    // 4 Tests for Iterator method next()

    // The 3 characteristics associated with next() are: C1, C2, C5

    // Test 1 of next(): testNext_BaseCase(): C1-T, C2-T, C5-T
    @Test
    public void testNext_BaseCase() {
        String result = itr.next();
        assertEquals("Elephant", result);
    }

    // Test 2 of next(): testNext_C1(): C1-F, C2-F, C5-T
    @Test
    public void testNext_C1() {
        for (int i = 0; i < 2; i++) {
            itr.next();
        }
        assertFalse("Iterator should not have more elements", itr.hasNext());
    }

    // Test 3 of next(): testNext_C2(): C1-T, C2-F, C5-T
    @Test
    public void testNext_C2() {
        List<String> list = new ArrayList<>();
        list.add(null);
        Iterator<String> itr = list.iterator();

        String nextElement = itr.next();

        assertNull("Next element should be null", nextElement);
    }

    // Test 4 of next(): testNext_C5(): C1-T, C2-F, C5-F
    @Test(expected = ConcurrentModificationException.class)
    public void testNext_C5() {
        String nextElement = itr.next();

        assertNotNull("Next element should not be null", nextElement);

        list.add("Dog");

        itr.next();
    }


    // 6 Tests for Iterator method remove()


    // The 5 characteristics associated with remove() are: C1, C2, C3, C4, C5

    // Test 1 of remove(): testRemove_BaseCase(): C1-T, C2-T, C3-T, C4-T, C5-T
    @Test
    public void testRemove_BaseCase() {
        String delete = null;
        while (itr.hasNext()) {
            String element = itr.next();
            if (element.equals("Apple")) {
                delete = element;
                itr.remove();
                break;
            }
        }
        assertFalse("The list does not contain removed element", list.contains(delete));
    }

    // Test 2 of remove(): testRemove_C1(): C1-F, C2-F, C3-T, C4-T, C5-T
    @Test
    public void testRemove_C1() {
        itr.next();
        try {
            itr.remove();
        } catch (IllegalStateException e) {
            assertEquals("Next has not been called", e.getMessage());
            throw e;
        }
    }

    // Test 3 of remove(): testRemove_C2(): C1-T, C2-F, C3-T, C4-T, C5-T
    @Test
    public void testRemove_C2() {

        list.add(null);
        list.add("Dog");
        itr = list.iterator();

        itr.next();
        itr.next();
        itr.next();
        itr.remove();

        assertFalse(list.contains(null));
    }

    // Test 4 of remove(): testRemove_C3(): C1-T, C2-T, C3-F, C4-T, C5-T
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove_C3() {

        List<String> unmodifiableList = Collections.unmodifiableList(list);
        Iterator<String> unmodifiableItr = unmodifiableList.iterator();
        unmodifiableItr.next();

        unmodifiableItr.remove();
    }

    // Test 5 of remove(): testRemove_C4(): C1-T, C2-T, C3-T, C4-F, C5-T
    @Test
    public void testRemove_C4() {
        assertTrue(itr.hasNext());
        assertNotNull(itr.next());
        int ListSizeBefore = list.size();
        itr.next();
        itr.remove();
        assertEquals(ListSizeBefore - 1, list.size());
    }


    // Test 6 of next(): testRemove_C5(): C1-T, C2-T, C3-T, C4-T, C5-F
    @Test
    public void testRemove_C5() {
        itr.next();
        list.add("elephant");
        try {
            itr.remove();
        } catch (ConcurrentModificationException e) {
            return;
        }
        fail("Expected ConcurrentModificationException to be thrown");
    }

}



