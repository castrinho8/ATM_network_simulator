package practicaacs.fap;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolSaldoTest {

	@Test
	public void testSolSaldo() {
		SolSaldo m = new SolSaldo("ma.ch.te", "pa.ca.va", 23, 43, true, "tarjeta", 3);
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
		assertEquals(m.getNumcanal(),23);
		assertEquals(m.getNmsg(),43);
		assertTrue(m.getCodonline());
		assertEquals(m.getNum_tarjeta(),"tarjeta");
		assertEquals(m.getNum_cuenta(),3);		
	}

	@Test
	public void testParse() {
		SolSaldo m = null;
		String msg = new SolSaldo("ma.ch.te", "pa.ca.va", 23, 43, true, "tarjeta", 3).toString();
		
		try {
			m = (SolSaldo) Mensaje.parse(msg);
		} catch (MensajeNoValidoException e) {
			fail();
		}
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
		assertEquals(m.getNumcanal(),23);
		assertEquals(m.getNmsg(),43);
		assertTrue(m.getCodonline());
		assertEquals(m.getNum_tarjeta(),"tarjeta");
		assertEquals(m.getNum_cuenta(),3);

	}

}
