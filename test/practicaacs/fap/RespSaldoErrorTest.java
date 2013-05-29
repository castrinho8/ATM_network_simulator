package practicaacs.fap;

import static org.junit.Assert.*;

import org.junit.Test;

public class RespSaldoErrorTest {

	@Test
	public void testRespSaldo() {
		RespSaldoError m = new RespSaldoError("ma.ch.te", "pa.ca.va",23, 43, true, CodigosError.CANALOCUP);
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
		assertEquals(m.getNumcanal(),23);
		assertEquals(m.getNmsg(),43);
		assertTrue(m.getCodonline());
		assertEquals(m.getCodigoError(),CodigosError.CANALOCUP);
	}

	@Test
	public void testParse() {
		RespSaldoError m = null;
		String msg = new RespSaldoError("ma.ch.te", "pa.ca.va",23, 43, true, CodigosError.CANALOCUP).toString();
				
		try {
			m = (RespSaldoError) Mensaje.parse(msg);
		} catch (MensajeNoValidoException e) {
			fail(e.getLocalizedMessage());
			return;
		}
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
		assertEquals(m.getNumcanal(),23);
		assertEquals(m.getNmsg(),43);
		assertTrue(m.getCodonline());
		assertEquals(m.getCodigoError(),CodigosError.CANALOCUP);
	}

}
