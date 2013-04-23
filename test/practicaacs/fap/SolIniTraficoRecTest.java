package practicaacs.fap;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolIniTraficoRecTest {

	@Test
	public void testSolIniTraficoRec() {
		SolIniTraficoRec m = new SolIniTraficoRec("ma.ch.te", "pa.ca.va");
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
	}

	@Test
	public void testParse() {
		SolIniTraficoRec m = null;
		String msg = new SolIniTraficoRec("ma.ch.te", "pa.ca.va").toString();
		
		try {
			m = (SolIniTraficoRec) Mensaje.parse(msg);
		} catch (MensajeNoValidoException e) {
			fail(e.getLocalizedMessage());
			return;
		}
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
	}


}
