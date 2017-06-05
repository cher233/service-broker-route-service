package model;

import org.junit.Assert;
import org.junit.Test;
import org.servicebroker.routeservice.model.FiltersType;



/**
 * Created by Cher on 04/06/2017.
 */
public class FilterTypeTest {
    @Test
    public void testContainsPositive()
    {
        String filterCheck = "xss";
        Assert.assertTrue(FiltersType.contains(filterCheck));
    }

    @Test
    public void testContainsNegative()
    {
        String filterCheck = "bla";
        Assert.assertFalse(FiltersType.contains(filterCheck));
    }
}
