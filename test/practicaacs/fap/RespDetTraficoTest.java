package practicaacs.fap;

import static org.junit.Assert.*;

import org.junit.Test;

public class RespDetTraficoTest {

	@Test
	public void testRespDetTrafico() {
		RespDetTrafico m = new RespDetTrafico("ma.ch.te", "pa.ca.va", false, CodigosError.FUERASEC);
		
		assertEquals("ma.ch.te",m.getOrigen());
		assertEquals("pa.ca.va",m.getDestino());
		assertFalse(m.getCodResp());
		assertEquals(CodigosError.FUERASEC,m.getCodError());
		
	}

	@Test
	public void testParse() {
		RespDetTrafico m = null;
		String msg = new RespDetTrafico("ma.ch.te", "pa.ca.va", false, CodigosError.FUERASEC).toString();
		
		try {
			m = (RespDetTrafico) Mensaje.parse(msg);
		} catch (MensajeNoValidoException e) {
			fail(e.getLocalizedMessage());
			return;
		}
		
		assertEquals("ma.ch.te",m.getOrigen());
		assertEquals("pa.ca.va",m.getDestino());
		assertFalse(m.getCodResp());
		assertEquals(CodigosError.FUERASEC,m.getCodError());	
	}

}
