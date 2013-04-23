package practicaacs.fap;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolReanTraficoTest {

	@Test
	public void testSolReanTrafico() {
		SolReanTrafico m = new SolReanTrafico("ma.ch.te", "pa.ca.va");
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
	}

	@Test
	public void testParse() {
		SolReanTrafico m = null;
		String msg = new SolReanTrafico("ma.ch.te", "pa.ca.va").toString();
		
		try {
			m = (SolReanTrafico) Mensaje.parse(msg);
		} catch (MensajeNoValidoException e) {
			fail(e.getLocalizedMessage());
			return;
		}
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
	}


}
