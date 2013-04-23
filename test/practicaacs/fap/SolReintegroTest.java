package practicaacs.fap;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolReintegroTest {
	@Test
	public void testSolReintegro() {
		SolReintegro m = new SolReintegro("ma.ch.te", "pa.ca.va", 23, 43, true, "tarjeta", 3, 2000);
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
		assertEquals(m.getNumcanal(),23);
		assertEquals(m.getNmsg(),43);
		assertTrue(m.getCodonline());
		assertEquals(m.getNum_tarjeta(),"tarjeta");
		assertEquals(m.getNum_cuenta(),3);
		assertEquals(m.getImporte(),2000);
		
	}

	@Test
	public void testParse() {
		SolReintegro m = null;
		String msg = new SolReintegro("ma.ch.te", "pa.ca.va", 23, 43, true, "tarjeta", 3, 2000).toString();
		
		try {
			m = (SolReintegro) Mensaje.parse(msg);
		} catch (MensajeNoValidoException e) {
			fail();
		}
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.ca");
		assertEquals(m.getNumcanal(),23);
		assertEquals(m.getNmsg(),43);
		assertTrue(m.getCodonline());
		assertEquals(m.getNum_tarjeta(),"tarjeta");
		assertEquals(m.getNum_cuenta(),3);
		assertEquals(m.getImporte(),2000);

	}


}
