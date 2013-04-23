package practicaacs.fap;

import static org.junit.Assert.*;

import org.junit.Test;

public class RespIniTraficoRecTest {

	@Test
	public void testRespIniTraficoRec() {
		RespIniTraficoRec m = new RespIniTraficoRec("ma.ch.te", "pa.ca.va", false, CodigosError.FUERASEC);
		
		assertEquals("ma.ch.te",m.getOrigen());
		assertEquals("pa.ca.va",m.getDestino());
		assertFalse(m.getCodResp());
		assertEquals(CodigosError.FUERASEC,m.getCodError());
		
	}

	@Test
	public void testParse() {
		RespIniTraficoRec m = null;
		String msg = new RespIniTraficoRec("ma.ch.te", "pa.ca.va", false, CodigosError.FUERASEC).toString();
		
		try {
			m = (RespIniTraficoRec) Mensaje.parse(msg);
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
