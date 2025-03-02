package org.ranch.ballshack.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {

	private final Map<Class<?>, Map<Object, Method>> subscribers = new ConcurrentHashMap<>();

	public boolean subscribe(Object subscriber) {
		for (Method method : subscriber.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent(EventSubscribe.class) && method.getParameters().length != 0) {

				Class<?> eventType = method.getParameterTypes()[0];
				subscribers
						.computeIfAbsent(eventType, k -> new ConcurrentHashMap<>()) // Ensure the inner map exists
						.put(subscriber, method); // Map the instance to its method
				return true;

			}
		}
		return false;
	}

	public boolean unsubscribe(Object subscriber) {
		boolean[] unsubscribed = {false};
		subscribers.values().removeIf(map -> {
			if (map.remove(subscriber) != null) {
				unsubscribed[0] = true; // If a method was removed, set flag to true
			}
			return map.isEmpty();
		});
		return unsubscribed[0];
	}

	public void post(Event event) {
		Map<Object, Method> subscribed = subscribers.get(event.getClass());
		if (subscribed == null) {
			return;
		}

		for (Map.Entry<Object, Method> entry : subscribed.entrySet()) {
			try {
				entry.getValue().invoke(entry.getKey(), event); // Call method on the object
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace(); // Handle reflection errors
			}
		}
	}
}