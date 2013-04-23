package practicaacs.fap;

import static org.junit.Assert.*;

import org.junit.Test;

public class RespTraspasoTest {

	@Test
	public void testRespTraspaso() {
		RespTraspaso m = new RespTraspaso("ma.ch.te", "pa.ca.va",23, 43, true, CodigosRespuesta.CONSACEPTADA, true, 3000,false,400);
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.ca");
		assertEquals(m.getNumcanal(),23);
		assertEquals(m.getNmsg(),43);
		assertTrue(m.getCodonline());
		assertEquals(m.getCod_resp(),CodigosRespuesta.CONSACEPTADA);
		assertTrue(m.getSignoOrigen());
		assertFalse(m.getSignoDestino());
		assertEquals(m.getSaldoOrigen(),3000);
		assertEquals(m.getSaldoDestino(),4000);
	}

	@Test
	public void testParse() {
		RespTraspaso m = null;
		String msg = new RespTraspaso("ma.ch.te", "pa.ca.va",23, 43, true, CodigosRespuesta.CONSACEPTADA, true, 3000,false,400).toString();
		
		try {
			m = (RespTraspaso) Mensaje.parse(msg);
		} catch (MensajeNoValidoException e) {
			fail();
		}
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.ca");
		assertEquals(m.getNumcanal(),23);
		assertEquals(m.getNmsg(),43);
		assertTrue(m.getCodonline());
		assertEquals(m.getCod_resp(),CodigosRespuesta.CONSACEPTADA);
		assertTrue(m.getSignoOrigen());
		assertFalse(m.getSignoDestino());
		assertEquals(m.getSaldoOrigen(),3000);
		assertEquals(m.getSaldoDestino(),4000);
	}
	
}
