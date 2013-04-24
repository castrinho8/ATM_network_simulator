package practicaacs.fap;

import static org.junit.Assert.*;

import org.junit.Test;

public class RespFinTraficoRecTest {

	@Test
	public void testRespFinTraficoRec() {
		RespFinTraficoRec m = new RespFinTraficoRec("ma.ch.te", "pa.ca.va", false, CodigosError.FUERASEC);
		
		assertEquals("ma.ch.te",m.getOrigen());
		assertEquals("pa.ca.va",m.getDestino());
		assertFalse(m.getCodResp());
		assertEquals(CodigosError.FUERASEC,m.getCodError());
		
	}

	@Test
	public void testParse() {
		RespFinTraficoRec m = null;
		String msg = new RespFinTraficoRec("ma.ch.te", "pa.ca.va", false, CodigosError.FUERASEC).toString();
		
		try {
			m = (RespFinTraficoRec) Mensaje.parse(msg);
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
