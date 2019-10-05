package protocol;

import java.io.Serializable;

public class ByteFile implements Serializable{

	private static final long serialVersionUID = 1L;

	public byte[] data;

	public ByteFile(int size) {
		data = new byte[size];
	}
	
	public ByteFile() {
		data = new byte[Tags.MAX_MSG_SIZE];
	}
}

