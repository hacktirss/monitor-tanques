/**
 * VeederTLS2X0Test
 * Pruebas de la clase VeederTLS2X0Test
 * © 2021, DETI Desarrollo y Transferencia de Informática
 * http://detisa.com.mx
 * @author Rolando Esquivel Villafaña, Softcoatl
 * @version 1.0
 * @since Ago 2021
 */
package com.as2.sensorft.protocol;

import com.softcoatl.tls.protocol.TLS2X0;
import com.softcoatl.utils.ASCII;
import org.junit.*;

public class VeederTLS2X0Test {
    
    public VeederTLS2X0Test() {
        TLS2X0 instance = new TLS2X0();
        System.out.println("Comando Fecha " + instance.dateTimeCommand());
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testParseDelivery() throws Exception {
        System.out.println("VeederTLS2X0Test: parseDelivery");
        String delivery = ASCII.SOH + "150130712061339002173003101206143502705900315111213390018970032211121431026833002761029155400207000325102916440268660030409221455002075003310922154902681800315090720520106870033209072124011190003310817135800285500331081714480277030032907291447003862003350729154002857900329211012061416002137003111206150702727000363112213500021560031711221440027227003341110131400135300324111014090265450033910251537002124003301025162802712800379101314240021730033110131518027326003870929124000207900334092913310272330036309151530002065003350915162902724900388090720500120810033909072120012512003390830150900214300334083016000272520039808161632002169003360816172302713400372300711101241019291003181110133504438700294110512570021190032711051353026832002861020155600485300331102016540296290032010011452005749003331001154803050100359091516130065530033309151707031355003480827141100498900334082715050298530031608031458013986003340803155503896200355947CD" + ASCII.ETX;
        TLS2X0 instance = new TLS2X0();
        instance.parseDelivery(delivery).forEach(System.out::println);
    }

    @Test
    public void testParseInventory() throws Exception {
        System.out.println("VeederTLS2X0Test: parseInventory");
        String inventory = ASCII.SOH + "100092114500001311000246200207700331056960-022111000933101426200339044774-013011001286702214900336036888-079EAB0" + ASCII.ETX;
        TLS2X0 instance = new TLS2X0();
        instance.parseInventory(inventory).forEach(System.out::println);
    }

    @Test
    public void testValidateDateTime() throws Exception {
        System.out.println("VeederTLS2X0Test: validateDateTime");
        String date = ASCII.SOH + "50021092103009FD3FC" + ASCII.ETX;
        TLS2X0 instance = new TLS2X0();
        boolean result = instance.validateDateTime(date);
        assert result;
    }
}
