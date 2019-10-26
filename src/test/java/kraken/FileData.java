package kraken;

import java.io.Serializable;

public class FileData implements Serializable{
	private static final long serialVersionUID = 1L;

	public byte[] data;

	public FileData(int size) { data = new byte[size]; }
	
	public FileData() { data = new byte[Tags.MAX_MSG_SIZE]; }

}
