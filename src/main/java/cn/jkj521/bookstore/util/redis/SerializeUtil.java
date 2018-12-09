package cn.jkj521.bookstore.util.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {
	
	//序列化
	public static byte[] serialize(Object object){
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try{
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	//反序列化
	public static Object unserialize(byte[] bytes){
		ByteArrayInputStream bais = null;
		try{
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
