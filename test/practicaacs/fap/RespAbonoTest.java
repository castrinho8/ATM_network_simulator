package practicaacs.fap;

import static org.junit.Assert.*;

import org.junit.Test;

public class RespAbonoTest {


	@Test
	public void testContructorAndGetters() {
		RespAbono m = new RespAbono("ma.ch.te", "pa.ca.va", 4, 23, false, CodigosRespuesta.CONSACEPTADA, true, 1000);
		
		assertEquals("ma.ch.te",m.getOrigen());
		assertEquals("pa.ca.va",m.getDestino());
		assertEquals(4,m.getNumcanal());
		assertEquals(23,m.getNmsg());
		assertFalse(m.getCodonline());
		assertEquals(CodigosRespuesta.CONSACEPTADA,m.getCod_resp());
		assertTrue(m.getSigno());
		assertEquals(1000,m.getSaldo());
		
	}	
	
	@Test
	public void testParse() {
		RespAbono m = null;
		String msg = new RespAbono("ma.ch.te", "pa.ca.va", 4, 23, false, CodigosRespuesta.CONSACEPTADA, true, 1000).toString();
		
		try {
			m = (RespAbono) Mensaje.parse(msg);
		} catch (MensajeNoValidoException e) {
			fail("Mensaje No Valido!!");
			return;
		}
		
		assertNotNull(m);
		assertEquals("ma.ch.te",m.getOrigen());
		assertEquals("pa.ca.va",m.getOrigen());
		assertEquals(4,m.getNumcanal());
		assertEquals(23,m.getNmsg());
		assertFalse(m.getCodonline());
		assertEquals(CodigosRespuesta.CONSACEPTADA,m.getCod_resp());
		assertTrue(m.getSigno());
		assertEquals(1000,m.getSaldo());
	}

}
