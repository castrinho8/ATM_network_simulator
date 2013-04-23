package practicaacs.fap;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class SolAperturaSesionTest {

	
	@SuppressWarnings("deprecation")
	@Test
	public void testSolSaldo() {
		SolAperturaSesion m = new SolAperturaSesion("ma.ch.te", "pa.ca.va", 23, null, "124.234.234.233/2323");
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
		assertEquals(m.getNcanales(),23);
		assertEquals(m.getPuerto(), "124.234.234.233/2323");
		assertEquals(m.getTime().getDay(),new Date().getDay());
		assertEquals(m.getTime().getMonth(),new Date().getMonth());
		assertEquals(m.getTime().getMinutes(),new Date().getMinutes());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testParse() {
		SolAperturaSesion m = null;
		String msg = new SolAperturaSesion("ma.ch.te", "pa.ca.va", 23, null, "124.234.234.233/2323").toString();
		
		try {
			m = (SolAperturaSesion) Mensaje.parse(msg);
		} catch (MensajeNoValidoException e) {
			fail();
		}
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
		assertEquals(m.getNcanales(),23);
		assertEquals(m.getPuerto(), "124.234.234.233/2323");
		assertEquals(m.getTime().getDay(),new Date().getDay());
		assertEquals(m.getTime().getMonth(),new Date().getMonth());
		assertEquals(m.getTime().getMinutes(),new Date().getMinutes());

	}

}
