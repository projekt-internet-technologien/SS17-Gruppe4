package de.uniluebeck.ait.facerec;

import de.uzl.itm.ncoap.communication.blockwise.BlockSize;

public class App {
	public static void main(String[] args) {
		SSPExampleApplication server = new SSPExampleApplication(BlockSize.SIZE_64, BlockSize.SIZE_64);
		server.run();
	}
}
