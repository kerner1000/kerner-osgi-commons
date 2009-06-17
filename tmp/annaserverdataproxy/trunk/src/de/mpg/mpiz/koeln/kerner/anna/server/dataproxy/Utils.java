package de.mpg.mpiz.koeln.kerner.anna.server.dataproxy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class Utils {

	private Utils() {
	}

	public static <V> V deepCopy(Class<V> c, Serializable s) throws IOException, ClassNotFoundException{
		if(c == null || s == null)
			throw new NullPointerException();
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		new ObjectOutputStream(bs).writeObject(s);
		ByteArrayInputStream bi = new ByteArrayInputStream(bs.toByteArray());
		V v = c.cast(new ObjectInputStream(bi).readObject());
		bs.close();
		bi.close();
		return v;
	}

}
