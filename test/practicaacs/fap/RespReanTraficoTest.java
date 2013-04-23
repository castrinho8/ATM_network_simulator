package practicaacs.fap;

import static org.junit.Assert.*;

import org.junit.Test;

public class RespReanTraficoTest {

	@Test
	public void testRespReanTrafico() {
		RespReanTrafico m = new RespReanTrafico("ma.ch.te", "pa.ca.va", false, CodigosError.FUERASEC);
		
		assertEquals("ma.ch.te",m.getOrigen());
		assertEquals("pa.ca.ca",m.getDestino());
		assertFalse(m.getCodResp());
		assertEquals(CodigosError.FUERASEC,m.getCodError());
		
	}

	@Test
	public void testParse() {
		RespReanTrafico m = null;
		String msg = new RespReanTrafico("ma.ch.te", "pa.ca.va", false, CodigosError.FUERASEC).toString();
		
		try {
			m = (RespReanTrafico) Mensaje.parse(msg);
		} catch (MensajeNoValidoException e) {
			fail();
		}
		
		assertEquals("ma.ch.te",m.getOrigen());
		assertEquals("pa.ca.ca",m.getDestino());
		assertFalse(m.getCodResp());
		assertEquals(CodigosError.FUERASEC,m.getCodError());
		
	}

}
