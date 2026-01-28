package org.ranch.ballshack.event;

import org.ranch.ballshack.BallsHack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {

	private final Map<Class<?>, Map<Object, List<Method>>> subscribers = new ConcurrentHashMap<>();

	public boolean subscribe(Object subscriber) {
		boolean subscribed = false;

		for (Method method : subscriber.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent(EventSubscribe.class) && method.getParameterCount() == 1) {
				Class<?> eventType = method.getParameterTypes()[0];
				subscribers
						.computeIfAbsent(eventType, k -> new ConcurrentHashMap<>())
						.computeIfAbsent(subscriber, k -> new ArrayList<>())
						.add(method);
				subscribed = true;
			}
		}
		return subscribed;
	}

	public boolean unsubscribe(Object subscriber) {
		final boolean[] unsubscribed = {false};
		subscribers.values().removeIf(map -> {
			if (map.remove(subscriber) != null) {
				unsubscribed[0] = true;
			}
			return map.isEmpty();
		});
		return unsubscribed[0];
	}

	public void post(Event event) {
		Map<Object, List<Method>> subscribed = subscribers.get(event.getClass());
		if (subscribed == null) {
			return;
		}

		for (Map.Entry<Object, List<Method>> entry : subscribed.entrySet()) {
			Object subscriber = entry.getKey();
			for (Method method : entry.getValue()) {
				if (method.isAnnotationPresent(NoWorld.class) || BallsHack.mc.world != null) {
					try {
						method.invoke(subscriber, event);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}