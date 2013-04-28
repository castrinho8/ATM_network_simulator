package practicaacs.fap;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class RespMovimientosTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testRespMovimientos() {
		RespMovimientos m = new RespMovimientos("ma.ch.te", "pa.ca.va", 4, 2, true, CodigosRespuesta.CUENTANVALIDA,
				32, CodigosMovimiento.REINTEGRO, true, 2300, new Date());
		
		assertEquals("ma.ch.te",m.getOrigen());
		assertEquals("pa.ca.va",m.getDestino());
		assertEquals(m.getNumcanal(),4);
		assertEquals(m.getNmsg(),2);
		assertTrue(m.getCodonline());
		assertEquals(m.getCod_resp(),CodigosRespuesta.CUENTANVALIDA);
		assertEquals(m.getNmovimientos(),32);
		assertEquals(m.getTipoMov(),CodigosMovimiento.REINTEGRO);
		assertTrue(m.getSigno());
		assertEquals(m.getImporte(),2300);
		assertEquals(m.getFecha().getDay(),new Date().getDay());
		assertEquals(m.getFecha().getMonth(),new Date().getMonth());
		assertEquals(m.getFecha().getMinutes(),new Date().getMinutes());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testParse() {
		RespMovimientos m = null;
		String msg = new RespMovimientos("ma.ch.te", "pa.ca.va", 4, 2, true, CodigosRespuesta.CUENTANVALIDA,
				18, CodigosMovimiento.REINTEGRO, true, 2300, new Date()).toString();
		
		try {
			m = (RespMovimientos) Mensaje.parse(msg);
		} catch (MensajeNoValidoException e) {
			fail(e.getLocalizedMessage());
			return;
		}
		
		assertNotNull(m);
		assertEquals("ma.ch.te",m.getOrigen());
		assertEquals("pa.ca.va",m.getDestino());
		assertEquals(m.getNumcanal(),4);
		assertEquals(m.getNmsg(),2);
		assertTrue(m.getCodonline());
		assertEquals(m.getCod_resp(),CodigosRespuesta.CUENTANVALIDA);
		assertEquals(m.getNmovimientos(),18);
		assertEquals(m.getTipoMov(),CodigosMovimiento.REINTEGRO);
		assertTrue(m.getSigno());
		assertEquals(m.getImporte(),2300);
		assertEquals(m.getFecha().getDay(),new Date().getDay());
		assertEquals(m.getFecha().getMonth(),new Date().getMonth());
		assertEquals(m.getFecha().getMinutes(),new Date().getMinutes());
		
	}

}
