package practicaacs.fap;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class SolDetTraficoTest {

	
	@Test
	public void testSolSaldo() {
		SolDetTrafico m = new SolDetTrafico("ma.ch.te", "pa.ca.va");
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
	}

	@Test
	public void testParse() {
		SolDetTrafico m = null;
		String msg = new SolDetTrafico("ma.ch.te", "pa.ca.va").toString();
		
		try {
			m = (SolDetTrafico) Mensaje.parse(msg);
		} catch (MensajeNoValidoException e) {
			fail();
		}
		
		assertEquals(m.getOrigen(),"ma.ch.te");
		assertEquals(m.getDestino(),"pa.ca.va");
	}

}
