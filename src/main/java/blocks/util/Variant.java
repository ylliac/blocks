package blocks.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.ByteStreams;

public class Variant {

	// GENERIC

	public static boolean canConvertTo(Class<?> clazz, Object value) {
		return clazz.isInstance(value);
	}

	public static <T> T to(Class<T> clazz, Object value) {
		if (clazz.isInstance(value)) {
			return clazz.cast(value);
		} else {
			return null;
		}
	}

	// FILE

	public static boolean canConvertToFile(Object value) {
		return (value instanceof File);
	}

	public static File toFile(Object value) {
		File result = null;

		if (value instanceof File) {
			result = ((File) value);
		}

		return result;
	}

	// BYTE BUFFER

	public static boolean canConvertToByteBuffer(Object value) {
		return (value instanceof ByteBuffer || value instanceof byte[]
				|| value instanceof File || value instanceof String);
	}

	public static ByteBuffer toByteBuffer(Object value) {
		ByteBuffer result = null;

		if (value instanceof ByteBuffer) {
			result = ((ByteBuffer) value);
		} else if (value instanceof byte[]) {
			result = ByteBuffer.wrap((byte[]) value);
		} else if (value instanceof String) {
			byte[] bytes = value.toString().getBytes();
			result = ByteBuffer.wrap(bytes);
		} else if (value instanceof File) {
			try {
				FileInputStream input = new FileInputStream((File) value);
				byte[] bytes = ByteStreams.toByteArray(input);
				input.close();
				result = ByteBuffer.wrap(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	// DOUBLE

	public static boolean canConvertToDouble(Object value) {
		boolean result = false;

		if (value instanceof Number) {
			result = true;
		} else if (value instanceof String) {
			try {
				Double.parseDouble(value.toString());
				result = true;
			} catch (NumberFormatException e) {
				// DO NOTHING
			}
		}

		return result;
	}

	public static double toDouble(Object value) {
		double result = 0.0;

		if (value instanceof Number) {
			result = ((Number) value).doubleValue();
		} else if (value instanceof String) {
			try {
				result = Double.parseDouble(value.toString());
			} catch (NumberFormatException e) {
				// DO NOTHING
			}
		}

		return result;
	}

	// STRING

	public static boolean canConvertToString(Object value) {
		return value != null;
	}

	public static String toString(Object value) {
		return value != null ? value.toString() : "";
	}

	// BOOLEAN

	public static boolean canConvertToBoolean(Object value) {
		return (value instanceof Boolean || value instanceof Number || value instanceof String);
	}

	public static boolean toBoolean(Object value) {
		boolean result = false;

		if (value instanceof Boolean) {
			result = (Boolean) value;
		} else if (value instanceof Number) {
			result = ((Number) value).intValue() != 0;
		} else if (value instanceof String) {
			result = Boolean.parseBoolean(value.toString());
		}

		return result;
	}

	// CHAR

	public static boolean canConvertToChar(Object value) {
		boolean result = false;

		if (value instanceof Character) {
			result = true;
		} else if (value instanceof String && value.toString().length() == 1) {
			result = true;
		}

		return result;
	}

	public static char toChar(Object value) {
		char result = 0;

		if (value instanceof Character) {
			result = (Character) value;
		} else if (value instanceof String && value.toString().length() == 1) {
			result = value.toString().charAt(0);
		}

		return result;
	}

	// INTEGER

	public static boolean canConvertToInt(Object value) {
		boolean result = false;

		if (value instanceof Number)
			result = true;
		else if (value instanceof String) {
			try {
				Integer.parseInt(value.toString());
				result = true;
			} catch (NumberFormatException e) {
				// DO NOTHING
			}
		}

		return result;
	}

	public static int toInt(Object value) {
		int result = 0;

		if (value instanceof Number) {
			result = ((Number) value).intValue();
		} else if (value instanceof String) {
			try {
				result = Integer.parseInt(value.toString());
			} catch (NumberFormatException e) {
				// DO NOTHING
			}
		}

		return result;
	}

	// LONG

	public static boolean canConvertToLong(Object value) {
		boolean result = false;

		if (value instanceof Number)
			result = true;
		else if (value instanceof String) {
			try {
				Long.parseLong(value.toString());
				result = true;
			} catch (NumberFormatException e) {
				// DO NOTHING
			}
		}

		return result;
	}

	public static long toLong(Object value) {
		long result = 0;

		if (value instanceof Number) {
			result = ((Number) value).longValue();
		} else if (value instanceof String) {
			try {
				result = Long.parseLong(value.toString());
			} catch (NumberFormatException e) {
				// DO NOTHING
			}
		}

		return result;
	}

	// LIST

	public static boolean canConvertToList(Object value) {
		return (value instanceof Collection);
	}

	public static List<Object> toList(Object value) {
		List<Object> list = new ArrayList<Object>();
		if (value instanceof Collection) {
			list.addAll((Collection<?>) value);
		}
		return list;
	}

	// MAP

	public static boolean canConvertToMap(Object value) {
		return (value instanceof Map);
	}

	public static Map<String, Object> toMap(Object value) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (value instanceof Map) {
			Map<?, ?> otherMap = (Map<?, ?>) value;
			for (Map.Entry<?, ?> e : otherMap.entrySet()) {
				map.put(e.getKey().toString(), e.getValue());
			}
		}
		return map;
	}

}
