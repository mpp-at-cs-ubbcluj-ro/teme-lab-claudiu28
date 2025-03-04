import main.model.ComputerRepairRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ComputerRepairRequestTest {
    @Test
    @DisplayName("Test 1")
    public void TestOne(){
        ComputerRepairRequest request =
                new ComputerRepairRequest(1,"User1","Address1","0722229871","Asus","13/10/2020","Broken display");
        assertEquals(1, request.getID());
        assertEquals("User1",request.getOwnerName());
        assertEquals("Asus",request.getModel());
    }

    @Test
    @DisplayName("Test 2")
    public void TestTwo(){
        ComputerRepairRequest request1 =
                new ComputerRepairRequest(1,"User1","Address1","0722229871","Asus","13/10/2020","Broken display");
        ComputerRepairRequest request2 =
                new ComputerRepairRequest(2,"User2","Address2","0733339871","Lenevo","3/11/2020","Broken display");

        assertNotEquals(request1,request2);
        assertEquals(request2.getProblemDescription(),request1.getProblemDescription());
    }
      
}
