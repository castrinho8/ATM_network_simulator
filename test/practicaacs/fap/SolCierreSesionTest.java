package practicaacs.fap;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class SolCierreSesionTest {

	
	@Test
	public void testSolSaldo() {
		SolCierreSesion m = new SolCierreSesion("ma.ch.te", "pa.ca.va", 1,3,4);
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
		assertEquals(m.getTotal_reintegros(),1);
		assertEquals(m.getAbonos(),3);
		assertEquals(m.getTraspasos(),4);
	}

	@Test
	public void testParse() {
		SolCierreSesion m = null;
		String msg = new SolCierreSesion("ma.ch.te", "pa.ca.va", 1,3,4).toString();
		
		try {
			m = (SolCierreSesion) Mensaje.parse(msg);
		} catch (MensajeNoValidoException e) {
			fail();
		}
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
		assertEquals(m.getTotal_reintegros(),1);
		assertEquals(m.getAbonos(),3);
		assertEquals(m.getTraspasos(),4);

	}

}
