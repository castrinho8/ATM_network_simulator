package practicaacs.fap;

import static org.junit.Assert.*;

import org.junit.Test;

public class RespAperturaSesionTest {

	@Test
	public void testRespAperturaSesionStringStringBooleanCodigosError() {
		RespAperturaSesion m = new RespAperturaSesion("ma.ch.te", "pa.ca.va", false, CodigosError.FUERASEC);
		
		assertEquals("ma.ch.te",m.getOrigen());
		assertEquals("pa.ca.va",m.getDestino());
		assertFalse(m.getCodResp());
		assertEquals(CodigosError.FUERASEC,m.getCodError());
		
	}

	@Test
	public void testParse() {
		RespAperturaSesion m = null;
		String msg = new RespAperturaSesion("ma.ch.te", "pa.ca.va", false, CodigosError.FUERASEC).toString();
		
		System.out.println("RespAperturaSesion -> " + msg);
		
		try {
			m = (RespAperturaSesion) Mensaje.parse(msg);
		} catch (MensajeNoValidoException e) {
			fail(e.getLocalizedMessage());
			return;
		}
		
		assertNotNull(m);
		assertEquals("ma.ch.te",m.getOrigen());
		assertEquals("pa.ca.va",m.getDestino());
		assertFalse(m.getCodResp());
		assertEquals(CodigosError.FUERASEC,m.getCodError());			
		
	}

}
