package TestCore;

import Core.SampleRepresentation;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class TestSampleRepresentation {

    @Test
    public void TestSeparatePermissions() {
        SampleRepresentation s = new SampleRepresentation();
        List<String> permissions = new ArrayList();
        permissions.add("permission:severity");
        s.setPermissions(permissions);
        s.separatePermissions();
        assertThat("severity", is(equalTo(s.getSeverities().get(0))));
    }

    @Test
    public void TestUniquePermissionSeparate() {
        SampleRepresentation s = new SampleRepresentation();
        List<String> permissions = new ArrayList();
        permissions.add("permission2");
        s.setPermissions(permissions);
        s.separatePermissions();
        assertThat("permission2", is(equalTo(s.getPermissions().get(0))));
    }
}
