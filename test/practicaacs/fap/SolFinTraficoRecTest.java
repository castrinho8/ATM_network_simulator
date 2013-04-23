package practicaacs.fap;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolFinTraficoRecTest {
	@Test
	public void testSolFinTraficoRec() {
		SolFinTraficoRec m = new SolFinTraficoRec("ma.ch.te", "pa.ca.va");
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
	}

	@Test
	public void testParse() {
		SolFinTraficoRec m = null;
		String msg = new SolFinTraficoRec("ma.ch.te", "pa.ca.va").toString();
		
		try {
			m = (SolFinTraficoRec) Mensaje.parse(msg);
		} catch (MensajeNoValidoException e) {
			fail(e.getLocalizedMessage());
			return;
		}
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
	}


}
