package customskinserver.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BytesUtil {
	public static byte[] toBytes(String text){
		ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
			out.writeUTF(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return b.toByteArray();
	}
	public static String fromBytes(byte[] bytes){
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
		try {
			return in.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
