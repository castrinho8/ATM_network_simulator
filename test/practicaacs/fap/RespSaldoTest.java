package practicaacs.fap;

import static org.junit.Assert.*;

import org.junit.Test;

public class RespSaldoTest {

	@Test
	public void testRespSaldo() {
		RespSaldo m = new RespSaldo("ma.ch.te", "pa.ca.va",23, 43, true, CodigosRespuesta.CONSACEPTADA, true, 3000);
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
		assertEquals(m.getNumcanal(),23);
		assertEquals(m.getNmsg(),43);
		assertTrue(m.getCodonline());
		assertEquals(m.getCod_resp(),CodigosRespuesta.CONSACEPTADA);
		assertTrue(m.getSigno());
		assertEquals(m.getSaldo(),3000);
	}

	@Test
	public void testParse() {
		RespSaldo m = null;
		String msg = new RespSaldo("ma.ch.te", "pa.ca.va",23, 43, true, CodigosRespuesta.CONSACEPTADA,
				true, 3000).toString();
		
		System.out.println("RespSaldo -> " + msg);
		
		try {
			m = (RespSaldo) Mensaje.parse(msg);
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
		assertEquals(m.getCod_resp(),CodigosRespuesta.CONSACEPTADA);
		assertTrue(m.getSigno());
		assertEquals(m.getSaldo(),3000);
	}

}
