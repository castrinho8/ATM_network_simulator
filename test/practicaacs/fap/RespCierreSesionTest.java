package practicaacs.fap;

import static org.junit.Assert.*;

import org.junit.Test;

public class RespCierreSesionTest {

	@Test
	public void testRespCierreSesion() {
		RespCierreSesion m = new RespCierreSesion("ma.ch.te", "pa.ca.va", false, CodigosError.FUERASEC, 32, 43, 34);
		
		assertEquals("ma.ch.te",m.getOrigen());
		assertEquals("pa.ca.va",m.getDestino());
		assertFalse(m.getCodResp());
		assertEquals(CodigosError.FUERASEC,m.getCodError());
		assertEquals(m.getTotalReintegros(),32);
		assertEquals(m.getTotalAbonos(),43);
		assertEquals(m.getTotalTraspasos(),34);
	}

	@Test
	public void testParse() {
		RespCierreSesion m = null;
		String msg = new RespCierreSesion("ma.ch.te", "pa.ca.va", false, CodigosError.FUERASEC, 32, 43, 34).toString();
		
		try {
			m = (RespCierreSesion) Mensaje.parse(msg);
		} catch (MensajeNoValidoException e) {
			fail(e.getLocalizedMessage());
			return;
		}
		
		assertNotNull(m);
		assertEquals("ma.ch.te",m.getOrigen());
		assertEquals("pa.ca.va",m.getDestino());
		assertFalse(m.getCodResp());
		assertEquals(CodigosError.FUERASEC,m.getCodError());	
		assertEquals(m.getTotalReintegros(),32);
		assertEquals(m.getTotalAbonos(),43);
		assertEquals(m.getTotalTraspasos(),34);
		
	}

}
