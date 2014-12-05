package blocks.core;

import java.util.HashMap;
import java.util.Map;

public class Node {

	public Node() {

	}

	public Property<?> getProperty(String name) {
		return properties.get(name);
	}

	public Object get(String name) {
		return properties.get(name).get();
	}

	public void putProperty(Property<?> property) {
		if (property == null) {
			throw new IllegalArgumentException();
		}

		Property<?> oldProperty = properties.put(property.getName(), property);
		if (property.getParent() != this) {
			property.setParent(this);
		}

		oldProperty.setParent(null);
	}

	public void removeProperty(Property<?> property) {
		if (property == null) {
			throw new IllegalArgumentException();
		}

		properties.remove(property.getName());

		if (property.getParent() == this) {
			property.setParent(null);
		}
	}

	public boolean contains(Property<?> property) {
		return properties.containsValue(property);
	}

	private Map<String, Property<?>> properties = new HashMap<>();
}
