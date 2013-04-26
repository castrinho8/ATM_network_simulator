package practicaacs.fap;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolTraspasoTest {
	
	
	@Test
	public void testSolSaldo() {
		SolTraspaso m = new SolTraspaso("ma.ch.te", "pa.ca.va", 23, 43, true, "tarjeta", 3, 1, 3000);
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
		assertEquals(m.getNumcanal(),23);
		assertEquals(m.getNmsg(),43);
		assertTrue(m.getCodonline());
		assertEquals(m.getNum_tarjeta(),"tarjeta");
		assertEquals(m.getNum_cuenta_origen(),3);
		assertEquals(m.getNum_cuenta_destino(),1);
		assertEquals(m.getImporte(),3000);
	}

	@Test
	public void testParse() {
		SolTraspaso m = null;
		String msg = new SolTraspaso("ma.ch.te", "pa.ca.va", 23, 43, true, "tarjeta", 3, 1, 3000).toString();
		
		try {
			m = (SolTraspaso) Mensaje.parse(msg);
		} catch (MensajeNoValidoException e) {
			fail(e.getLocalizedMessage());
			return;
		}
		
		assertNotNull(m);
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
		assertEquals(m.getNumcanal(),23);
		assertEquals(m.getNmsg(),43);
		assertTrue(m.getCodonline());
		assertEquals(m.getNum_tarjeta(),"tarjeta");
		assertEquals(m.getNum_cuenta_origen(),3);
		assertEquals(m.getNum_cuenta_destino(),1);
		assertEquals(m.getImporte(),3000);

	}
}
