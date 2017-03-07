package in.strollup.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Utils {
	public static <T> List<T> getSublist(Collection<T> elements, int maxSize) {
		if (isEmptyOrNull(elements)) {
			return new ArrayList<T>();
		}
		List<T> list = new ArrayList<T>(elements);
		if (elements.size() > maxSize) {
			list = new ArrayList<T>(list.subList(0, maxSize));
		}

		return list;
	}
	
	public static <T> boolean isEmptyOrNull(Collection<T> elements) {
		if (elements == null || elements.isEmpty()) {
			return true;
		}
		return false;
	}
}
