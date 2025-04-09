package com.notenhanh.service.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginAttemptService {

	private Map<String, Integer> attemptsMemory = new HashMap<>();
	private Map<String, Long> lockTimeMemory = new HashMap<>();

	@Value("${loginFailedCountAttempt}")
	private int loginFailedCountAttempt;

	@Value("${lockTime}")
	private int lockTimefollowMinutes;

	public int getLockTimefollowMinutes() {
		return lockTimefollowMinutes;
	}

	public boolean CheckAccountLocked(String username) {
		if (!lockTimeMemory.containsKey(username)) {
			return false;
		}

		long lockTime = lockTimeMemory.get(username);
		long currentTime = System.currentTimeMillis();

		if (currentTime >= lockTime) {
			clearLock(username);
			return false;
		}

		return true;
	}

	public void loginFailed(String username) {
		int attempts = attemptsMemory.getOrDefault(username, 0) + 1;
		attemptsMemory.put(username, attempts);

		if (attempts >= loginFailedCountAttempt && !lockTimeMemory.containsKey(username)) {
			lockTimeMemory.put(username, System.currentTimeMillis() + (lockTimefollowMinutes * 60000));
		}
	}

	public void clearLock(String username) {
		attemptsMemory.remove(username);
		lockTimeMemory.remove(username);
	}

	public int getLoginAttempts(String username) {
		return attemptsMemory.getOrDefault(username, 0);
	}

	public long getLockTime(String username) {
		return lockTimeMemory.getOrDefault(username, 0L);
	}
}